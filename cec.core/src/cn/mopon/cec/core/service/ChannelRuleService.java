package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.SimpleLog;

/**
 * 渠道规则服务类。
 */
@Service
public class ChannelRuleService {
	@Resource
	private ChannelPolicyService channelPolicyService;
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private Dao<ChannelRule> channelRuleDao;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增渠道渠道结算规则。
	 * 
	 * @param channelRule
	 *            规则
	 */
	@AutoFillIn
	@Transactional
	public void createChannelRule(ChannelRule channelRule) {
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(channelRule.getGroup()
				.getPolicy(), "channelPolicyRule.add.approve");
		ChannelRuleGroup group = channelRuleGroupService
				.getChannelRuleGroup(channelRule.getGroup().getId());
		channelRuleDao.save(channelRule);
		group.getRules().add(channelRule);
		group.setValid(false);
		// 将结算策略修改为“待提交”状态。
		channelRule.getGroup().getPolicy().setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 根据规则ID获取渠道渠道结算规则。
	 * 
	 * @param channelRuleId
	 *            规则ID
	 * @return 返回指定ID的渠道渠道结算规则。
	 */
	@Transactional(readOnly = true)
	public ChannelRule getChannelRule(String channelRuleId) {
		return channelRuleDao.get(channelRuleId);
	}

	/**
	 * 更新渠道渠道结算规则。
	 * 
	 * @param channelRule
	 *            规则
	 */
	@AutoFillIn
	@Transactional
	public void updateChannelRule(ChannelRule channelRule) {
		ChannelRule origChannelRule = channelRuleDao.get(channelRule.getId());
		ChannelPolicy policy = channelPolicyService
				.getChannelPolicy(origChannelRule.getGroup().getPolicy()
						.getId());
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(policy,
				"channelPolicyRule.edit.approve");
		// 如果编辑的策略规则是已生效的，则新增一条绑定关系的策略规则；如果编辑的策略规则是未生效的，则直接更新该策略规则。
		if (origChannelRule.getValid() == ValidStatus.VALID) {
			// 如果渠道结算规则已经被编辑过，不允许再次进行编辑。
			if (channelRule.getBoundRule() != null) {
				messageSource.thrown("channelPolicyRule.edit.repeat");
			}
			channelRule.setId(null);
			channelRule.setValid(ValidStatus.UNVALID);
			channelRule.setEnabled(EnabledStatus.DISABLED);
			channelRule.setStatus(RuleStatus.UNAUDIT);
			channelRule.setBoundRule(origChannelRule);
			channelRule.autoFillIn();
			channelRuleDao.save(channelRule);
			origChannelRule.setBoundRule(channelRule);
			channelRule.getGroup().getRules().add(channelRule);
		} else {
			BeanUtils.copyFields(channelRule, origChannelRule,
					"ordinal,valid,enabled");
		}
		channelRule.getGroup().setValid(false);
		// 将结算策略修改为“待提交”状态。
		policy.setStatus(PolicyStatus.SUBMIT);
	}

	/**
	 * 删除渠道结算规则。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRule.delete.log", vars = {
			"channelRule.group.policy.channel.name",
			"channelRule.group.policy.name", "channelRule.group.cinema.name",
			"channelRule.name" })
	public void deleteChannelRule(ChannelRule channelRule) {
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(channelRule.getGroup()
				.getPolicy(), "channelPolicyRule.delete.approve");
		ChannelRuleGroup group = channelRule.getGroup();
		// 未生效的规则直接删除
		if (channelRule.getValid() == ValidStatus.UNVALID) {
			// 未生效的规则有捆绑规则，则捆绑规则解绑
			if (channelRule.isBounded()) {
				ChannelRule boundchannelRule = channelRule.getBoundRule();
				boundchannelRule.setBoundRule(null);
				group.getRules().add(boundchannelRule);
			}
			channelRuleDao.remove(channelRule);
		} else {
			// “已生效”的规则不允许删除。
			messageSource.thrown("channelPolicyRule.delete.valid");
		}
		group.getRules().remove(channelRule);
		if (!group.hasUnvalidRules()) {
			group.setValid(true);
		}
		Integer count = channelRuleGroupService.searchChannelRuleGroupCount(
				group.getPolicy().getId(), false);
		if ((count == 0 || (count == 1 && group.getValid()))
				&& group.getPolicy().getValid() != ValidStatus.UNVALID) {
			group.getPolicy().setStatus(PolicyStatus.APPROVED);
		}
	}

	/**
	 * 启用渠道结算规则。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRule.enable.log", vars = {
			"channelRule.group.policy.channel.name",
			"channelRule.group.policy.name", "channelRule.group.cinema.name",
			"channelRule.name" })
	public List<Show> enableChannelRule(ChannelRule channelRule) {
		// 如果渠道结算规则未生效，不允许启用。
		checkValidStatus(channelRule, "channelPolicyRule.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (channelRule.getEnabled() == EnabledStatus.DISABLED) {
			channelRule.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(channelRule);
		}
		return shows;
	}

	/**
	 * 停用渠道结算规则 。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channelPolicyRule.disable.log", vars = {
			"channelRule.group.policy.channel.name",
			"channelRule.group.policy.name", "channelRule.group.cinema.name",
			"channelRule.name" })
	public List<Show> disableChannelRule(ChannelRule channelRule) {
		// 如果渠道结算规则未生效，不允许停用。
		checkValidStatus(channelRule, "channelPolicyRule.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (channelRule.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置结算规则停用前先获取结算规则影响到的影院排期
			shows = showService.getMatchedShows(channelRule);
			// 停用结算规则时将结算规则影响到的排期全部置为失效。
			channelRule.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(channelRule);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 复制渠道结算规则。
	 * 
	 * @param channelRuleId
	 *            渠道结算规则ID
	 */
	@Transactional
	public void copyChannelRule(String channelRuleId) {
		ChannelRule origchannelRule = channelRuleDao.get(channelRuleId);
		// 如果结算策略处于待审核、待审批状态，不允许进行该操作。
		channelPolicyService.checkChannelPolicyApprove(origchannelRule
				.getGroup().getPolicy(), "channelPolicyRule.copy.approve");
		ChannelRule channelRule = ChannelRule.copy(origchannelRule,
				origchannelRule.getGroup());
		channelRuleDao.save(channelRule);
		// 将结算策略修改为“待提交”状态。
		origchannelRule.getGroup().getPolicy().setStatus(PolicyStatus.SUBMIT);
		origchannelRule.getGroup().getRules().add(channelRule);
		origchannelRule.getGroup().setValid(false);
	}

	/**
	 * 标记结算规则审核通过。
	 * 
	 * @param channelRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signAuditPass(String channelRuleId) {
		ChannelRule channelRule = getChannelRule(channelRuleId);
		if (channelRule.getGroup().getPolicy().getStatus() != PolicyStatus.AUDIT) {
			messageSource.thrown("channelPolicyRule.sign.audit.invalid");
		}
		channelRule.setStatus(RuleStatus.AUDITPASS);
	}

	/**
	 * 标记结算规则退回。
	 * 
	 * @param channelRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signAuditRefuse(String channelRuleId) {
		ChannelRule channelRule = getChannelRule(channelRuleId);
		if (channelRule.getGroup().getPolicy().getStatus() != PolicyStatus.AUDIT) {
			messageSource.thrown("channelPolicyRule.sign.audit.invalid");
		}
		channelRule.setStatus(RuleStatus.REFUSE);
	}

	/**
	 * 标记结算规则审批通过。
	 * 
	 * @param channelRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signApprovePass(String channelRuleId) {
		ChannelRule channelRule = getChannelRule(channelRuleId);
		if (channelRule.getGroup().getPolicy().getStatus() != PolicyStatus.APPROVE) {
			messageSource.thrown("channelPolicyRule.sign.approve.invalid");
		}
		channelRule.setStatus(RuleStatus.APPROVEPASS);
	}

	/**
	 * 标记结算规则审批退回。
	 * 
	 * @param channelRuleId
	 *            结算规则ID
	 */
	@Transactional
	public void signApproveRefuse(String channelRuleId) {
		ChannelRule channelRule = getChannelRule(channelRuleId);
		if (channelRule.getGroup().getPolicy().getStatus() != PolicyStatus.APPROVE) {
			messageSource.thrown("channelPolicyRule.sign.approve.invalid");
		}
		channelRule.setStatus(RuleStatus.REFUSE);
	}

	/**
	 * 检测渠道结算规则是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @param msgKey
	 *            提示语Key
	 */
	private void checkValidStatus(ChannelRule channelRule, String msgKey) {
		if (channelRule.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}