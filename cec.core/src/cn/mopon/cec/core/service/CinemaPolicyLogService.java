package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.mail.CinemaPolicyApproveMailModel;
import cn.mopon.cec.core.mail.CinemaPolicyApprovePassMailModel;
import cn.mopon.cec.core.mail.CinemaPolicyApproveRefuseMailModel;
import cn.mopon.cec.core.mail.CinemaPolicyAuditMailModel;
import cn.mopon.cec.core.mail.CinemaPolicyAuditRefuseMailModel;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.model.CinemaRuleListModel;
import cn.mopon.cec.core.model.PolicyLogSearchModel;
import coo.base.model.Page;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;

/**
 * 结算策略审批记录管理。
 */
@Service
public class CinemaPolicyLogService {
	@Resource
	private Dao<CinemaPolicyLog> cinemaPolicyLogDao;
	@Resource
	private CinemaPolicyService cinemaPolicyService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private SecurityService securityService;
	@Resource
	private MailService mailService;

	/**
	 * 搜索审批结算策略记录。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的审批结算策略记录分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<CinemaPolicyLog> searchPolicyLog(
			PolicyLogSearchModel searchModel) {
		searchModel.setPageSize(25);
		FullTextCriteria criteria = cinemaPolicyLogDao.createFullTextCriteria();
		criteria.addFilterField("status", searchModel.getStatus().getValue());
		criteria.addSearchField("policy.cinema.name", Analyze.NO);
		criteria.addSearchField("policy.cinema.county.city.name", Analyze.NO);
		criteria.addSortAsc("policy.cinema.county.city.code",
				SortField.Type.STRING);
		criteria.setKeyword(searchModel.getKeyword());
		return cinemaPolicyLogDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 获取指定ID的结算策略审批记录。
	 * 
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @return 返回指定ID的结算策略审批记录。
	 */
	@Transactional(readOnly = true)
	public CinemaPolicyLog getPolicyLog(String policyLogId) {
		return cinemaPolicyLogDao.get(policyLogId);
	}

