package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelPolicyLog;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.mail.ChannelPolicyApproveMailModel;
import cn.mopon.cec.core.mail.ChannelPolicyApprovePassMailModel;
import cn.mopon.cec.core.mail.ChannelPolicyApproveRefuseMailModel;
import cn.mopon.cec.core.mail.ChannelPolicyAuditMailModel;
import cn.mopon.cec.core.mail.ChannelPolicyAuditRefuseMailModel;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.model.ChannelChannelPolicyListModel;
import cn.mopon.cec.core.model.ChannelPolicyLogSearchModel;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;

/**
 * 策略审批记录管理。
 */
@Service
public class ChannelPolicyLogService {
	@Resource
	private Dao<ChannelPolicyLog> channelPolicyLogDao;
	@Resource
	private ChannelPolicyService channelPolicyService;
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private SecurityService securityService;
	@Resource
	private MailService mailService;

	/**
	 * 按渠道分组搜索审批策略记录。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的审批策略记录分页对象。
	 */
	@Transactional(readOnly = true)
	public ChannelChannelPolicyListModel searchPolicyLog(
			ChannelPolicyLogSearchModel searchModel) {
		FullTextCriteria criteria = channelPolicyLogDao
				.createFullTextCriteria();
		criteria.addFilterField("status", searchModel.getStatus().getValue());
		criteria.addSortAsc("policy.channel.code", SortField.Type.STRING);
		criteria.setKeyword(searchModel.getKeyword());
		List<ChannelPolicyLog> policyLogs = channelPolicyLogDao
				.searchBy(criteria);
		List<Channel> channels = new ArrayList<>();
		for (ChannelPolicyLog log : policyLogs) {
			if (!channels.contains(log.getPolicy().getChannel())) {
				channels.add(log.getPolicy().getChannel());
			}
		}
		ChannelChannelPolicyListModel channelPolicyLogListModel = new ChannelChannelPolicyListModel(
				channels);
		setChannelPolicyLogListModel(policyLogs, channelPolicyLogListModel);
		return channelPolicyLogListModel;
	}

	/**
	 * 向分组模型中装载数据。
	 * 
	 * @param policyLogs
	 *            策略审批记录
	 * 
	 * @param channelPolicyLogListModel
	 *            渠道结算策略审核记录按渠道分组的列表模型
	 */
	private void setChannelPolicyLogListModel(
			List<ChannelPolicyLog> policyLogs,
			ChannelChannelPolicyListModel channelPolicyLogListModel) {
		for (ChannelPolicyLog policyLog : policyLogs) {
			Integer count = channelRuleGroupService
					.searchChannelRuleGroupCount(policyLog.getPolicy().getId(),
							false);
			channelPolicyLogListModel.addPolicyLog(policyLog, count);
		}
	}

	/**
	 * 获取指定ID的策略审批记录。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @return 返回指定ID的策略审批记录。
	 */
	@Transactional(readOnly = true)
	public ChannelPolicyLog getChannelPolicyLog(String channelPolicyLogId) {
		return channelPolicyLogDao.get(channelPolicyLogId);
	}

	/**
	 * 获取指定ID的策略审批记录，根据城市过滤影院。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @param cinemaId
	 *            影院ID
	 * @return 返回指定ID的策略审批记录。
	 */
	@Transactional(readOnly = true)
	public ChannelPolicyLog getGroupChannelPolicyLog(String channelPolicyLogId,
			String cinemaId) {
		ChannelPolicyLog origChannelPolicyLog = channelPolicyLogDao
				.get(channelPolicyLogId);
		ChannelPolicyLog channelPolicyLog = new ChannelPolicyLog();
		BeanUtils.copyFields(origChannelPolicyLog, channelPolicyLog, "policy");
		ChannelPolicy channelPolicy = channelPolicyService.groupChannelPolicy(
				origChannelPolicyLog.getPolicy().getId(), cinemaId);
		channelPolicyLog.setPolicy(channelPolicy);
		Iterator<ChannelRuleGroup> groupIterable = channelPolicy.getGroups()
				.iterator();
		while (groupIterable.hasNext()) {
			ChannelRuleGroup group = groupIterable.next();
			if (!group.hasUnvalidRules()) {
				groupIterable.remove();
			}
		}
		return channelPolicyLog;
	}

