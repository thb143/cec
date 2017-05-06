package cn.mopon.cec.site.actions.product;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeLog;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.model.CardTypeLogSearchModel;
import cn.mopon.cec.core.service.BenefitCardTypeLogService;
import cn.mopon.cec.core.service.BenefitCardTypeService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 权益卡卡类审批管理。
 */
@Controller
@RequestMapping("/product")
public class BenefitCardTypeLogAction {
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private BenefitCardTypeLogService benefitCardTypeLogService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 提交权益卡卡类。
	 * 
	 * @param typeId
	 *            权益卡ID
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-submit")
	public ModelAndView submit(String typeId) {
		BenefitCardTypeLog cardTypeLog = new BenefitCardTypeLog();
		cardTypeLog.setType(benefitCardTypeService.getBenefitCardType(typeId));
		benefitCardTypeLogService.submitCardType(cardTypeLog);
		return NavTabResultUtils.reload(messageSource
				.get("benefitCardType.submit.success"));
	}

	/**
	 * 查看待审核权益卡列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("BENEFITCARDTYPE_AUDIT")
	@RequestMapping("benefitCardType-audit-list")
	public void listAuditCardType(Model model,
			CardTypeLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.AUDIT);
		searchModel.setOrderBy("createDate");
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cardTypeLogs",
				benefitCardTypeLogService.searchBenefitCardTypeLog(searchModel));
	}

	/**
	 * 查看待审核的权益卡。
	 * 
	 * @param model
	 *            数据模型
	 * @param cardTypeLogId
	 *            审核的权益卡审批ID
	 */
	@Auth("BENEFITCARDTYPE_AUDIT")
	@RequestMapping("benefitCardType-audit-view")
	public void audit(Model model, String cardTypeLogId) {
		BenefitCardTypeLog cardTypeLog = benefitCardTypeLogService
				.getBenefitCardTypeLog(cardTypeLogId);
		model.addAttribute("cardTypeLog", cardTypeLog);
	}

	/**
	 * 审核通过权益卡。
	 * 
	 * @param cardTypeLogId
	 *            权益卡审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("BENEFITCARDTYPE_AUDIT")
	@RequestMapping("benefitCardType-audit-pass")
	public ModelAndView auditPass(String cardTypeLogId) {
		benefitCardTypeLogService.auditPassBenefitCardType(cardTypeLogId);
		return NavTabResultUtils.reload(messageSource
				.get("benefitCardType.audit.pass.success"));
	}

	/**
	 * 弹出审核退回权益卡页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param cardTypeLogId
	 *            卡类审批记录ID
	 */
	@Auth("BENEFITCARDTYPE_AUDIT")
	@RequestMapping("benefitCardType-audit-refuse")
	public void auditRefuse(Model model, String cardTypeLogId) {
		BenefitCardTypeLog cardTypeLog = new BenefitCardTypeLog();
		cardTypeLog.setId(cardTypeLogId);
		model.addAttribute("cardTypeLog", cardTypeLog);
	}

	/**
	 * 审核退回权益卡。
	 * 
	 * @param cardTypeLogId
	 *            权益卡审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("BENEFITCARDTYPE_AUDIT")
	@RequestMapping("benefitCardType-audit-refuse-save")
	public ModelAndView auditRefuseSave(BenefitCardTypeLog cardTypeLog) {
		benefitCardTypeLogService.auditRefuseBenefitCardType(cardTypeLog);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("benefitCardType.audit.refuse.success"));
	}

	/**
	 * 查看待审批权益卡列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("BENEFITCARDTYPE_APPROVE")
	@RequestMapping("benefitCardType-approve-list")
	public void listApproveCardType(Model model,
			CardTypeLogSearchModel searchModel) {
		searchModel.setStatus(AuditStatus.APPROVE);
		searchModel.setOrderBy("auditTime");
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cardTypeLogs",
				benefitCardTypeLogService.searchBenefitCardTypeLog(searchModel));
	}

	/**
	 * 查看待审批的权益卡。
	 * 
	 * @param model
	 *            数据模型
	 * @param cardTypeLogId
	 *            权益卡审批记录ID
	 */
	@Auth("BENEFITCARDTYPE_APPROVE")
	@RequestMapping("benefitCardType-approve-view")
	public void approve(Model model, String cardTypeLogId) {
		BenefitCardTypeLog cardTypeLog = benefitCardTypeLogService
				.getBenefitCardTypeLog(cardTypeLogId);
		model.addAttribute("cardTypeLog", cardTypeLog);
	}

	/**
	 * 弹出审核退回权益卡页面。
	 * 
	 * @param model
	 *            数据模型。
	 * @param cardTypeLogId
	 *            权益卡审批记录ID
	 */
	@RequestMapping("benefitCardType-approve-refuse")
	@Auth("BENEFITCARDTYPE_APPROVE")
	public void approveRefuse(Model model, String cardTypeLogId) {
		BenefitCardTypeLog cardTypeLog = new BenefitCardTypeLog();
		cardTypeLog.setId(cardTypeLogId);
		model.addAttribute("cardTypeLog", cardTypeLog);
	}

	/**
	 * 审批退回权益卡。
	 * 
	 * @param cardTypeLogId
	 *            权益卡审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("BENEFITCARDTYPE_APPROVE")
	@RequestMapping("benefitCardType-approve-refuse-save")
	public ModelAndView approveRefuseSave(BenefitCardTypeLog cardTypeLog) {
		benefitCardTypeLogService.approveRefuseBenefitCardType(cardTypeLog);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("benefitCardType.approve.refuse.success"));
	}

	/**
	 * 审批通过权益卡。
	 * 
	 * @param cardTypeLogId
	 *            权益卡审批记录ID
	 * @return 返回提示信息。
	 */
	@Auth("BENEFITCARDTYPE_APPROVE")
	@RequestMapping("benefitCardType-approve-pass")
	public ModelAndView approvePass(String cardTypeLogId) {
		benefitCardTypeLogService.approvePassBenefitCardType(cardTypeLogId);
		return NavTabResultUtils.reload(messageSource
				.get("benefitCardType.approve.pass.success"));
	}

	/**
	 * 权益卡审批记录。
	 * 
	 * @param model
	 *            　数据模型
	 * @param typeId
	 *            　权益卡ID
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardTypeLog-view")
	public void view(Model model, String typeId) {
		BenefitCardType type = benefitCardTypeService
				.getBenefitCardType(typeId);
		List<BenefitCardTypeLog> logs = new ArrayList<BenefitCardTypeLog>();
		if (type.isBounded()) {
			logs.addAll(type.getBoundType().getLogs());
		}
		logs.addAll(type.getLogs());
		model.addAttribute("benefitCardTypeLogs", logs);
	}

	/**
	 * 加载票务卡类规则页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardTypeLog
	 *            权益卡类审批记录
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardTypeLog-rule-view")
	public void viewRule(Model model, BenefitCardTypeLog benefitCardTypeLog) {
		benefitCardTypeLog = benefitCardTypeLogService
				.getBenefitCardTypeLog(benefitCardTypeLog.getId());
		model.addAttribute("benefitCardTypeLog", benefitCardTypeLog);
	}

	/**
	 * 加载卖品卡类规则页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardTypeLog
	 *            权益卡类审批记录
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardTypeLog-snackRule-view")
	public void viewSnackRule(Model model, BenefitCardTypeLog benefitCardTypeLog) {
		benefitCardTypeLog = benefitCardTypeLogService
				.getBenefitCardTypeLog(benefitCardTypeLog.getId());
		model.addAttribute("benefitCardTypeLog", benefitCardTypeLog);
	}
}