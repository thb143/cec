package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.SpecialPolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.SimpleLog;

/**
 * 特殊定价规则管理。
 */
@Service
public class SpecialRuleService {
	@Resource
	private Dao<SpecialRule> specialRuleDao;
	@Resource
	private Dao<SpecialChannel> specialChannelDao;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取指定ID的特殊定价规则。
	 * 
	 * @param ruleId
	 *            特殊定价规则ID
	 * @return 返回指定ID的特殊定价规则。
	 */
	@Transactional(readOnly = true)
	public SpecialRule getSpecialRule(String ruleId) {
		return specialRuleDao.get(ruleId);
	}

	/**
	 * 新增特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 */
	@Transactional
	@AutoFillIn
	public void createSpecialRule(SpecialRule rule) {
		SpecialPolicy policy = specialPolicyService.getSpecialPolicy(rule
				.getPolicy().getId());
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(policy,
				"specialRule.add.approve");
		rule.setOrdinal(policy.genNewRuleOrdinal());
		policy.getRules().add(rule);
		rule.setPolicy(policy);
		specialRuleDao.save(rule);
		// 将策略修改为“待提交”状态。
		policy.setStatus(SpecialPolicyStatus.SUBMIT);
	}

	/**
	 * 更新特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 */
	@Transactional
	public void updateSpecialRule(SpecialRule rule) {
		SpecialPolicy policy = specialPolicyService.getSpecialPolicy(rule
				.getPolicy().getId());
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(policy,
				"specialRule.edit.approve");
		SpecialRule origRule = getSpecialRule(rule.getId());
		if (!origRule.isEdit()) {
			messageSource.thrown("specialRule.edit.fail");
		}
		update(rule, origRule, policy);
		// 将策略修改为“待提交”状态。
		policy.setStatus(SpecialPolicyStatus.SUBMIT);
	}

	/**
	 * 更新特殊定价规则。
	 * 
	 * @param rule
	 *            新特殊定价规则
	 * @param origRule
	 *            旧特殊定价规则
	 * @param policy
	 *            特殊定价策略
	 */
	private void update(SpecialRule rule, SpecialRule origRule,
			SpecialPolicy policy) {
		// 如果编辑的规则是已生效的，则新增一条绑定关系的规则。如果编辑的规则是未生效的，则直接更新该规则。
		if (origRule.getValid() == ValidStatus.VALID) {
			// 如果规则已经被编辑过，不允许再次进行编辑。
			if (origRule.getBoundRule() != null) {
				messageSource.thrown("specialRule.edit.repeat");
			}
			rule.setId(null);
			rule.setValid(ValidStatus.UNVALID);
			rule.setEnabled(EnabledStatus.DISABLED);
			rule.setOrdinal(policy.genNewRuleOrdinal());
			rule.setBoundRule(origRule);
			rule.autoFillIn();
			policy.getRules().add(rule);
			rule.setPolicy(policy);
			specialRuleDao.save(rule);
			origRule.setBoundRule(rule);
			for (SpecialChannel origChannel : origRule.getChannels()) {
				// 复制原规则下“已生效”的渠道。
				if (origChannel.getValid() == ValidStatus.VALID) {
					copySpecialChannel(rule, origChannel);
				}
			}
		} else {
			BeanUtils.copyFields(rule, origRule,
					"ordinal,valid,enabled,channels");
		}
	}

	/**
	 * 复制特殊定价规则下的渠道。
	 * 
	 * @param rule
	 *            特殊定价规则
	 * @param origChannel
	 *            特殊定价规则下的旧渠道
	 */
	private void copySpecialChannel(SpecialRule rule, SpecialChannel origChannel) {
		SpecialChannel channel = new SpecialChannel();
		BeanUtils.copyFields(origChannel, channel, "id,rule");
		channel.setId(null);
		channel.setRule(rule);
		rule.getChannels().add(origChannel);
		specialChannelDao.save(channel);
	}

