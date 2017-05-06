package cn.mopon.cec.site.actions.price;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.model.PolicyLogSearchModel;
import cn.mopon.cec.core.service.CinemaPolicyLogService;
import cn.mopon.cec.core.service.CinemaPolicyService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 结算策略审批记录管理。
 */
@Controller
@RequestMapping("/price")
public class CinemaPolicyLogAction {
	@Resource
	private CinemaPolicyService cinemaPolicyService;
	@Resource
	private CinemaPolicyLogService cinemaPolicyLogService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 提交结算策略。
	 * 
	 * @param policyId
	 *            结算策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-submit")
	public ModelAndView submit(String policyId) {
		CinemaPolicyLog cinemaPolicyLog = new CinemaPolicyLog();
		cinemaPolicyLog.setPolicy(cinemaPolicyService.getPolicy(policyId));
		cinemaPolicyLogService.submitPolicy(cinemaPolicyLog);
		return NavTabResultUtils.reload(messageSource
				.get("policy.submit.success"));
	}

	/**
	 * 查看待审核结算策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("cinemaPolicy-audit-list")
	public void listAuditPolicy(Model model, PolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.AUDIT);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaPolicyLogPage",
				cinemaPolicyLogService.searchPolicyLog(searchModel));
	}

	/**
	 * 查看待审核的结算策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @param status
	 *            规则状态
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("cinemaPolicy-audit-view")
	public void audit(Model model, String policyLogId, String status) {
		CinemaPolicyLog policyLog = cinemaPolicyLogService
				.getPolicyLog(policyLogId);
		RuleStatus ruleStatus = IEnumUtils.getIEnumByValue(RuleStatus.class,
				status);
		model.addAttribute("cinemaRuleList", cinemaPolicyLogService
				.searchCinemaRule(policyLog.getPolicy(), ruleStatus));
		PolicyLogSearchModel policyLogSearchModel = new PolicyLogSearchModel();
		policyLogSearchModel.setCinemaRuleStatus(ruleStatus);
		model.addAttribute("cinemaRuleModel", policyLogSearchModel);
		model.addAttribute("policyLog", policyLog);
	}

	/**
	 * 审核通过结算策略。
	 * 
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("cinemaPolicy-audit-pass")
	public ModelAndView auditPass(String policyLogId) {
		cinemaPolicyLogService.auditPassPolicy(policyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.audit.pass.success"));
	}

	/**
	 * 审核退回结算策略。
	 * 
	 * @param policyLogId
	 *            定价结算策略记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("cinemaPolicy-audit-refuse")
	public ModelAndView auditRefuse(String policyLogId) {
		CinemaPolicyLog policyLog = cinemaPolicyLogService
				.getPolicyLog(policyLogId);
		cinemaPolicyLogService.auditRefusePolicy(policyLog);
		return NavTabResultUtils.reload(messageSource
				.get("policy.audit.refuse.success"));
	}

	/**
	 * 查看待审批结算策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("cinemaPolicy-approve-list")
	public void listApprovePolicy(Model model, PolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.APPROVE);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaPolicyLogPage",
				cinemaPolicyLogService.searchPolicyLog(searchModel));
	}

	/**
	 * 查看待审批的结算策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @param status
	 *            规则状态
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("cinemaPolicy-approve-view")
	public void approve(Model model, String policyLogId, String status) {
		CinemaPolicyLog policyLog = cinemaPolicyLogService
				.getPolicyLog(policyLogId);
		RuleStatus ruleStatus = IEnumUtils.getIEnumByValue(RuleStatus.class,
				status);
		model.addAttribute("cinemaRuleList", cinemaPolicyLogService
				.searchCinemaRule(policyLog.getPolicy(), ruleStatus));
		PolicyLogSearchModel policyLogSearchModel = new PolicyLogSearchModel();
		policyLogSearchModel.setCinemaRuleStatus(ruleStatus);
		model.addAttribute("cinemaRuleModel", policyLogSearchModel);
		model.addAttribute("policyLog", policyLog);
	}

	/**
	 * 审批通过结算策略。
	 * 
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("cinemaPolicy-approve-pass")
	public ModelAndView approvePass(String policyLogId) {
		cinemaPolicyLogService.approvePassPolicy(policyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.approve.pass.success"));
	}

	/**
	 * 审批退回结算策略。
	 * 
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("cinemaPolicy-approve-refuse")
	public ModelAndView approveRefuse(String policyLogId) {
		CinemaPolicyLog policyLog = cinemaPolicyLogService
				.getPolicyLog(policyLogId);
		cinemaPolicyLogService.approveRefusePolicy(policyLog);
		return NavTabResultUtils.reload(messageSource
				.get("policy.approve.refuse.success"));
	}

	/**
	 * 查看已退回结算策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param selectedPolicyLogId
	 *            选中的结算策略记录ID
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("cinemaPolicy-refuse-list")
	public void listRefusePolicy(Model model, String selectedPolicyLogId,
			PolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.REFUSED);
		model.addAttribute("selectedPolicyLogId", selectedPolicyLogId);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaPolicyLogPage",
				cinemaPolicyLogService.searchPolicyLog(searchModel));
	}

	/**
	 * 查看已退回结算策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            结算策略审批记录ID
	 * @param status
	 *            规则状态
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("cinemaPolicy-refuse-view")
	public void refuseView(Model model, String policyLogId, String status) {
		CinemaPolicyLog policyLog = cinemaPolicyLogService
				.getPolicyLog(policyLogId);
		RuleStatus ruleStatus = IEnumUtils.getIEnumByValue(RuleStatus.class,
				status);
		model.addAttribute("cinemaRuleList", cinemaPolicyLogService
				.searchCinemaRule(policyLog.getPolicy(), ruleStatus));
		PolicyLogSearchModel policyLogSearchModel = new PolicyLogSearchModel();
		policyLogSearchModel.setCinemaRuleStatus(ruleStatus);
		model.addAttribute("cinemaRuleModel", policyLogSearchModel);
		model.addAttribute("policyLog", policyLog);
	}

	/**
	 * 查看指定的结算策略审批记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            结算策略ID
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("cinemaPolicyLog-view")
	public void view(Model model, String policyId) {
		model.addAttribute("policyLogs", cinemaPolicyService
				.getPolicy(policyId).getLogs());
	}
}