	/**
	 * 提交策略。
	 * 
	 * @param channelPolicyId
	 *            策略审批记录
	 */
	@Transactional
	public void submitPolicy(String channelPolicyId) {
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		checkPolicyCanSubmit(channelPolicy);
		// 如果策略的最后一条审批记录是“已退回”状态，则将该审批记录的状态设置为“已处理”。
		if (CollectionUtils.isNotEmpty(channelPolicy.getLogs())) {
			ChannelPolicyLog lastRefusedPolicyLog = channelPolicy
					.getLastRefusedLog();
			if (lastRefusedPolicyLog != null) {
				lastRefusedPolicyLog.setStatus(AuditStatus.PROCESSED);
			}
		}
		ChannelPolicyLog channelPolicyLog = createChannelPolicyLog(channelPolicy);
		// 发送提醒邮件
		List<User> users = securityService.findUserByPermission("POLICY_AUDIT");
		ChannelPolicyAuditMailModel mailModel = new ChannelPolicyAuditMailModel(
				channelPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 创建策略审批记录。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回创建的策略审批记录。
	 */
	private ChannelPolicyLog createChannelPolicyLog(ChannelPolicy channelPolicy) {
		ChannelPolicyLog channelPolicyLog = new ChannelPolicyLog();
		// 设置策略审批记录为“待审核”状态。
		channelPolicyLog.setStatus(AuditStatus.AUDIT);
		channelPolicyLog.setSubmitTime(new Date());
		channelPolicyLog.setSubmitter(securityService.getCurrentUser());
		channelPolicyLog.setPolicy(channelPolicy);
		channelPolicyLogDao.save(channelPolicyLog);
		// 设置策略为“待审核”状态。
		channelPolicy.setStatus(PolicyStatus.AUDIT);
		List<ChannelRuleGroup> groups = channelRuleGroupService
				.searchUnvalidChannelRuleGroup(channelPolicyLog.getPolicy()
						.getId());
		// 将策略下的状态为“审核退回”的规则设置为“未审核”状态。
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
				// 对“未生效”状态的策略规则进行处理。
				if (rule.getStatus() == RuleStatus.REFUSE) {
					rule.setStatus(RuleStatus.UNAUDIT);
				}
			}
		}
		return channelPolicyLog;
	}

	/**
	 * 检查策略是否可以提交。
	 * 
	 * @param policy
	 *            结算策略
	 */
	private void checkPolicyCanSubmit(ChannelPolicy policy) {
		// 如果策略不是“待提交”状态，不允许进行该操作。
		if (policy.getStatus() != PolicyStatus.SUBMIT) {
			messageSource.thrown("channelPolicy.submit.repeat");
		}
		// 如果策略“已过期”状态，不允许进行该操作。
		if (policy.getExpired()) {
			messageSource.thrown("channelPolicy.audit.expired");
		}
		List<ChannelRuleGroup> groups = channelRuleGroupService
				.searchUnvalidChannelRuleGroup(policy.getId());
		if (groups.size() == 0) {
			messageSource.thrown("channelPolicy.no.unvalid.rule");
		} else {
			for (ChannelRuleGroup group : groups) {
				for (ChannelRule rule : group.getRules()) {
					if (rule.getValid() == ValidStatus.UNVALID) {
						return;
					}
				}
			}
			messageSource.thrown("channelPolicy.no.unvalid.rule");
		}
	}

