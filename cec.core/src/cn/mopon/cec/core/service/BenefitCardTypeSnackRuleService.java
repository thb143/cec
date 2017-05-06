package cn.mopon.cec.core.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
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
public class BenefitCardTypeSnackRuleService {
	@Resource
	private Dao<BenefitCardTypeSnackRule> benefitCardTypeSnackRuleDao;
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取单个卡类规则
	 * 
	 * @param ruleId
	 *            卡类规则Id
	 * @return 卡类规则对象。
	 */
	@Transactional(readOnly = true)
	public BenefitCardTypeSnackRule getBenefitCardTypeSnackRule(String ruleId) {
		return benefitCardTypeSnackRuleDao.get(ruleId);
	}

	/**
	 * 保存新增卡类规则
	 * 
	 * @param rule
	 *            卡类规则对象
	 */
	@Transactional
	@AutoFillIn
	public void createBenefitCardTypeSnackRule(BenefitCardTypeSnackRule rule) {
		benefitCardTypeSnackRuleDao.save(rule);
		rule.getType().getSnackRules().add(rule);
		if (!rule.getType().isBounded()) {
			rule.getType().setStatus(PolicyStatus.SUBMIT);
		}
	}

	/**
	 * 启用卡类规则
	 * 
	 * @param rule
	 *            卡类卖规则对象
	 * @return 返回受影响的排期列表。
	 */
	@SimpleLog(code = "benefitCardTypeRule.enabled.log", vars = {
			"rule.type.name", "rule.name" })
	@Transactional
	public void enabled(BenefitCardTypeSnackRule rule) {
		// 如果规则未生效，则不允许启用。
		checkValidStatus(rule, "benefitCardTypeRule.enable.invalid");
		if (rule.getEnabled() == EnabledStatus.DISABLED) {
			rule.setEnabled(EnabledStatus.ENABLED);
		}
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
	public void disabled(BenefitCardTypeSnackRule rule) {
		// 如果规则未生效，则不允许停用。
		checkValidStatus(rule, "benefitCardTypeRule.disable.invalid");
		if (rule.getEnabled() == EnabledStatus.ENABLED) {
			rule.setEnabled(EnabledStatus.DISABLED);
		}
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
	public void delete(BenefitCardTypeSnackRule rule) {
		if (rule.getValid() == ValidStatus.VALID) {
			messageSource.thrown("benefitCardTypeRule.delete.valid");
		}
		// 解除绑定关系。
		if (rule.isBounded()) {
			rule.getBoundRule().setBoundRule(null);
		}
		benefitCardTypeSnackRuleDao.remove(rule);
		BenefitCardType type = rule.getType();
		type.getSnackRules().remove(rule);
	}

	/**
	 * 更新权益卡规则。
	 * 
	 * @param rule
	 *            权益卡规则
	 */
	@Transactional
	@AutoFillIn
	public void update(BenefitCardTypeSnackRule rule) {
		BenefitCardType type = benefitCardTypeService.getBenefitCardType(rule
				.getType().getId());
		// 判断是否可以编辑
		if (type.getStatus() == PolicyStatus.AUDIT
				|| type.getStatus() == PolicyStatus.APPROVE) {
			messageSource.thrown("benefitCardTypeRule.edit.approve");
		}
		BenefitCardTypeSnackRule origRule = getBenefitCardTypeSnackRule(rule
				.getId());
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
	private void update(BenefitCardTypeSnackRule rule,
			BenefitCardTypeSnackRule origRule, BenefitCardType type) {
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
			type.getSnackRules().add(rule);
			rule.setType(type);
			benefitCardTypeSnackRuleDao.save(rule);
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
	private void checkValidStatus(BenefitCardTypeSnackRule rule, String msgKey) {
		if (rule.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}