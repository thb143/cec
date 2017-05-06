package cn.mopon.cec.site.actions.price;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.model.CinemaModel;
import cn.mopon.cec.core.model.ProviceCinemaListModel;
import cn.mopon.cec.core.service.ChannelPolicyService;
import cn.mopon.cec.core.service.ChannelRuleGroupService;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import coo.base.model.Page;
import coo.base.util.StringUtils;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 渠道策略管理。
 */
@Controller
@RequestMapping("/price")
public class ChannelPolicyAction {
	@Resource
	private ChannelService channelService;
	@Resource
	private ChannelPolicyService channelPolicyService;
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看策略列表。
	 * 
	 * @param selectedChannelPolicyId
	 *            选中的策略ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicy-list")
	public void list(String selectedChannelPolicyId, Model model,
			SearchModel searchModel) {
		model.addAttribute(channelPolicyService.searchPolicy(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("selectedChannelPolicyId", selectedChannelPolicyId);
	}

	/**
	 * 查看渠道策略开放的影院列表。
	 * 
	 * @param channelPolicyId
	 *            渠道策略ID
	 * @param selectGroupId
	 *            已选择的分组ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            检索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicy-cinema-list")
	public void cinemaList(String channelPolicyId, String selectGroupId,
			Model model, SearchModel searchModel) {
		searchModel.setPageSize(30);
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		Page<ChannelRuleGroup> groups = channelRuleGroupService
				.searchChannelRuleGroup(channelPolicyId, searchModel, null);
		model.addAttribute("channelPolicy", channelPolicy);
		model.addAttribute("groupCount", channelRuleGroupService
				.searchChannelRuleGroupCount(channelPolicyId, null));
		model.addAttribute("groupPage", groups);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("selectGroupId", selectGroupId);
	}

	/**
	 * 查看策略。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @param cinemaId
	 *            影院ID
	 * @param model
	 *            数据模型
	 * 
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("channelPolicy-view")
	public void view(String channelPolicyId, String cinemaId, Model model) {
		ChannelPolicy channelPolicy = channelPolicyService.groupChannelPolicy(
				channelPolicyId, cinemaId);
		model.addAttribute("channelPolicy", channelPolicy);
		model.addAttribute("cinemaId", cinemaId);
	}

	/**
	 * 新增策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelId
	 *            渠道ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-add")
	public void add(Model model, String channelId) {
		ChannelPolicy channelPolicy = new ChannelPolicy();
		channelPolicy.setChannel(channelService.getChannel(channelId));
		model.addAttribute(channelPolicy);
	}

	/**
	 * 保存策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-save")
	public ModelAndView save(ChannelPolicy channelPolicy) {
		channelPolicyService.createChannelPolicy(channelPolicy);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.add.success"),
				"/price/channelPolicy-list?selectedChannelPolicyId="
						+ channelPolicy.getId());
	}

	/**
	 * 编辑策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            策略ID
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelPolicy-edit")
	public void edit(Model model, String channelPolicyId) {
		model.addAttribute(channelPolicyService
				.getChannelPolicy(channelPolicyId));
	}

	/**
	 * 更新策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelPolicy-update")
	public ModelAndView update(ChannelPolicy channelPolicy) {
		// 获取渠道 用于记录日志。
		channelPolicy.setChannel(channelPolicyService.getChannelPolicy(
				channelPolicy.getId()).getChannel());
		channelPolicyService.updateChannelPolicy(channelPolicy);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("policy.edit.success"));
	}

	/**
	 * 删除策略。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-delete")
	public ModelAndView delete(String channelPolicyId) {
		channelPolicyService.deleteChannelPolicy(channelPolicyService
				.getChannelPolicy(channelPolicyId));
		return NavTabResultUtils.reload(messageSource
				.get("policy.delete.success"));
	}

	/**
	 * 启用策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelPolicy-enable")
	public ModelAndView enable(ChannelPolicy channelPolicy) {
		List<Show> shows = channelPolicyService
				.enableChannelPolicy(channelPolicy);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.forward(
				messageSource.get("policy.enable.success"),
				"/price/channelPolicy-list?selectedChannelPolicyId="
						+ channelPolicy.getId());
	}

	/**
	 * 停用策略。
	 * 
	 * @param channelPolicy
	 *            策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelPolicy-disable")
	public ModelAndView disable(ChannelPolicy channelPolicy) {
		List<Show> shows = channelPolicyService
				.disableChannelPolicy(channelPolicy);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.forward(
				messageSource.get("policy.disable.success"),
				"/price/channelPolicy-list?selectedChannelPolicyId="
						+ channelPolicy.getId());
	}

	/**
	 * 上移策略。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-up")
	@ResponseBody
	public void up(String channelPolicyId) {
		channelPolicyService.upChannelPolicy(channelPolicyService
				.getChannelPolicy(channelPolicyId));
	}

	/**
	 * 下移策略。
	 * 
	 * @param channelPolicyId
	 *            策略ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-down")
	@ResponseBody
	public void down(String channelPolicyId) {
		channelPolicyService.downChannelPolicy(channelPolicyService
				.getChannelPolicy(channelPolicyId));
	}

	/**
	 * 弹出复制新增选择策略页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelId
	 *            渠道ID
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-copy-select")
	public void copySelect(Model model, String channelId,
			SearchModel searchModel) {
		model.addAttribute("channelId", channelId);
		model.addAttribute("policyPage",
				channelPolicyService.searchChannelPolicy(searchModel));
	}

	/**
	 * 复制策略。
	 * 
	 * @param channelPolicyId
	 *            待复制的策略ID
	 * @param channelId
	 *            渠道ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-copy")
	public ModelAndView copy(String channelPolicyId, String channelId) {
		ChannelPolicy channelPolicy = channelPolicyService.copyChannelPolicy(
				channelPolicyService.getChannelPolicy(channelPolicyId),
				channelId);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.copy.success"),
				"/price/channelPolicy-list?selectedChannelPolicyId="
						+ channelPolicy.getId());
	}

	/**
	 * 弹出新增选择影院页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            策略ID
	 * @param cinemaModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-cinema-select")
	public void cinemaSelect(Model model, String channelPolicyId,
			CinemaModel cinemaModel) {
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		List<Cinema> cinemas = cinemaService.filterSelectedCinema(
				channelPolicy, cinemaModel);
		ProviceCinemaListModel proviceCinemaListModel = new ProviceCinemaListModel();
		for (Cinema cinema : cinemas) {
			proviceCinemaListModel.addProvinceCinemaModel(cinema);
		}
		model.addAttribute("cinemaModel", cinemaModel);
		model.addAttribute("cinemaListModel", proviceCinemaListModel);
		model.addAttribute("channelPolicyId", channelPolicyId);
	}

	/**
	 * 弹出影院开放设置页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            策略ID
	 * @param cinemaModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-cinema-set")
	public void cinemaSet(Model model, String channelPolicyId,
			CinemaModel cinemaModel) {
		setCinemaTreeModel(model, channelPolicyId, cinemaModel);
	}

	/**
	 * 弹出影院接入费设置页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            策略ID
	 * @param cinemaModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelPolicy-connectFee-set")
	public void connectFeeSet(Model model, String channelPolicyId,
			CinemaModel cinemaModel) {
		setCinemaTreeModel(model, channelPolicyId, cinemaModel);
	}

	/**
	 * 设置影院树形页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            策略ID
	 * @param cinemaModel
	 *            搜索条件
	 */
	private void setCinemaTreeModel(Model model, String channelPolicyId,
			CinemaModel cinemaModel) {
		ChannelPolicy channelPolicy = channelPolicyService
				.getChannelPolicy(channelPolicyId);
		ProviceCinemaListModel proviceCinemaListModel = new ProviceCinemaListModel();
		for (ChannelRuleGroup group : channelPolicy.getGroups()) {
			if (cinemaModel != null
					&& StringUtils.isNotEmpty(cinemaModel.getKeyword())) {
				if (group.getCinema().getName()
						.contains(cinemaModel.getKeyword().trim())) {
					proviceCinemaListModel.addProvinceCinemaModel(group
							.getCinema());
				}
			} else {
				proviceCinemaListModel
						.addProvinceCinemaModel(group.getCinema());
			}
		}
		model.addAttribute("cinemaModel", cinemaModel);
		model.addAttribute("cinemaListModel", proviceCinemaListModel);
		model.addAttribute("channelPolicy", channelPolicy);
	}
}