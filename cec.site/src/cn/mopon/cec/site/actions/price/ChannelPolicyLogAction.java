package cn.mopon.cec.site.actions.price;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.ChannelPolicyLog;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.model.ChannelPolicyLogSearchModel;
import cn.mopon.cec.core.service.ChannelPolicyLogService;
import cn.mopon.cec.core.service.ChannelPolicyService;
import cn.mopon.cec.core.service.ChannelRuleGroupService;
import cn.mopon.cec.core.service.CinemaService;
import coo.base.model.Page;
import coo.base.util.StringUtils;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 策略审批记录管理。
 */
@Controller
@RequestMapping("/price")
public class ChannelPolicyLogAction {
	@Resource
	private ChannelPolicyService channelPolicyService;
	@Resource
	private ChannelPolicyLogService channelPolicyLogService;
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 提交策略。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-submit")
	public ModelAndView submit(String channelPolicyId) {
		channelPolicyLogService.submitPolicy(channelPolicyId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.submit.success"));
	}

	/**
	 * 查看待审核策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelPolicy-audit-list")
	public void listAuditPolicy(Model model,
			ChannelPolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.AUDIT);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute(channelPolicyLogService.searchPolicyLog(searchModel));
	}

	/**
	 * 查看待审核策略中分组影院列表。
	 * 
	 * @param channelPolicyLogId
	 *            渠道策略审批ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            检索条件
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelPolicy-audit-cinema-list")
	public void listAuditPolicyCinema(String channelPolicyLogId, Model model,
			SearchModel searchModel) {
		listPolicyCinema(channelPolicyLogId, model, searchModel);
	}

	/**
	 * 查看待审核的策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @param cinemaId
	 *            影院ID
	 * @param ruleStatus
	 *            规则状态
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelPolicy-audit-view")
	public void audit(Model model, String channelPolicyLogId, String cinemaId,
			String ruleStatus) {
		ChannelPolicyLog policyLog = channelPolicyLogService
				.getGroupChannelPolicyLog(channelPolicyLogId, cinemaId);
		RuleStatus status = IEnumUtils.getIEnumByValue(RuleStatus.class,
				ruleStatus);
		policyLog.getPolicy().setRuleStatus(status);
		model.addAttribute(policyLog);
		model.addAttribute("cinemaId", cinemaId);
	}

	/**
	 * 审核通过策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelPolicy-audit-pass")
	public ModelAndView auditPass(String channelPolicyLogId) {
		channelPolicyLogService.auditPassPolicy(channelPolicyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.audit.pass.success"));
	}

	/**
	 * 审核退回策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("channelPolicy-audit-refuse")
	public ModelAndView auditRefuse(String channelPolicyLogId) {
		channelPolicyLogService.auditRefusePolicy(channelPolicyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.audit.refuse.success"));
	}

	/**
	 * 查看待审批策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelPolicy-approve-list")
	public void listApprovePolicy(Model model,
			ChannelPolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.APPROVE);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute(channelPolicyLogService.searchPolicyLog(searchModel));
	}

	/**
	 * 查看待审批策略中分组影院列表。
	 * 
	 * @param channelPolicyLogId
	 *            渠道策略审批ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            检索条件
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelPolicy-approve-cinema-list")
	public void listApprovePolicyCinema(String channelPolicyLogId, Model model,
			SearchModel searchModel) {
		listPolicyCinema(channelPolicyLogId, model, searchModel);
	}

	/**
	 * 查看待审批的策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @param cinemaId
	 *            影院ID
	 * @param ruleStatus
	 *            规则状态
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelPolicy-approve-view")
	public void approve(Model model, String channelPolicyLogId,
			String cinemaId, String ruleStatus) {
		ChannelPolicyLog channelPolicyLog = channelPolicyLogService
				.getGroupChannelPolicyLog(channelPolicyLogId, cinemaId);
		RuleStatus status = IEnumUtils.getIEnumByValue(RuleStatus.class,
				ruleStatus);
		channelPolicyLog.getPolicy().setRuleStatus(status);
		model.addAttribute(channelPolicyLog);
		model.addAttribute("cinemaId", cinemaId);
	}

	/**
	 * 审批通过策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelPolicy-approve-pass")
	public ModelAndView approvePass(String channelPolicyLogId) {
		channelPolicyLogService.approvePassPolicy(channelPolicyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.approve.pass.success"));
	}

	/**
	 * 审批退回策略。
	 * 
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("channelPolicy-approve-refuse")
	public ModelAndView approveRefuse(String channelPolicyLogId) {
		channelPolicyLogService.approveRefusePolicy(channelPolicyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.approve.refuse.success"));
	}

	/**
	 * 查看已退回策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param selectedPolicyLogId
	 *            选中的策略记录ID
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicy-refuse-list")
	public void listRefusePolicy(Model model, String selectedPolicyLogId,
			ChannelPolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.REFUSED);
		model.addAttribute("selectedPolicyLogId", selectedPolicyLogId);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute(channelPolicyLogService.searchPolicyLog(searchModel));
	}

	/**
	 * 查看已退回策略中分组影院列表。
	 * 
	 * @param channelPolicyLogId
	 *            渠道策略审批ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            检索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicy-refuse-cinema-list")
	public void listRefusePolicyCinema(String channelPolicyLogId, Model model,
			SearchModel searchModel) {
		listPolicyCinema(channelPolicyLogId, model, searchModel);
	}

	/**
	 * 查看已退回策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyLogId
	 *            策略审批记录ID
	 * @param cinemaId
	 *            影院ID
	 * @param ruleStatus
	 *            规则状态
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicy-refuse-view")
	public void refuseView(Model model, String channelPolicyLogId,
			String cinemaId, String ruleStatus) {
		ChannelPolicyLog channelPolicyLog = channelPolicyLogService
				.getGroupChannelPolicyLog(channelPolicyLogId, cinemaId);
		if (StringUtils.isNotEmpty(ruleStatus)) {
			RuleStatus status = IEnumUtils.getIEnumByValue(RuleStatus.class,
					ruleStatus);
			channelPolicyLog.getPolicy().setRuleStatus(status);
		}
		model.addAttribute(channelPolicyLog);
		model.addAttribute("cinemaId", cinemaId);
	}

	/**
	 * 查看策略中分组影院列表。
	 * 
	 * @param channelPolicyLogId
	 *            渠道策略审批ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            检索条件
	 */
	private void listPolicyCinema(String channelPolicyLogId, Model model,
			SearchModel searchModel) {
		searchModel.setPageSize(25);
		ChannelPolicyLog channelPolicyLog = channelPolicyLogService
				.getChannelPolicyLog(channelPolicyLogId);
		Page<ChannelRuleGroup> groups = channelRuleGroupService
				.searchChannelRuleGroup(channelPolicyLog.getPolicy().getId(),
						searchModel, false);
		model.addAttribute("channelPolicyLog", channelPolicyLog);
		model.addAttribute("groupPage", groups);
		model.addAttribute("searchModel", searchModel);
	}

	/**
	 * 查看指定的渠道结算策略审批记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            结算策略ID
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicyLog-view")
	public void view(Model model, String channelPolicyId) {
		model.addAttribute("channelPolicyLogs", channelPolicyService
				.getChannelPolicy(channelPolicyId).getLogs());
	}
}