	/**
	 * 提交结算策略。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 */
	@Transactional
	public void submitPolicy(CinemaPolicyLog policyLog) {
		CinemaPolicy policy = cinemaPolicyService.getPolicy(policyLog
				.getPolicy().getId());
		checkPolicyCanSubmit(policy);
		// 如果结算策略的最后一条审批记录是“已退回”状态，则将该审批记录的状态设置为“已处理”。
		CinemaPolicyLog lastRefusedPolicyLog = policy.getLastRefusedLog();
		if (lastRefusedPolicyLog != null) {
			lastRefusedPolicyLog.setStatus(AuditStatus.PROCESSED);
		}
		// 设置结算策略审批记录为“待审核”状态。
		policyLog.setStatus(AuditStatus.AUDIT);
		policyLog.setSubmitTime(new Date());
		policyLog.setSubmitter(securityService.getCurrentUser());
		cinemaPolicyLogDao.save(policyLog);
		// 设置结算策略为“待审核”状态。
		policy.setStatus(PolicyStatus.AUDIT);
		// 设置结算策略规则为“审核退回”修改为“未审核”。
		for (CinemaRule rule : policyLog.getPolicy().getRules()) {
			if (rule.getStatus() == RuleStatus.REFUSE) {
				rule.setStatus(RuleStatus.UNAUDIT);
			}
		}
		// 发送提醒邮件
		policyLog.setPolicy(policy);
		List<User> users = securityService.findUserByPermission("POLICY_AUDIT");
		CinemaPolicyAuditMailModel mailModel = new CinemaPolicyAuditMailModel(
				policyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 检查策略是否可以提交。
	 * 
	 * @param policy
	 *            结算策略
	 */
	private void checkPolicyCanSubmit(CinemaPolicy policy) {
		// 如果结算策略不是“待提交”状态，不允许进行该操作。
		if (policy.getStatus() != PolicyStatus.SUBMIT) {
			messageSource.thrown("cinemaPolicy.submit.repeat");
		}
		// 如果结算策略“已过期”状态，不允许进行该操作。
		if (policy.getExpired()) {
			messageSource.thrown("cinemaPolicy.audit.expired");
		}
	}

	/**
	 * 审核通过结算策略。
	 * 
	 * @param policyLogId
	 *            结算策略审批记录ID
	 */
	@Transactional
	public void auditPassPolicy(String policyLogId) {
		CinemaPolicyLog policyLog = getPolicyLog(policyLogId);
		// 检查策略是否允许审核通过。
		checkCinemaPolicyAuditPass(policyLog);
		// 设置结算策略审批记录为"待审批"状态。
		policyLog.setStatus(AuditStatus.APPROVE);
		policyLog.setAuditor(securityService.getCurrentUser());
		policyLog.setAuditTime(new Date());
		// 设置结算策略为“待审批”状态。
		policyLog.getPolicy().setStatus(PolicyStatus.APPROVE);
		// 设置结算策略规则为“审核通过”状态
		for (CinemaRule rule : policyLog.getPolicy().getRules()) {
			// 将“规则标记通过”修改为“未审核”。
			if (rule.getStatus() == RuleStatus.AUDITPASS) {
				rule.setStatus(RuleStatus.UNAUDIT);
			}
		}
		// 发送提醒邮件
		List<User> users = securityService
				.findUserByPermission("POLICY_APPROVE");
		CinemaPolicyApproveMailModel mailModel = new CinemaPolicyApproveMailModel(
				policyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审核退回结算策略。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 */
	@Transactional
	public void auditRefusePolicy(CinemaPolicyLog policyLog) {
		CinemaPolicyLog origPolicyLog = getPolicyLog(policyLog.getId());
		// 检查策略是否允许审核退回。
		// 如果结算策略审批记录不是“待审核”状态，不允许进行该操作。
		if (policyLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("cinemaPolicy.audit.repeat");
		}
		// 设置结算策略审批记录审批为"已退回"状态。
		origPolicyLog.setStatus(AuditStatus.REFUSED);
		origPolicyLog.setAuditor(securityService.getCurrentUser());
		origPolicyLog.setAuditTime(new Date());
		// 设置结算策略为"已退回"状态。
		origPolicyLog.getPolicy().setStatus(PolicyStatus.BACKED);
		for (CinemaRule rule : policyLog.getPolicy().getRules()) {
			if (rule.isUnAudit()) {
				rule.setStatus(RuleStatus.REFUSE);
			}
		}
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origPolicyLog.getSubmitter());
		CinemaPolicyAuditRefuseMailModel mailModel = new CinemaPolicyAuditRefuseMailModel(
				origPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批通过结算策略。
	 * 
	 * @param policyLogId
	 *            结算策略审批记录ID
	 */
	@Transactional
	public void approvePassPolicy(String policyLogId) {
		CinemaPolicyLog origPolicyLog = getPolicyLog(policyLogId);
		// 检查策略是否允许审批通过。
		checkCinemaPolicyApprovePass(origPolicyLog);
		// 设置结算策略审批记录为"已审批"状态。
		origPolicyLog.setStatus(AuditStatus.APPROVED);
		origPolicyLog.setApprover(securityService.getCurrentUser());
		origPolicyLog.setApproveTime(new Date());
		// 设置结算策略为“已审批”状态。
		origPolicyLog.getPolicy().setStatus(PolicyStatus.APPROVED);
		// 设置结算策略为"启用、已生效"状态。
		origPolicyLog.getPolicy().setValid(ValidStatus.VALID);
		origPolicyLog.getPolicy().setEnabled(EnabledStatus.ENABLED);
		// 处理结算策略规则和结算策略渠道。
		processPassedPolicyRules(origPolicyLog.getPolicy());
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origPolicyLog.getSubmitter());
		users.add(origPolicyLog.getAuditor());
		CinemaPolicyApprovePassMailModel mailModel = new CinemaPolicyApprovePassMailModel(
				origPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批退回结算策略。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 */
	@Transactional
	public void approveRefusePolicy(CinemaPolicyLog policyLog) {
		CinemaPolicyLog origPolicyLog = getPolicyLog(policyLog.getId());
		// 检查策略是否允许审批通过。
		// 如果结算策略审批记录不是“待审批”状态，不允许进行该操作。
		if (policyLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("cinemaPolicy.approve.repeat");
		}
		// 设置结算策略审批记录审批为"已退回"状态。
		origPolicyLog.setStatus(AuditStatus.REFUSED);
		origPolicyLog.setApprover(securityService.getCurrentUser());
		origPolicyLog.setApproveTime(new Date());
		// 设置结算策略为"已退回"状态。
		origPolicyLog.getPolicy().setStatus(PolicyStatus.BACKED);
		for (CinemaRule rule : policyLog.getPolicy().getRules()) {
			if (rule.isUnAudit()) {
				rule.setStatus(RuleStatus.REFUSE);
			}
		}
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origPolicyLog.getSubmitter());
		users.add(origPolicyLog.getAuditor());
		CinemaPolicyApproveRefuseMailModel mailModel = new CinemaPolicyApproveRefuseMailModel(
				origPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 处理审批通过的结算策略规则。
	 * 
	 * @param policy
	 *            结算策略
	 */
	private void processPassedPolicyRules(CinemaPolicy policy) {
		for (CinemaRule cinemaRule : policy.getRules()) {
			// 对“未生效”状态的结算策略规则进行处理。
			if (cinemaRule.getValid() == ValidStatus.UNVALID) {
				if (cinemaRule.isBounded()) {
					CinemaRule boundCinemaRule = cinemaRule.getBoundRule();
					// 双方解除绑定。
					boundCinemaRule.setBoundRule(null);
					cinemaRule.setBoundRule(null);
					// 将该结算规则状态修改为“已失效”、“停用”。
					boundCinemaRule.setValid(ValidStatus.INVALID);
					boundCinemaRule.setEnabled(EnabledStatus.DISABLED);
				}
				// 将结算规则审批状态修改为“未审核”。
				cinemaRule.setStatus(RuleStatus.UNAUDIT);
				cinemaRule.setValid(ValidStatus.VALID);
				cinemaRule.setEnabled(EnabledStatus.ENABLED);
			}
		}
	}

	/**
	 * 检查结算策略是否允许审核通过。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 */
	private void checkCinemaPolicyAuditPass(CinemaPolicyLog policyLog) {
		// 如果结算策略审批记录不是“待审核”状态，不允许进行该操作。
		if (policyLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("cinemaPolicy.audit.repeat");
		}
		for (CinemaRule rule : policyLog.getPolicy().getRules()) {
			// 如果结算策略存在未审核的规则，不允许进行该操作。
			if (rule.isUnAudit()) {
				messageSource.thrown("cinemaPolicy.audit.pass.unaudit");
			}
			// 如果结算策略存在审核退回的规则，不允许进行该操作。
			if (rule.getStatus() == RuleStatus.REFUSE) {
				messageSource.thrown("cinemaPolicy.audit.pass.invalid");
			}
		}
	}

	/**
	 * 检查结算策略是否允许审核通过。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 */
	private void checkCinemaPolicyApprovePass(CinemaPolicyLog policyLog) {
		// 如果结算策略审批记录不是“待审批”状态，不允许进行该操作。
		if (policyLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("cinemaPolicy.approve.repeat");
		}
		for (CinemaRule rule : policyLog.getPolicy().getRules()) {
			// 如果结算策略存在未审批的规则，不允许进行该操作。
			if (rule.isUnAudit()) {
				messageSource.thrown("cinemaPolicy.approve.pass.unapprove");
			}
			// 如果结算策略存在审批退回的规则，不允许进行该操作。
			if (rule.getStatus() == RuleStatus.REFUSE) {
				messageSource.thrown("cinemaPolicy.approve.pass.invalid");
			}
		}
	}

	/**
	 * 检索结算策略规则。
	 * 
	 * @param policy
	 *            结算策略
	 * @param status
	 *            规则状态
	 * @return 符合条件的结算规则模型。
	 */
	public CinemaRuleListModel searchCinemaRule(CinemaPolicy policy,
			RuleStatus status) {
		if (status == null) {
			return policy.getAllCinemaRuleList();
		} else if (status == RuleStatus.UNAUDIT) {
			return policy.getUnAuditCinemaRuleList();
		} else if (status == RuleStatus.AUDITPASS) {
			return policy.getAuditpassCinemaRuleList();
		} else if (status == RuleStatus.APPROVEPASS) {
			return policy.getApprovepassCinemaRuleList();
		} else {
			return policy.getRefuseCinemaRuleList();
		}
	}
}