	/**
	 * 复制特殊定价规则。
	 * 
	 * @param ruleId
	 *            特殊定价规则ID
	 */
	@Transactional
	public void copySpecialRule(String ruleId) {
		SpecialRule origRule = getSpecialRule(ruleId);
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(origRule.getPolicy(),
				"specialRule.copy.approve");
		SpecialRule rule = SpecialRule.copy(origRule, origRule.getPolicy());
		specialRuleDao.save(rule);
		for (SpecialChannel channel : rule.getChannels()) {
			specialChannelDao.save(channel);
		}
		// 将策略修改为“待提交”状态。
		origRule.getPolicy().setStatus(SpecialPolicyStatus.SUBMIT);
	}

	/**
	 * 删除特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 */
	@Transactional
	@SimpleLog(code = "specialRule.delete.log", vars = { "rule.policy.name",
			"rule.name" })
	public void deleteSpecialRule(SpecialRule rule) {
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(rule.getPolicy(),
				"specialRule.delete.approve");
		// 如果规则"已生效",不允许删除。
		if (rule.getValid() == ValidStatus.VALID) {
			messageSource.thrown("specialRule.delete.valid");
		}
		// 如果结算特殊定价规则是副本则解除原本的关联，删除副本。
		if (rule.isCopy()) {
			SpecialRule boundRule = rule.getBoundRule();
			boundRule.setBoundRule(null);
			rule.getPolicy().getRules().remove(rule);
			specialRuleDao.remove(rule);
		} else {
			// 如果规则“未生效”，即没有产品关联，直接删除。
			rule.getPolicy().getRules().remove(rule);
			specialRuleDao.remove(rule);
		}
	}

	/**
	 * 上移特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 */
	@Transactional
	@SimpleLog(code = "specialRule.up.log", vars = { "rule.policy.name",
			"rule.name" })
	public void upSpecialRule(SpecialRule rule) {
		moveSpecialRule(rule, Order.asc("ordinal"));
	}

	/**
	 * 下移特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 */
	@Transactional
	@SimpleLog(code = "specialRule.down.log", vars = { "rule.policy.name",
			"rule.name" })
	public void downSpecialRule(SpecialRule rule) {
		moveSpecialRule(rule, Order.desc("ordinal"));
	}

	/**
	 * 启用特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "specialRule.enable.log", vars = { "rule.policy.name",
			"rule.name" })
	public List<Show> enableSpecialRule(SpecialRule rule) {
		// 如果特殊定价规则未生效，不允许启用。
		checkValidStatus(rule, "specialRule.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (rule.getEnabled() == EnabledStatus.DISABLED) {
			rule.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(rule);
		}
		return shows;
	}

	/**
	 * 停用特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 * @return 返回作废渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "specialRule.disable.log", vars = { "rule.policy.name",
			"rule.name" })
	public List<Show> disableSpecialRule(SpecialRule rule) {
		// 如果特殊定价规则未生效，不允许停用。
		checkValidStatus(rule, "specialRule.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (rule.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置结算规则停用前先获取结算规则影响到的影院排期
			shows = showService.getMatchedShows(rule);
			// 停用结算规则时将结算规则影响到的排期全部置为失效。
			rule.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(rule);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 移动特殊定价规则。
	 * 
	 * @param rule
	 *            特殊定价规则
	 * @param order
	 *            排序
	 */
	@SuppressWarnings("unchecked")
	private void moveSpecialRule(SpecialRule rule, Order order) {
		Criteria criteria = specialRuleDao.createCriteria();
		if (order.isAscending()) {
			criteria.add(Restrictions.gt("ordinal", rule.getOrdinal()));
		} else {
			criteria.add(Restrictions.lt("ordinal", rule.getOrdinal()));
		}
		criteria.add(Restrictions.eq("policy", rule.getPolicy()));
		criteria.add(Restrictions.in("valid", new Object[] {
				ValidStatus.UNVALID, ValidStatus.VALID }));
		criteria.addOrder(order);
		List<SpecialRule> rules = criteria.list();
		if (!rules.isEmpty()) {
			Integer currentOrdinal = rule.getOrdinal();
			SpecialRule switchRule = rules.get(0);
			rule.setOrdinal(switchRule.getOrdinal());
			switchRule.setOrdinal(currentOrdinal);
		}
	}

	/**
	 * 检测特殊定价规则是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param rule
	 *            特殊定价规则
	 * @param msgKey
	 *            提示语Key
	 */
	private void checkValidStatus(SpecialRule rule, String msgKey) {
		if (rule.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}