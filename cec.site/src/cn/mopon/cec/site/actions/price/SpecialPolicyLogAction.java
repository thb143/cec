package cn.mopon.cec.site.actions.price;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.model.SpecialPolicyLogSearchModel;
import cn.mopon.cec.core.service.SpecialPolicyLogService;
import cn.mopon.cec.core.service.SpecialPolicyService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 特殊定价策略审批管理。
 */
@Controller
@RequestMapping("/price")
public class SpecialPolicyLogAction {
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private SpecialPolicyLogService specialPolicyLogService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 弹出提交特殊定价策略页面。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @param model
	 *            数据模型
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-submit")
	public void submit(String policyId, Model model) {
		SpecialPolicyLog specialPolicyLog = new SpecialPolicyLog();
		specialPolicyLog.setPolicy(specialPolicyService
				.getSpecialPolicy(policyId));
		model.addAttribute("specialPolicyLog", specialPolicyLog);
	}

	/**
	 * 提交特殊定价策略。
	 * 
	 * @param policyLog
	 *            特殊定价策略审批记录
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-submit-save")
	public ModelAndView submitSave(SpecialPolicyLog policyLog) {
		specialPolicyLogService.submitSpecialPolicy(policyLog);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("policy.submit.success"));
	}

	/**
	 * 查看待审核的特殊定价策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            特殊定价策略审批记录ID
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("specialPolicy-audit-view")
	public void audit(Model model, String policyLogId) {
		SpecialPolicyLog specialPolicyLog = specialPolicyLogService
				.getSpecialPolicyLog(policyLogId);
		model.addAttribute("specialPolicyLog", specialPolicyLog);
	}

	/**
	 * 查看待审核特殊定价策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("specialPolicy-audit-list")
	public void listAudit(Model model, SpecialPolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.AUDIT);
		searchModel.setOrderBy("submitTime");
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("specialPolicyLogs",
				specialPolicyLogService.searchSpecialPolicyLog(searchModel));
	}

	/**
	 * 审核通过特殊定价策略。
	 * 
	 * @param policyLogId
	 *            特殊定价策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("specialPolicy-audit-pass")
	public ModelAndView auditPass(String policyLogId) {
		specialPolicyLogService.auditPassSpecialPolicy(policyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.audit.pass.success"));
	}

	/**
	 * 弹出审核退回页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            特殊定价策略审核记录ID
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("specialPolicy-audit-refuse")
	public void auditRefuse(Model model, String policyLogId) {
		SpecialPolicyLog specialPolicyLog = new SpecialPolicyLog();
		specialPolicyLog.setId(policyLogId);
		model.addAttribute("specialPolicyLog", specialPolicyLog);
	}

	/**
	 * 审核退回特殊定价策略。
	 * 
	 * @param specialPolicyLog
	 *            特殊定价策略审批记录
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_AUDIT")
	@RequestMapping("specialPolicy-audit-refuse-save")
	public ModelAndView auditRefuseSave(SpecialPolicyLog specialPolicyLog) {
		specialPolicyLogService.auditRefuseSpecialPolicy(specialPolicyLog);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("policy.audit.refuse.success"));
	}

	/**
	 * 查看待审批特殊场次的特殊定价策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            特殊定价策略审批记录ID
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("specialPolicy-approve-view")
	public void approve(Model model, String policyLogId) {
		SpecialPolicyLog specialPolicyLog = specialPolicyLogService
				.getSpecialPolicyLog(policyLogId);
		model.addAttribute("specialPolicyLog", specialPolicyLog);
	}

	/**
	 * 查看待审批特殊定价策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("specialPolicy-approve-list")
	public void listApprovePolicy(Model model,
			SpecialPolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.APPROVE);
		searchModel.setOrderBy("auditTime");
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("specialPolicyApproveLogs",
				specialPolicyLogService.searchSpecialPolicyLog(searchModel));
	}

	/**
	 * 审批通过特殊定价策略。
	 * 
	 * @param policyLogId
	 *            特殊定价策略审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("specialPolicy-approve-pass")
	public ModelAndView approvePass(String policyLogId) {
		specialPolicyLogService.approvePassSpecialPolicy(policyLogId);
		return NavTabResultUtils.reload(messageSource
				.get("policy.approve.pass.success"));
	}

	/**
	 * 弹出审批退回页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            特殊定价策略审批记录ID
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("specialPolicy-approve-refuse")
	public void approveRefuse(Model model, String policyLogId) {
		SpecialPolicyLog specialPolicyLog = new SpecialPolicyLog();
		specialPolicyLog.setId(policyLogId);
		model.addAttribute("specialPolicyLog", specialPolicyLog);
	}

	/**
	 * 审批退回特殊定价策略。
	 * 
	 * @param specialPolicyLog
	 *            特殊定价策略审批记录
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_APPROVE")
	@RequestMapping("specialPolicy-approve-refuse-save")
	public ModelAndView approveRefuseSave(SpecialPolicyLog specialPolicyLog) {
		specialPolicyLogService.approveRefuseSpecialPolicy(specialPolicyLog);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("policy.approve.refuse.success"));
	}

	/**
	 * 查看已退回特殊定价策略列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param selectedSpecialPolicyLogId
	 *            选中的特殊定价策略记录ID
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("specialPolicy-refuse-list")
	public void listRefuse(Model model, String selectedSpecialPolicyLogId,
			SpecialPolicyLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.REFUSED);
		searchModel.setOrderBy("approveTime");
		model.addAttribute("selectedSpecialPolicyLogId",
				selectedSpecialPolicyLogId);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("specialPolicyRefuseLogs",
				specialPolicyLogService.searchSpecialPolicyLog(searchModel));
	}

	/**
	 * 查看已退回的特殊定价策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyLogId
	 *            特殊定价策略审批记录ID
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("specialPolicy-refuse-view")
	public void refuseView(Model model, String policyLogId) {
		SpecialPolicyLog specialPolicyLog = specialPolicyLogService
				.getSpecialPolicyLog(policyLogId);
		model.addAttribute("specialPolicyLog", specialPolicyLog);
	}

	/**
	 * 查看指定的特殊定价策略审批记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            特殊定价策略ID
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("specialPolicyLog-view")
	public void view(Model model, String policyId) {
		model.addAttribute("specialPolicyLogs", specialPolicyService
				.getSpecialPolicy(policyId).getLogs());
	}
}