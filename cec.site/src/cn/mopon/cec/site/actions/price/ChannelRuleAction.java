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
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.service.ChannelRuleGroupService;
import cn.mopon.cec.core.service.ChannelRuleService;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.Circuit;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 结算策略规则管理。
 */
@Controller
@RequestMapping("/price")
public class ChannelRuleAction {
	@Resource
	private ChannelRuleService channelRuleService;
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private Circuit circuit;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增渠道结算规则。
	 * 
	 * @param model
	 *            数据模型
	 * @param groupId
	 *            规则分组ID
	 * @param showType
	 *            放映类型
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-add")
	public void add(Model model, String groupId, String showType) {
		ChannelRule channelRule = new ChannelRule();
		channelRule.setShowType(IEnumUtils.getIEnumByValue(ShowType.class,
				showType));
		channelRule.setGroup(channelRuleGroupService
				.getChannelRuleGroup(groupId));
		model.addAttribute(channelRule);
	}

	/**
	 * 保存渠道结算规则。
	 * 
	 * @param channelRule
	 *            策略规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-save")
	public ModelAndView save(ChannelRule channelRule) {
		channelRuleService.createChannelRule(channelRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.add.success"), "channelPolicyBox");
	}

	/**
	 * 编辑渠道结算规则。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelRuleId
	 *            规则ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-edit")
	public void edit(Model model, String channelRuleId) {
		model.addAttribute(channelRuleService.getChannelRule(channelRuleId));
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());
	}

	/**
	 * 更新渠道结算规则。
	 * 
	 * @param channelRule
	 *            策略规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-update")
	public ModelAndView update(ChannelRule channelRule) {
		channelRuleService.updateChannelRule(channelRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.edit.success"), "channelPolicyBox");
	}

	/**
	 * 启用渠道结算规则。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelRule-enable")
	public ModelAndView enable(ChannelRule channelRule) {
		List<Show> shows = channelRuleService.enableChannelRule(channelRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.enable.success"), "channelPolicyBox");
	}

	/**
	 * 停用渠道结算规则。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelRule-disable")
	public ModelAndView disable(ChannelRule channelRule) {
		List<Show> shows = channelRuleService.disableChannelRule(channelRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.disable.success"), "channelPolicyBox");
	}

	/**
	 * 删除渠道结算规则。
	 * 
	 * @param channelRuleId
	 *            规则ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-delete")
	public ModelAndView delete(String channelRuleId) {
		channelRuleService.deleteChannelRule(channelRuleService
				.getChannelRule(channelRuleId));
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.delete.success"), "channelPolicyBox");
	}

	/**
	 * 打开复制渠道结算规则页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelRule
	 *            渠道结算规则
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-copy-edit")
	public void copyEdit(Model model, ChannelRule channelRule) {
		model.addAttribute("showTypes", channelRule.getGroup().getCinema()
				.getTicketSettings().getShowTypes());
		model.addAttribute(channelRule);
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());
	}

	/**
	 * 复制渠道结算规则。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRule-copy")
	public ModelAndView copy(ChannelRule channelRule) {
		channelRuleService.createChannelRule(channelRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.copy.success"), "channelPolicyBox");
	}

	/**
	 * 标记结算规则审核通过。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelRule-sign-audit-pass")
	public ModelAndView auditPass(String ruleId) {
		channelRuleService.signAuditPass(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 标记结算规则审核退回。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelRule-sign-audit-refuse")
	public ModelAndView auditRefuse(String ruleId) {
		channelRuleService.signAuditRefuse(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 标记结算规则审批通过。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelRule-sign-approve-pass")
	public ModelAndView approvePass(String ruleId) {
		channelRuleService.signApprovePass(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 标记结算规则审批退回。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelRule-sign-approve-refuse")
	public ModelAndView approveRefuse(String ruleId) {
		channelRuleService.signApproveRefuse(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 包装渠道结算规则。
	 * 
	 * @param request
	 *            请求对象
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回包装后的渠道结算规则。
	 */
	@ModelAttribute
	public ChannelRule wrapChannelRule(HttpServletRequest request,
			ChannelRule channelRule) {
		String uri = request.getRequestURI();
		if (uri.endsWith("channelRule-save")
				|| uri.endsWith("channelRule-update")
				|| uri.endsWith("channelRule-copy")) {
			channelRule.setPeriodRule(PeriodRule.createPeriodRule(channelRule
					.getPeriodRule().getType()));
			SettleRuleType type = IEnumUtils.getIEnumByValue(
					SettleRuleType.class,
					request.getParameter("settleRule.type"));
			if (type == SettleRuleType.ROUND) {
				channelRule.setSettleRule(RoundSettleRule
						.wrapRoundSettleRule(request));
			} else {
				channelRule
						.setSettleRule(SettleRule.createSettleRule(channelRule
								.getSettleRule().getType()));
			}
		}
		return channelRule;
	}
}