package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.SimpleLog;

/**
 * 权益卡规则管理类。
 */
@Service
public class BenefitCardTypeRuleService {
	@Resource
	private Dao<BenefitCardTypeRule> benefitCardTypeRuleDao;
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private ShowService showService;

	/**
	 * 获取单个卡类规则
	 * 
	 * @param ruleId
	 *            卡类规则Id
	 * @return 卡类规则对象。
	 */
	@Transactional(readOnly = true)
	public BenefitCardTypeRule getBenefitCardTypeRule(String ruleId) {
		return benefitCardTypeRuleDao.get(ruleId);
	}

	/**
	 * 保存新增卡类规则
	 * 
	 * @param rule
	 *            卡类规则对象
	 */
	@Transactional
	@AutoFillIn
	public void createBenefitCardTypeRule(BenefitCardTypeRule rule) {
		benefitCardTypeRuleDao.save(rule);
		rule.getType().getRules().add(rule);
		if (!rule.getType().isBounded()) {
			rule.getType().setStatus(PolicyStatus.SUBMIT);
		}
	}

	/**
	 * 启用卡类规则
	 * 
	 * @param rule
	 *            卡类规则对象
	 * @return 返回受影响的排期列表。
	 */
	@SimpleLog(code = "benefitCardTypeRule.enabled.log", vars = {
			"rule.type.name", "rule.name" })
	@Transactional
	public List<Show> enabled(BenefitCardTypeRule rule) {
		// 如果规则未生效，则不允许启用。
		checkValidStatus(rule, "benefitCardTypeRule.enable.invalid");
		List<Show> shows = new ArrayList<>();
		if (rule.getEnabled() == EnabledStatus.DISABLED) {
			rule.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(rule);
		}
		return shows;
	}

	/**
	 * 停用卡类规则
	 * 
	 * @param rule
	 *            卡类规则对象
	 * @return 返回受影响的排期列表。
	 */
	@SimpleLog(code = "benefitCardTypeRule.disabled.log", vars = {
			"rule.type.name", "rule.name" })
	@Transactional
	public List<Show> disabled(BenefitCardTypeRule rule) {
		// 如果规则未生效，则不允许停用。
		checkValidStatus(rule, "benefitCardTypeRule.disable.invalid");
		List<Show> shows = new ArrayList<>();
		if (rule.getEnabled() == EnabledStatus.ENABLED) {
			shows = showService.getMatchedShows(rule);
			rule.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(rule);
			for (ChannelShow show : channelShows) {
				show.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 删除卡类规则
	 * 
	 * @param rule
	 *            卡类规则对象
	 */
	@SimpleLog(code = "benefitCardTypeRule.delete.log", vars = {
			"rule.type.name", "rule.name" })
	@Transactional
	public void delete(BenefitCardTypeRule rule) {
		if (rule.getValid() == ValidStatus.VALID) {
			messageSource.thrown("benefitCardTypeRule.delete.valid");
		}
		// 解除绑定关系。
		if (rule.isBounded()) {
			rule.getBoundRule().setBoundRule(null);
		}
		benefitCardTypeRuleDao.remove(rule);
		BenefitCardType type = rule.getType();
		type.getRules().remove(rule);
	}

	/**
	 * 更新权益卡规则。
	 * 
	 * @param rule
	 *            权益卡规则
	 */
	@Transactional
	@AutoFillIn
	public void update(BenefitCardTypeRule rule) {
		BenefitCardType type = benefitCardTypeService.getBenefitCardType(rule
				.getType().getId());
		// 判断是否可以编辑
		if (type.getStatus() == PolicyStatus.AUDIT
				|| type.getStatus() == PolicyStatus.APPROVE) {
			messageSource.thrown("benefitCardTypeRule.edit.approve");
		}
		BenefitCardTypeRule origRule = getBenefitCardTypeRule(rule.getId());
		update(rule, origRule, type);
		// 将卡类修改为“待提交”状态。
		if (!type.isBounded()) {
			type.setStatus(PolicyStatus.SUBMIT);
		}
	}

	/**
	 * 更新卡类优惠规则。
	 * 
	 * @param rule
	 *            新规则
	 * @param origRule
	 *            旧规则
	 * @param type
	 *            卡类
	 */
	private void update(BenefitCardTypeRule rule, BenefitCardTypeRule origRule,
			BenefitCardType type) {
		if (origRule.getValid() == ValidStatus.VALID) {
			// 如果规则已经被编辑过，不允许再次进行编辑。
			if (origRule.getBoundRule() != null) {
				messageSource.thrown("benefitCardTypeRule.edit.repeat");
			}
			rule.setId(null);
			rule.setValid(ValidStatus.UNVALID);
			rule.setEnabled(EnabledStatus.DISABLED);
			rule.setOrdinal(type.genNewRuleOrdinal());
			rule.setBoundRule(origRule);
			rule.autoFillIn();
			type.getRules().add(rule);
			rule.setType(type);
			benefitCardTypeRuleDao.save(rule);
			origRule.setBoundRule(rule);
		} else {
			BeanUtils.copyFields(rule, origRule,
					"valid,enabled,settles,ordinal");
		}
	}

	/**
	 * 检测权益卡定价规则是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param rule
	 *            特殊定价规则
	 * @param msgKey
	 *            提示语Key
	 */
	private void checkValidStatus(BenefitCardTypeRule rule, String msgKey) {
		if (rule.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}
