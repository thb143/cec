package cn.mopon.cec.site.actions.price;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaPolicyService;
import cn.mopon.cec.core.service.CinemaService;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 影院结算策略管理。
 */
@Controller
@RequestMapping("/price")
public class CinemaPolicyAction {
	@Resource
	private CinemaService cinemaService;
	@Resource
	private CinemaPolicyService cinemaPolicyService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看策略列表。
	 * 
	 * @param selectedPolicyId
	 *            选中的策略ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("cinemaPolicy-list")
	public void list(String selectedPolicyId, Model model,
			SearchModel searchModel) {
		model.addAttribute("selectedPolicyId", selectedPolicyId);
		model.addAttribute("cinemaPage",
				cinemaService.searchCinema(searchModel));
	}

	/**
	 * 查看策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            策略ID
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("cinemaPolicy-view")
	public void view(Model model, String policyId) {
		CinemaPolicy policy = cinemaPolicyService.getPolicy(policyId);
		model.addAttribute("cinemaRuleList", policy.getAllCinemaRuleList());
		model.addAttribute(policy);
	}

	/**
	 * 新增策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaId
	 *            影院ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-add")
	public void add(Model model, String cinemaId) {
		CinemaPolicy cinemaPolicy = new CinemaPolicy();
		cinemaPolicy.setCinema(cinemaService.getCinema(cinemaId));
		model.addAttribute(cinemaPolicy);
	}

	/**
	 * 保存策略。
	 * 
	 * @param cinemaPolicy
	 *            定价策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-save")
	public ModelAndView save(CinemaPolicy cinemaPolicy) {
		cinemaPolicyService.createCinemaPolicy(cinemaPolicy);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.add.success"),
				"/price/cinemaPolicy-list?selectedPolicyId="
						+ cinemaPolicy.getId());
	}

	/**
	 * 弹出复制新增选择策略页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaId
	 *            影院ID
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-copy-select")
	public void copySelect(Model model, String cinemaId, SearchModel searchModel) {
		model.addAttribute("cinemaId", cinemaId);
		model.addAttribute("policyPage",
				cinemaPolicyService.searchPolicy(searchModel));
	}

	/**
	 * 复制结算策略。
	 * 
	 * @param policyId
	 *            待复制的策略ID
	 * @param cinemaId
	 *            影院ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-copy")
	public ModelAndView copy(String policyId, String cinemaId) {
		CinemaPolicy policy = cinemaPolicyService.copyPolicy(
				cinemaPolicyService.getPolicy(policyId), cinemaId);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.copy.success"),
				"/price/cinemaPolicy-list?selectedPolicyId=" + policy.getId());
	}

	/**
	 * 编辑策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            策略ID
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("cinemaPolicy-edit")
	public void edit(Model model, String policyId) {
		model.addAttribute(cinemaPolicyService.getPolicy(policyId));
	}

	/**
	 * 更新策略。
	 * 
	 * @param cinemaPolicy
	 *            策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("cinemaPolicy-update")
	public ModelAndView update(CinemaPolicy cinemaPolicy) {
		// 获取影院 用于记录日志。
		cinemaPolicy.setCinema(cinemaPolicyService.getPolicy(
				cinemaPolicy.getId()).getCinema());
		cinemaPolicyService.updateCinemaPolicy(cinemaPolicy);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("policy.edit.success"));
	}

	/**
	 * 启用影院结算策略。
	 * 
	 * @param cinemaPolicy
	 *            影院结算策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("cinemaPolicy-enable")
	public ModelAndView enable(CinemaPolicy cinemaPolicy) {
		List<Show> shows = cinemaPolicyService.enablePolicy(cinemaPolicy);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.forward(
				messageSource.get("policy.enable.success"),
				"/price/cinemaPolicy-list?selectedPolicyId="
						+ cinemaPolicy.getId());
	}

	/**
	 * 停用影院结算策略。
	 * 
	 * @param cinemaPolicy
	 *            影院结算策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("cinemaPolicy-disable")
	public ModelAndView disable(CinemaPolicy cinemaPolicy) {
		List<Show> shows = cinemaPolicyService.disablePolicy(cinemaPolicy);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.forward(
				messageSource.get("policy.disable.success"),
				"/price/cinemaPolicy-list?selectedPolicyId="
						+ cinemaPolicy.getId());
	}

	/**
	 * 删除策略。
	 * 
	 * @param policyId
	 *            策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-delete")
	public ModelAndView delete(String policyId) {
		cinemaPolicyService.deleteCinemaPolicy(cinemaPolicyService
				.getPolicy(policyId));
		return NavTabResultUtils.reload(messageSource
				.get("policy.delete.success"));
	}

	/**
	 * 上移策略。
	 * 
	 * @param policyId
	 *            结算策略ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-up")
	@ResponseBody
	public void up(String policyId) {
		cinemaPolicyService.upCinemaPolicy(cinemaPolicyService
				.getPolicy(policyId));
	}

	/**
	 * 下移策略。
	 * 
	 * @param policyId
	 *            结算策略ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("cinemaPolicy-down")
	@ResponseBody
	public void down(String policyId) {
		cinemaPolicyService.downCinemaPolicy(cinemaPolicyService
				.getPolicy(policyId));
	}
}