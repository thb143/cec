package cn.mopon.cec.site.actions.price;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import cn.mopon.cec.core.assist.period.PeriodRule;
import cn.mopon.cec.core.assist.settle.RoundSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.Circuit;
import cn.mopon.cec.core.service.SpecialPolicyService;
import cn.mopon.cec.core.service.SpecialRuleService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 特殊定价规则管理。
 */
@Controller
@RequestMapping("/price")
public class SpecialRuleAction {
	@Resource
	private SpecialRuleService specialRuleService;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private Circuit circuit;
	@Resource
	private MessageSource messageSource;

	/**
	 * 添加特殊定价规则。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            特殊定价策略ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialRule-add")
	public void add(Model model, String policyId) {
		SpecialRule specialRule = new SpecialRule();
		SpecialPolicy specialPolicy = specialPolicyService
				.getSpecialPolicy(policyId);
		specialRule.setPolicy(specialPolicy);
		model.addAttribute("specialRule", specialRule);
	}

	/**
	 * 保存特殊定价规则。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialRule-save")
	public ModelAndView save(SpecialRule specialRule) {
		specialRuleService.createSpecialRule(specialRule);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("rule.add.success"),
				"/price/specialPolicy-list?selectedSpecialPolicyId="
						+ specialRule.getPolicy().getId());
	}

	/**
	 * 查看特殊定价规则。
	 * 
	 * @param model
	 *            数据模型。
	 * @param policyId
	 *            特殊定价策略ID。
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("specialRule-view")
	public void view(Model model, String policyId) {
		SpecialPolicy specialPolicy = specialPolicyService
				.getSpecialPolicy(policyId);
		model.addAttribute("specialPolicy", specialPolicy);
	}

	/**
	 * 修改特殊定价规则。
	 * 
	 * @param model
	 *            数据模型
	 * @param specialRuleId
	 *            特殊定价规则ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialRule-edit")
	public void edit(Model model, String specialRuleId) {
		SpecialRule specialRule = specialRuleService
				.getSpecialRule(specialRuleId);
		specialRule.setPolicy(specialPolicyService.getSpecialPolicy(specialRule
				.getPolicy().getId()));
		model.addAttribute("hallList", specialRule.getHallModels());
		model.addAttribute("specialRule", specialRule);
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());
	}

	/**
	 * 更新特殊定价规则。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialRule-update")
	public ModelAndView update(SpecialRule specialRule) {
		specialRuleService.updateSpecialRule(specialRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.edit.success"), "specialPolicyBox");
	}

	/**
	 * 复制特殊定价规则。
	 * 
	 * @param specialRuleId
	 *            特殊定价规则ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialRule-copy")
	public ModelAndView copy(String specialRuleId) {
		specialRuleService.copySpecialRule(specialRuleId);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.copy.success"), "specialPolicyBox");
	}

	/**
	 * 启用特殊定价规则。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialRule-enable")
	public ModelAndView enable(SpecialRule specialRule) {
		List<Show> shows = specialRuleService.enableSpecialRule(specialRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.enable.success"), "specialPolicyBox");
	}

	/**
	 * 停用特殊定价规则。
	 * 
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialRule-disable")
	public ModelAndView disable(SpecialRule specialRule) {
		List<Show> shows = specialRuleService.disableSpecialRule(specialRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.disable.success"), "specialPolicyBox");
	}

	/**
	 * 删除特殊定价规则。
	 * 
	 * @param specialRuleId
	 *            特殊定价规则ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialRule-delete")
	public ModelAndView delete(String specialRuleId) {
		SpecialRule specialRule = specialRuleService
				.getSpecialRule(specialRuleId);
		specialRuleService.deleteSpecialRule(specialRule);
		return DialogResultUtils.forward(
				messageSource.get("rule.delete.success"),
				"/price/specialPolicy-list?selectedSpecialPolicyId="
						+ specialRule.getPolicy().getId());
	}

	/**
	 * 包装特殊定价规则。
	 * 
	 * @param request
	 *            请求对象
	 * @param specialRule
	 *            特殊定价规则
	 * @return 返回包装后的特殊定价规则。
	 */
	@ModelAttribute
	public SpecialRule wrapSpecialRule(HttpServletRequest request,
			SpecialRule specialRule) {
		String uri = request.getRequestURI();
		if (uri.endsWith("specialRule-save")
				|| uri.endsWith("specialRule-update")) {
			specialRule.setPeriodRule(PeriodRule.createPeriodRule(specialRule
					.getPeriodRule().getType()));
			SettleRuleType type = IEnumUtils.getIEnumByValue(
					SettleRuleType.class,
					request.getParameter("settleRule.type"));
			if (type == SettleRuleType.ROUND) {
				specialRule.setSettleRule(RoundSettleRule
						.wrapRoundSettleRule(request));
			} else {
				specialRule
						.setSettleRule(SettleRule.createSettleRule(specialRule
								.getSettleRule().getType()));
			}
		}
		return specialRule;
	}
}