package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.SpecialPolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.mail.SpecialPolicyApproveMailModel;
import cn.mopon.cec.core.mail.SpecialPolicyApprovePassMailModel;
import cn.mopon.cec.core.mail.SpecialPolicyApproveRefuseMailModel;
import cn.mopon.cec.core.mail.SpecialPolicyAuditMailModel;
import cn.mopon.cec.core.mail.SpecialPolicyAuditRefuseMailModel;
import cn.mopon.cec.core.model.SpecialPolicyLogSearchModel;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;

/**
 * 特殊定价策略审批管理。
 */
@Service
public class SpecialPolicyLogService {
	@Resource
	private Dao<SpecialPolicyLog> specialPolicyLogDao;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private SecurityService securityService;
	@Resource
	private MailService mailService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 搜索特殊定价策略审批记录。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的特殊定价策略审批记录对象。
	 */
	@Transactional(readOnly = true)
	public List<SpecialPolicyLog> searchSpecialPolicyLog(
			SpecialPolicyLogSearchModel searchModel) {
		FullTextCriteria criteria = specialPolicyLogDao
				.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("policy.name", Analyze.NO);
		criteria.addFilterField("status", searchModel.getStatus().getValue());
		criteria.addSortDesc(searchModel.getOrderBy(), SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return specialPolicyLogDao.searchBy(criteria);
	}

	/**
	 * 根据ID获取指定特殊定价策略审批记录。
	 * 
	 * @param logId
	 *            审批记录ID
	 * @return 返回指定的特殊定价策略审批记录。
	 */
	@Transactional
	public SpecialPolicyLog getSpecialPolicyLog(String logId) {
		return specialPolicyLogDao.get(logId);
	}

	/**
	 * 提交特殊定价策略。
	 * 
	 * @param log
	 *            审批记录
	 */
	@Transactional
	public void submitSpecialPolicy(SpecialPolicyLog log) {
		SpecialPolicy origPolicy = specialPolicyService.getSpecialPolicy(log
				.getPolicy().getId());
		// 如果策略不是“待提交”状态，不允许进行该操作。
		if (origPolicy.getStatus() != SpecialPolicyStatus.SUBMIT) {
			messageSource.thrown("specialPolicy.submit.repeat");
		}
		// 如果策略已过期，不允许进行该操作。
		if (origPolicy.getExpired()) {
			messageSource.thrown("specialPolicy.audit.expired");
		}
		// 如果策略的最后一条审批记录是“已退回”状态，则将该审批记录的状态设置为“已处理”。
		SpecialPolicyLog lastRefusedLog = origPolicy.getLastRefusedLog();
		if (lastRefusedLog != null) {
			lastRefusedLog.setStatus(AuditStatus.PROCESSED);
		}
		// 设置审批记录状态为“待审核”
		origPolicy.setStatus(SpecialPolicyStatus.AUDIT);
		log.setSubmitTime(new Date());
		log.setSubmitter(securityService.getCurrentUser());
		specialPolicyLogDao.save(log);
		// 设置策略审批为“待审核”
		origPolicy.setStatus(SpecialPolicyStatus.AUDIT);
		// 发送提醒邮件
		List<User> users = securityService.findUserByPermission("POLICY_AUDIT");
		SpecialPolicyAuditMailModel mailModel = new SpecialPolicyAuditMailModel(
				log, users);
		mailService.send(mailModel);
	}

	/**
	 * 审核通过特殊定价策略。
	 * 
	 * @param logId
	 *            特殊定价策略审批记录ID
	 */
	@Transactional
	public void auditPassSpecialPolicy(String logId) {
		SpecialPolicyLog origLog = getSpecialPolicyLog(logId);
		// 如果审批记录不是处于“待审核”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("specialPolicy.audit.repeat");
		}
		// 设置审批记录状态为“待审核”。
		origLog.setStatus(AuditStatus.APPROVE);
		origLog.setAuditor(securityService.getCurrentUser());
		origLog.setAuditTime(new Date());
		// 设置策略审批为“待审核”。
		origLog.getPolicy().setStatus(SpecialPolicyStatus.APPROVE);
		// 发送提醒邮件
		List<User> users = securityService
				.findUserByPermission("POLICY_APPROVE");
		SpecialPolicyApproveMailModel mailModel = new SpecialPolicyApproveMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审核退回特殊定价策略。
	 * 
	 * @param log
	 *            审批记录
	 */
	@Transactional
	public void auditRefuseSpecialPolicy(SpecialPolicyLog log) {
		SpecialPolicyLog origLog = getSpecialPolicyLog(log.getId());
		// 如果审批记录不是处于“待审核”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("specialPolicy.audit.repeat");
		}
		// 设置审批记录状态为“已退回”。
		origLog.setStatus(AuditStatus.REFUSED);
		origLog.setAuditor(securityService.getCurrentUser());
		origLog.setAuditTime(new Date());
		origLog.setRefuseNote(log.getRefuseNote());
		// 设置策略审批为“已退回”。
		origLog.getPolicy().setStatus(SpecialPolicyStatus.BACKED);
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origLog.getSubmitter());
		SpecialPolicyAuditRefuseMailModel mailModel = new SpecialPolicyAuditRefuseMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批通过特殊定价策略。
	 * 
	 * @param logId
	 *            审批记录ID
	 */
	@Transactional
	public void approvePassSpecialPolicy(String logId) {
		SpecialPolicyLog origLog = getSpecialPolicyLog(logId);
		// 如果审批记录不是处于“待审批”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("specialPolicy.approve.repeat");
		}
		// 设置审批记录状态为“已审批”。
		origLog.setStatus(AuditStatus.APPROVED);
		origLog.setApprover(securityService.getCurrentUser());
		origLog.setApproveTime(new Date());
		// 设置策略审批状态为“已审批”。
		origLog.getPolicy().setStatus(SpecialPolicyStatus.APPROVED);
		// 设置策略状态为“已生效”。
		origLog.getPolicy().setValid(ValidStatus.VALID);
		// 处理规则和渠道。
		processPassedSpecialRules(origLog.getPolicy());
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origLog.getSubmitter());
		users.add(origLog.getAuditor());
		SpecialPolicyApprovePassMailModel mailModel = new SpecialPolicyApprovePassMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批退回特殊定价策略。
	 * 
	 * @param log
	 *            审批记录
	 */
	@Transactional
	public void approveRefuseSpecialPolicy(SpecialPolicyLog log) {
		SpecialPolicyLog origLog = getSpecialPolicyLog(log.getId());
		// 如果审批记录不是处于“待审批”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("specialPolicy.approve.repeat");
		}
		// 设置审批记录状态为“已退回”。
		origLog.setStatus(AuditStatus.REFUSED);
		origLog.setApprover(securityService.getCurrentUser());
		origLog.setApproveTime(new Date());
		origLog.setRefuseNote(log.getRefuseNote());
		// 设置策略审批状态为“已退回”。
		origLog.getPolicy().setStatus(SpecialPolicyStatus.BACKED);
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origLog.getSubmitter());
		users.add(origLog.getAuditor());
		SpecialPolicyApproveRefuseMailModel mailModel = new SpecialPolicyApproveRefuseMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 处理审批通过的特殊定价规则。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	private void processPassedSpecialRules(SpecialPolicy policy) {
		for (SpecialRule rule : policy.getRules()) {
			// 处理关联的特殊定价渠道。
			processPassedSpecialChannels(rule);
			// 对“未生效”状态的规则进行处理。
			if (rule.getValid() == ValidStatus.UNVALID) {
				// 如果有绑定的规则，解除绑定关系并将该规则状态改为”已失效“、”停用“，新的规则状态修改为”生效“、”启用“。
				if (rule.isBounded()) {
					SpecialRule boundRule = rule.getBoundRule();
					boundRule.setBoundRule(null);
					rule.setBoundRule(null);
					boundRule.setValid(ValidStatus.INVALID);
					boundRule.setEnabled(EnabledStatus.DISABLED);
				}
				rule.setValid(ValidStatus.VALID);
				rule.setEnabled(EnabledStatus.ENABLED);
			}
		}
	}

	/**
	 * 处理审批通过的特殊定价渠道。
	 * 
	 * @param rule
	 *            特殊定价规则
	 */
	private void processPassedSpecialChannels(SpecialRule rule) {
		Iterator<SpecialChannel> channelIterator = rule.getChannels()
				.iterator();
		while (channelIterator.hasNext()) {
			SpecialChannel channel = channelIterator.next();
			// 对“未生效”状态的渠道进行处理。
			if (channel.getValid() == ValidStatus.UNVALID) {
				// 如果有绑定的渠道，解除绑定关系并将该渠道状态改为”已失效“、”停用“，新的渠道状态修改为”生效“、”启用“。
				if (channel.isBounded()) {
					SpecialChannel boundChannel = channel.getBoundChannel();
					boundChannel.setBoundChannel(null);
					channel.setBoundChannel(null);
					boundChannel.setValid(ValidStatus.INVALID);
					boundChannel.setEnabled(EnabledStatus.DISABLED);
				}
				// 设置特殊定价渠道为“已生效”状态。
				channel.setValid(ValidStatus.VALID);
				channel.setEnabled(EnabledStatus.ENABLED);
			}
		}
	}
}