	/**
	 * 审核通过策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 */
	@Transactional
	public void auditPassPolicy(String channelPolicyLogId) {
		ChannelPolicyLog channelPolicyLog = getChannelPolicyLog(channelPolicyLogId);
		List<ChannelRuleGroup> groups = channelRuleGroupService
				.searchUnvalidChannelRuleGroup(channelPolicyLog.getPolicy()
						.getId());
		channelPolicyService.filterChannelRule(channelPolicyLog.getPolicy(),
				groups);
		checkChannelPolicyAuditPass(channelPolicyLog, groups);
		// 设置策略审批记录为"待审批"状态。
		channelPolicyLog.setStatus(AuditStatus.APPROVE);
		channelPolicyLog.setAuditor(securityService.getCurrentUser());
		channelPolicyLog.setAuditTime(new Date());
		// 设置结算策略规则为“审核通过”状态
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
				if (rule.getStatus() == RuleStatus.AUDITPASS) {
					rule.setStatus(RuleStatus.UNAUDIT);
				}
			}
		}
		// 设置策略为“待审批”状态。
		channelPolicyLog.getPolicy().setStatus(PolicyStatus.APPROVE);
		// 发送提醒邮件
		List<User> users = securityService
				.findUserByPermission("POLICY_APPROVE");
		ChannelPolicyApproveMailModel mailModel = new ChannelPolicyApproveMailModel(
				channelPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审核退回策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 */
	@Transactional
	public void auditRefusePolicy(String channelPolicyLogId) {
		ChannelPolicyLog channelPolicyLog = getChannelPolicyLog(channelPolicyLogId);
		// 如果结算策略审批记录不是“待审核”状态，不允许进行该操作。
		if (channelPolicyLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("channelPolicy.audit.repeat");
		}
		// 设置策略审批记录审批为"已退回"状态。
		channelPolicyLog.setStatus(AuditStatus.REFUSED);
		channelPolicyLog.setAuditor(securityService.getCurrentUser());
		channelPolicyLog.setAuditTime(new Date());
		// 设置策略为"已退回"状态。
		channelPolicyLog.getPolicy().setStatus(PolicyStatus.BACKED);
		List<ChannelRuleGroup> groups = channelRuleGroupService
				.searchUnvalidChannelRuleGroup(channelPolicyLog.getPolicy()
						.getId());
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
				if (rule.isUnAudit()) {
					rule.setStatus(RuleStatus.REFUSE);
				}
			}
		}
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(channelPolicyLog.getSubmitter());
		ChannelPolicyAuditRefuseMailModel mailModel = new ChannelPolicyAuditRefuseMailModel(
				channelPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 审批通过策略。
	 * 
	 * @param policyLogId
	 *            策略审批记录ID
	 */
	@Transactional
	public void approvePassPolicy(String policyLogId) {
		ChannelPolicyLog channelPolicyLog = getChannelPolicyLog(policyLogId);
		List<ChannelRuleGroup> groups = channelRuleGroupService
				.searchUnvalidChannelRuleGroup(channelPolicyLog.getPolicy()
						.getId());
		channelPolicyService.filterChannelRule(channelPolicyLog.getPolicy(),
				groups);
		checkChannelPolicyApprovePass(channelPolicyLog, groups);
		// 设置策略审批记录为"已审批"状态。
		channelPolicyLog.setStatus(AuditStatus.APPROVED);
		channelPolicyLog.setApprover(securityService.getCurrentUser());
		channelPolicyLog.setApproveTime(new Date());
		// 设置策略为“已审批”状态。
		channelPolicyLog.getPolicy().setStatus(PolicyStatus.APPROVED);
		// 设置策略为"启用、已生效"状态。
		channelPolicyLog.getPolicy().setValid(ValidStatus.VALID);
		channelPolicyLog.getPolicy().setEnabled(EnabledStatus.ENABLED);
		// 处理策略规则和策略渠道。
		processPassedPolicyRules(channelPolicyLog.getPolicy(), groups);
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(channelPolicyLog.getSubmitter());
		users.add(channelPolicyLog.getAuditor());
		ChannelPolicyApprovePassMailModel mailModel = new ChannelPolicyApprovePassMailModel(
				channelPolicyLog, users);

		mailService.send(mailModel);
	}

	/**
	 * 审批退回策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录
	 */
	@Transactional
	public void approveRefusePolicy(String channelPolicyLogId) {
		ChannelPolicyLog channelPolicyLog = getChannelPolicyLog(channelPolicyLogId);
		// 如果结算策略审批记录不是“待审批”状态，不允许进行该操作。
		if (channelPolicyLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("channelPolicy.approve.repeat");
		}
		// 设置策略审批记录审批为"已退回"状态。
		channelPolicyLog.setStatus(AuditStatus.REFUSED);
		channelPolicyLog.setApprover(securityService.getCurrentUser());
		channelPolicyLog.setApproveTime(new Date());
		// 设置策略为"已退回"状态。
		channelPolicyLog.getPolicy().setStatus(PolicyStatus.BACKED);
		List<ChannelRuleGroup> groups = channelRuleGroupService
				.searchUnvalidChannelRuleGroup(channelPolicyLog.getPolicy()
						.getId());
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
				if (rule.isUnAudit()) {
					rule.setStatus(RuleStatus.REFUSE);
				}
			}
		}
		// 发送提醒邮件
		List<User> users = new ArrayList<User>();
		users.add(channelPolicyLog.getSubmitter());
		users.add(channelPolicyLog.getAuditor());
		ChannelPolicyApproveRefuseMailModel mailModel = new ChannelPolicyApproveRefuseMailModel(
				channelPolicyLog, users);
		mailService.send(mailModel);
	}

	/**
	 * 处理审批通过的策略规则。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @param groups
	 *            规则分组
	 */
	private void processPassedPolicyRules(ChannelPolicy channelPolicy,
			List<ChannelRuleGroup> groups) {
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
				// 对“未生效”状态的结算策略规则进行处理。
				if (rule.getValid() == ValidStatus.UNVALID) {
					if (rule.isBounded()) {
						ChannelRule boundPolicyRule = rule.getBoundRule();
						// 双方解除绑定。
						boundPolicyRule.setBoundRule(null);
						rule.setBoundRule(null);
						// 将该结算规则状态修改为“已失效”、“停用”。
						boundPolicyRule.setValid(ValidStatus.INVALID);
						boundPolicyRule.setEnabled(EnabledStatus.DISABLED);
					}
					rule.setValid(ValidStatus.VALID);
					rule.setEnabled(EnabledStatus.ENABLED);
					// 将结算规则审批状态修改为“未审核”。
					rule.setStatus(RuleStatus.UNAUDIT);
				}
			}
			group.setValid(true);
		}
	}

	/**
	 * 检查结算策略是否允许审核通过。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 * @param groups
	 *            规则分组
	 */
	private void checkChannelPolicyAuditPass(ChannelPolicyLog policyLog,
			List<ChannelRuleGroup> groups) {
		// 如果结算策略审批记录不是“待审核”状态，不允许进行该操作。
		if (policyLog.getStatus() != AuditStatus.AUDIT) {
			messageSource.thrown("channelPolicy.audit.repeat");
		}
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
				// 如果结算策略存在未审核的规则，不允许进行该操作。
				if (rule.isUnAudit()) {
					messageSource.thrown("channelPolicy.audit.pass.unaudit");
				}
				// 如果结算策略存在审核退回的规则，不允许进行该操作。
				if (rule.getStatus() == RuleStatus.REFUSE) {
					messageSource.thrown("channelPolicy.audit.pass.invalid");
				}
			}
		}
	}

	/**
	 * 检查结算策略是否允许审核通过。
	 * 
	 * @param policyLog
	 *            结算策略审批记录
	 * @param groups
	 *            规则分组
	 */
	private void checkChannelPolicyApprovePass(ChannelPolicyLog policyLog,
			List<ChannelRuleGroup> groups) {
		// 如果结算策略审批记录不是“待审批”状态，不允许进行该操作。
		if (policyLog.getStatus() != AuditStatus.APPROVE) {
			messageSource.thrown("channelPolicy.approve.repeat");
		}
		for (ChannelRuleGroup group : groups) {
			for (ChannelRule rule : group.getRules()) {
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
	}

	/**
	 * 按状态检索策略。
	 * 
	 * @param status
	 *            策略状态
	 * @return 符合条件的策略。
	 */
	@Transactional(readOnly = true)
	public List<ChannelPolicyLog> getChannelPolicyLog(AuditStatus status) {
		FullTextCriteria criteria = channelPolicyLogDao
				.createFullTextCriteria();
		criteria.addFilterField("status", status.getValue());
		return channelPolicyLogDao.searchBy(criteria);
	}
}
