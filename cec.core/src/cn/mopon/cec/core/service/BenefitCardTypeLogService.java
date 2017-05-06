package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.hibernate.search.annotations.Analyze;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeLog;
import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.mail.BenefitCardTypeApproveMailModel;
import cn.mopon.cec.core.mail.BenefitCardTypeApprovePassMailModel;
import cn.mopon.cec.core.mail.BenefitCardTypeApproveRefuseMailModel;
import cn.mopon.cec.core.mail.BenefitCardTypeAuditMailModel;
import cn.mopon.cec.core.mail.BenefitCardTypeAuditRefuseMailModel;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.model.CardTypeLogSearchModel;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;

/**
 * 权益卡卡类审批管理。
 */
@Service
public class BenefitCardTypeLogService {
	@Resource
	private MessageSource messageSource;
	@Resource
	private SecurityService securityService;
	@Resource
	private Dao<BenefitCardTypeLog> benefitCardTypeLogDao;
	@Resource
	private Dao<BenefitCardType> benefitCardTypeDao;
	@Resource
	private MailService mailService;

	/**
	 * 提交结算策略。
	 * 
	 * @param cardTypeLog
	 *            权益卡类审批记录
	 */
	@Transactional
	public void submitCardType(BenefitCardTypeLog cardTypeLog) {
		BenefitCardType cardType = cardTypeLog.getType();
		// 如果不是“待提交”状态，不允许进行该操作。
		if (cardType.getStatus() != PolicyStatus.SUBMIT) {
			messageSource.thrown("benefitCardType.submit.repeat");
		}
		// 如果结算策略的最后一条审批记录是“已退回”状态，则将该审批记录的状态设置为“已处理”。
		BenefitCardTypeLog lastRefusedLog = cardType.getLastRefusedLog();
		if (lastRefusedLog != null) {
			lastRefusedLog.setStatus(AuditStatus.PROCESSED);
		}
		// 设置权益卡审批记录为“待审核”状态。
		cardTypeLog.setStatus(AuditStatus.AUDIT);
		cardTypeLog.setSubmitTime(new Date());
		cardTypeLog.setSubmitter(securityService.getCurrentUser());
		benefitCardTypeLogDao.save(cardTypeLog);
		// 设置策略审批为“待审核”
		cardType.setStatus(PolicyStatus.AUDIT);
		// 发送提醒邮件
		List<User> users = securityService
				.findUserByPermission("BENEFITCARDTYPE_AUDIT");
		BenefitCardTypeAuditMailModel mailModel = new BenefitCardTypeAuditMailModel(
				cardTypeLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审核通过权益卡卡类。
	 * 
	 * @param logId
	 *            权益卡卡类审批记录ID
	 */
	@Transactional
	public void auditPassBenefitCardType(String logId) {
		BenefitCardTypeLog origLog = getBenefitCardTypeLog(logId);
		// 如果审批记录不是处于“待审核”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("benefitCardType.audit.repeat");
		}
		// 设置审批记录状态为“待审批”。
		origLog.setStatus(AuditStatus.APPROVE);
		origLog.setAuditor(securityService.getCurrentUser());
		origLog.setAuditTime(new Date());
		// 设置策略审批为“待审批”。
		origLog.getType().setStatus(PolicyStatus.APPROVE);
		// 发送提醒邮件
		List<User> users = securityService
				.findUserByPermission("BENEFITCARDTYPE_APPROVE");
		BenefitCardTypeApproveMailModel mailModel = new BenefitCardTypeApproveMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审核退回权益卡卡类。
	 * 
	 * @param cardTypeLog
	 *            权益卡卡类审批记录
	 */
	@Transactional
	public void auditRefuseBenefitCardType(BenefitCardTypeLog cardTypeLog) {
		BenefitCardTypeLog origLog = getBenefitCardTypeLog(cardTypeLog.getId());
		// 如果审批记录不是处于“待审核”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("benefitCardType.audit.repeat");
		}
		// 设置审批记录状态为“已退回”。
		origLog.setStatus(AuditStatus.REFUSED);
		origLog.setAuditor(securityService.getCurrentUser());
		origLog.setAuditTime(new Date());
		origLog.setRefuseNote(cardTypeLog.getRefuseNote());
		// 设置策略审批为“已退回”。
		origLog.getType().setStatus(PolicyStatus.BACKED);
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origLog.getSubmitter());
		BenefitCardTypeAuditRefuseMailModel mailModel = new BenefitCardTypeAuditRefuseMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批退回权益卡卡类。
	 * 
	 * @param cardTypeLog
	 *            权益卡卡类审批记录
	 */
	@Transactional
	public void approveRefuseBenefitCardType(BenefitCardTypeLog cardTypeLog) {
		BenefitCardTypeLog origLog = getBenefitCardTypeLog(cardTypeLog.getId());
		// 如果审批记录不是处于“待审核”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("benefitCardType.approve.repeat");
		}
		// 设置审批记录状态为“待审批”。
		origLog.setStatus(AuditStatus.REFUSED);
		origLog.setApprover(securityService.getCurrentUser());
		origLog.setApproveTime(new Date());
		origLog.setRefuseNote(cardTypeLog.getRefuseNote());
		// 设置策略审批为“待审批”。
		origLog.getType().setStatus(PolicyStatus.BACKED);
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origLog.getSubmitter());
		users.add(origLog.getAuditor());
		BenefitCardTypeApproveRefuseMailModel mailModel = new BenefitCardTypeApproveRefuseMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批通过权益卡卡类。
	 * 
	 * @param logId
	 *            权益卡卡类审批记录ID
	 */
	@Transactional
	public void approvePassBenefitCardType(String logId) {
		BenefitCardTypeLog origLog = getBenefitCardTypeLog(logId);
		// 如果审批记录不是处于“待审批”状态，不允许该操作。
		if (origLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("benefitCardType.approve.repeat");
		}
		// 设置审批记录状态为“已审批”。
		origLog.setStatus(AuditStatus.APPROVED);
		origLog.setApprover(securityService.getCurrentUser());
		origLog.setApproveTime(new Date());
		// 处理规则，排期，权益卡类。
		processPassedCardTypeRules(origLog.getType());
		processPassedCardTypeSnackRules(origLog.getType());
		processPassedCardType(origLog.getType());
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(origLog.getSubmitter());
		users.add(origLog.getAuditor());
		BenefitCardTypeApprovePassMailModel mailModel = new BenefitCardTypeApprovePassMailModel(
				origLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 查询权益卡审批记录。
	 * 
	 * @param searchModel
	 *            查询条件。
	 * @return 权益卡审批记录列表。
	 */
	@Transactional(readOnly = true)
	public List<BenefitCardTypeLog> searchBenefitCardTypeLog(
			CardTypeLogSearchModel searchModel) {
		FullTextCriteria criteria = benefitCardTypeLogDao
				.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("type.name", Analyze.NO);
		criteria.addFilterField("status", searchModel.getStatus().getValue());
		criteria.addSortDesc(searchModel.getOrderBy(), SortField.Type.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return benefitCardTypeLogDao.searchBy(criteria);
	}

	/**
	 * 获取指定ID的权益卡审批记录。
	 * 
	 * @param benefitCardTypeLogId
	 *            权益卡审批记录ID
	 * @return 返回指定ID的权益卡审批记录。
	 */
	@Transactional(readOnly = true)
	public BenefitCardTypeLog getBenefitCardTypeLog(String benefitCardTypeLogId) {
		return benefitCardTypeLogDao.get(benefitCardTypeLogId);
	}

	/**
	 * 处理审批通过的权益卡类。
	 * 
	 * @param cardType
	 *            权益卡类
	 */
	private void processPassedCardType(BenefitCardType cardType) {
		// 如果权益卡没有绑定，则修改为审批通过状态。
		if (!cardType.isBounded()) {
			cardType.setValid(ValidStatus.VALID);
			cardType.setStatus(PolicyStatus.APPROVED);
		} else {
			BenefitCardType boundCardType = benefitCardTypeDao.get(cardType
					.getBoundType().getId());
			// 如果权益卡有绑定，则修改当前为失效状态，把可修改的属性copy到原有记录上。
			copyFields(cardType, boundCardType);
			cardType.setValid(ValidStatus.INVALID);
			boundCardType.setBoundType(null);
			benefitCardTypeDao.update(boundCardType);
			cardType.setBoundType(null);
			benefitCardTypeDao.remove(cardType);
		}
	}

	/**
	 * 复制属性。
	 * 
	 * @param cardType
	 *            新卡类
	 * @param boundCardType
	 *            绑定的卡类
	 */
	private void copyFields(BenefitCardType cardType,
			BenefitCardType boundCardType) {
		boundCardType.setName(cardType.getName());
		boundCardType.setPrefix(cardType.getPrefix());
		boundCardType.setInitAmount(cardType.getInitAmount());
		boundCardType.setRechargeAmount(cardType.getRechargeAmount());
		boundCardType.setTotalDiscountCount(cardType.getTotalDiscountCount());
		boundCardType.setDailyDiscountCount(cardType.getDailyDiscountCount());
		boundCardType.setValidMonth(cardType.getValidMonth());
		boundCardType.getChannels().clear();
		for (Channel channel : cardType.getChannels()) {
			boundCardType.getChannels().add(channel);
		}
		for (BenefitCardTypeLog log : cardType.getLogs()) {
			log.setType(boundCardType);
			boundCardType.getLogs().add(log);
		}
	}

	/**
	 * 处理审批通过的权益卡类规则。
	 * 
	 * @param cardType
	 *            权益卡类
	 */
	private void processPassedCardTypeRules(BenefitCardType cardType) {
		List<BenefitCardTypeRule> rules;
		if (!cardType.isBounded()) {
			rules = cardType.getRules();
		} else {
			rules = cardType.getBoundType().getRules();
		}

		for (BenefitCardTypeRule rule : rules) {
			// 对“未生效”状态的规则进行处理。
			if (rule.getValid() == ValidStatus.UNVALID) {
				rule.setValid(ValidStatus.VALID);
				rule.setEnabled(EnabledStatus.ENABLED);
				// 如果规则有绑定，则设置原有规则为失效状态,再把编辑之后的规则添加到原有卡类。
				if (rule.isBounded()) {
					BenefitCardTypeRule boundRule = rule.getBoundRule();
					boundRule.setBoundRule(null);
					rule.setBoundRule(null);
					boundRule.setValid(ValidStatus.INVALID);
					boundRule.setEnabled(EnabledStatus.DISABLED);
				}
			}
		}
	}

	/**
	 * 处理审批通过的权益卡类卖品规则。
	 * 
	 * @param cardType
	 *            权益卡类
	 */
	private void processPassedCardTypeSnackRules(BenefitCardType cardType) {
		List<BenefitCardTypeSnackRule> snackRules;
		if (!cardType.isBounded()) {
			snackRules = cardType.getSnackRules();
		} else {
			snackRules = cardType.getBoundType().getSnackRules();
		}

		for (BenefitCardTypeSnackRule rule : snackRules) {
			// 对“未生效”状态的规则进行处理。
			if (rule.getValid() == ValidStatus.UNVALID) {
				rule.setValid(ValidStatus.VALID);
				rule.setEnabled(EnabledStatus.ENABLED);
				// 如果规则有绑定，则设置原有规则为失效状态,再把编辑之后的规则添加到原有卡类。
				if (rule.isBounded()) {
					BenefitCardTypeSnackRule boundRule = rule.getBoundRule();
					boundRule.setBoundRule(null);
					rule.setBoundRule(null);
					boundRule.setValid(ValidStatus.INVALID);
					boundRule.setEnabled(EnabledStatus.DISABLED);
				}
			}
		}
	}
}