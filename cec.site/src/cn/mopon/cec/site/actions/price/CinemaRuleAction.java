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
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaPolicyService;
import cn.mopon.cec.core.service.CinemaRuleService;
import cn.mopon.cec.core.service.Circuit;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 影院结算规则管理。
 */
@Controller
@RequestMapping("/price")
public class CinemaRuleAction {
	@Resource
	private CinemaPolicyService cinemaPolicyService;
	@Resource
	private CinemaRuleService cinemaRuleService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private Circuit circuit;

	/**
	 * 新增影院结算规则。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            结算策略ID
	 * @param showType
	 *            放映类型
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-add")
	public void add(Model model, String policyId, String showType) {
		CinemaRule cinemaRule = new CinemaRule();
		cinemaRule.setShowType(IEnumUtils.getIEnumByValue(ShowType.class,
				showType));
		cinemaRule.setPolicy(cinemaPolicyService.getPolicy(policyId));
		model.addAttribute(cinemaRule);
	}

	/**
	 * 保存影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-save")
	public ModelAndView save(CinemaRule cinemaRule) {
		cinemaRuleService.createCinemaRule(cinemaRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.add.success"), "policyBox");
	}

	/**
	 * 编辑影院结算规则。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaRuleId
	 *            影院结算规则ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-edit")
	public void edit(Model model, String cinemaRuleId) {
		model.addAttribute(cinemaRuleService.getCinemaRule(cinemaRuleId));
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());

	}

	/**
	 * 更新影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-update")
	public ModelAndView update(CinemaRule cinemaRule) {
		cinemaRuleService.updateCinemaRule(cinemaRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.edit.success"), "policyBox");
	}

	/**
	 * 启用影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("cinemaRule-enable")
	public ModelAndView enable(CinemaRule cinemaRule) {
		List<Show> shows = cinemaRuleService.enableCinemaRule(cinemaRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.enable.success"), "policyBox");
	}

	/**
	 * 停用影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("cinemaRule-disable")
	public ModelAndView disable(CinemaRule cinemaRule) {
		List<Show> shows = cinemaRuleService.disableCinemaRule(cinemaRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.disable.success"), "policyBox");
	}

	/**
	 * 删除影院结算规则。
	 * 
	 * @param cinemaRuleId
	 *            影院结算规则ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-delete")
	public ModelAndView delete(String cinemaRuleId) {
		cinemaRuleService.deleteCinemaRule(cinemaRuleService
				.getCinemaRule(cinemaRuleId));
		return NavTabResultUtils.reloadDiv(
				messageSource.get("rule.delete.success"), "policyBox");
	}

	/**
	 * 打开复制影院结算规则页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaRule
	 *            影院结算规则
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-copy-edit")
	public void copyEdit(Model model, CinemaRule cinemaRule) {
		model.addAttribute("showTypes", cinemaRule.getPolicy().getCinema()
				.getTicketSettings().getShowTypes());
		model.addAttribute(cinemaRule);
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());
	}

	/**
	 * 复制影院结算规则。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaRule-copy")
	public ModelAndView copy(CinemaRule cinemaRule) {
		cinemaRuleService.createCinemaRule(cinemaRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("rule.copy.success"), "policyBox");
	}

	/**
	 * 标记结算规则审核通过。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("cinemaRule-sign-audit-pass")
	public ModelAndView auditPass(String ruleId) {
		cinemaRuleService.signAuditPass(ruleId);
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
	@RequestMapping("cinemaRule-sign-audit-refuse")
	public ModelAndView auditRefuse(String ruleId) {
		cinemaRuleService.signRefuse(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 标记结算规则审核通过。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("cinemaRule-sign-approve-pass")
	public ModelAndView approvePass(String ruleId) {
		cinemaRuleService.signApprovePass(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 标记结算规则审核退回。
	 * 
	 * @param ruleId
	 *            结算规则ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("cinemaRule-sign-approve-refuse")
	public ModelAndView approveRefuse(String ruleId) {
		cinemaRuleService.signApproveRefuse(ruleId);
		return NavTabResultUtils.close("");
	}

	/**
	 * 包装影院结算规则。
	 * 
	 * @param request
	 *            请求对象
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回包装后的影院结算规则。
	 */
	@ModelAttribute
	public CinemaRule wrapCinemaRule(HttpServletRequest request,
			CinemaRule cinemaRule) {
		String uri = request.getRequestURI();
		if (uri.endsWith("cinemaRule-save")
				|| uri.endsWith("cinemaRule-update")
				|| uri.endsWith("cinemaRule-copy")) {
			cinemaRule.setPeriodRule(PeriodRule.createPeriodRule(cinemaRule
					.getPeriodRule().getType()));
			SettleRuleType type = IEnumUtils.getIEnumByValue(
					SettleRuleType.class,
					request.getParameter("settleRule.type"));
			if (type == SettleRuleType.ROUND) {
				cinemaRule.setSettleRule(RoundSettleRule
						.wrapRoundSettleRule(request));
			} else {
				cinemaRule.setSettleRule(SettleRule.createSettleRule(cinemaRule
						.getSettleRule().getType()));
			}
		}
		return cinemaRule;
	}

	/**
	 * 选择区间定价结算方式。
	 * 
	 * @param model
	 *            数据模型
	 * @param name
	 *            参数名
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("settleRule-select")
	public void selectSettleRuleType(Model model, String name) {
		model.addAttribute("name", name);
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());
	}
}