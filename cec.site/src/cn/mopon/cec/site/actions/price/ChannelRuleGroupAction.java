package cn.mopon.cec.site.actions.price;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.service.ChannelPolicyService;
import cn.mopon.cec.core.service.ChannelRuleGroupService;
import cn.mopon.cec.core.service.ChannelShowService;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 渠道规则管理类。
 */
@Controller
@RequestMapping("/price")
public class ChannelRuleGroupAction {
	@Resource
	private ChannelRuleGroupService channelRuleGroupService;
	@Resource
	private ChannelPolicyService channelPolicyService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 保存规则分组。
	 * 
	 * @param cinemaIds
	 *            影院ID数组
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-save")
	public ModelAndView save(String[] cinemaIds, String channelPolicyId) {
		channelRuleGroupService.createChannelRuleGroup(cinemaIds,
				channelPolicyId);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("channelPolicyRuleGroup.add.success"));
	}

	/**
	 * 规则分组开放设置保存。
	 * 
	 * @param cinemaIds
	 *            影院ID数组
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-set-save")
	public ModelAndView setSave(String[] cinemaIds, String channelPolicyId) {
		List<Show> shows = channelRuleGroupService.setChannelRuleGroupOpen(
				cinemaIds, channelPolicyId);
		channelShowService.batchGenChannelShows(shows);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("channelPolicyRuleGroup.batch.open.success"),
				"channelPolicyBox");
	}

	/**
	 * 上移规则分组。
	 * 
	 * @param groupId
	 *            分组ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-up")
	public ModelAndView up(String groupId) {
		channelRuleGroupService.upChannelPolicy(channelRuleGroupService
				.getChannelRuleGroup(groupId));
		return NavTabResultUtils.reloadDiv("", "channelPolicyBox");
	}

	/**
	 * 下移规则分组。
	 * 
	 * @param groupId
	 *            分组ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-down")
	public ModelAndView down(String groupId) {
		channelRuleGroupService.downChannelPolicy(channelRuleGroupService
				.getChannelRuleGroup(groupId));
		return NavTabResultUtils.reloadDiv("", "channelPolicyBox");
	}

	/**
	 * 删除规则分组。
	 * 
	 * @param groupId
	 *            分组ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-delete")
	public ModelAndView delete(String groupId) {
		channelRuleGroupService.deleteChannelRuleGroup(channelRuleGroupService
				.getChannelRuleGroup(groupId));
		return NavTabResultUtils.reload(messageSource
				.get("channelPolicyRuleGroup.delete.success"));
	}

	/**
	 * 开放规则分组。
	 * 
	 * @param channelRuleGroup
	 *            规则分组
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelRuleGroup-open")
	public ModelAndView open(ChannelRuleGroup channelRuleGroup) {
		List<Show> shows = channelRuleGroupService
				.openChannelRuleGroup(channelRuleGroup);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("channelPolicyRuleGroup.open.success"),
				"channelPolicyBox");
	}

	/**
	 * 关闭规则分组。
	 * 
	 * @param channelRuleGroup
	 *            规则分组
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("channelRuleGroup-close")
	public ModelAndView close(ChannelRuleGroup channelRuleGroup) {
		List<Show> shows = channelRuleGroupService
				.closeChannelRuleGroup(channelRuleGroup);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("channelPolicyRuleGroup.close.success"),
				"channelPolicyBox");
	}

	/**
	 * 弹出复制新增选择规则分组页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param groupId
	 *            分组ID
	 * @param policyId
	 *            策略ID
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-copy-select")
	public void copySelect(Model model, String groupId, String policyId,
			SearchModel searchModel) {
		model.addAttribute("groupId", groupId);
		model.addAttribute("policyId", policyId);
		model.addAttribute("groupPage", channelRuleGroupService
				.searchChannelRuleGroup(groupId, policyId, searchModel));
	}

	/**
	 * 复制规则分组。
	 * 
	 * @param groupId
	 *            待复制的分组ID
	 * @param groupIds
	 *            复制到目标规则分组ID数组
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-copy")
	public ModelAndView copy(String groupId, String[] groupIds) {
		channelRuleGroupService.batchCopy(groupId, groupIds);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("channelPolicyRuleGroup.copy.success"),
				"channelPolicyBox");
	}

	/**
	 * 对策略下所有的分组进行排序。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelPolicyId
	 *            策略ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-set-ordinal")
	public void setOrdinal(Model model, String channelPolicyId) {
		model.addAttribute("channelPolicy",
				channelPolicyService.getChannelPolicy(channelPolicyId));
	}

	/**
	 * 保存分组排序。
	 * 
	 * @param groupIds
	 *            分组ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-save-ordinal")
	public ModelAndView saveOrdinal(String[] groupIds) {
		channelRuleGroupService.saveOrdinal(groupIds);
		return DialogResultUtils.closeAndReloadDiv(messageSource
				.get("channelPolicyRuleGroup.save.ordinal.success"),
				"channelPolicyBox");
	}

	/**
	 * 编辑分组。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelRuleGroup
	 *            分组
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-edit")
	public void edit(Model model, ChannelRuleGroup channelRuleGroup) {
		model.addAttribute(channelRuleGroup);
	}

	/**
	 * 更新分组。
	 * 
	 * @param channelRuleGroup
	 *            分组
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-update")
	public ModelAndView update(ChannelRuleGroup channelRuleGroup) {
		channelRuleGroupService.update(channelRuleGroup);
		return DialogResultUtils.closeAndReloadDiv(messageSource
				.get("channelPolicyRuleGroup.set.connectFee.success"),
				"channelPolicyBox");
	}

	/**
	 * 规则分组开放设置保存。
	 * 
	 * @param connectFee
	 *            接入费
	 * @param groupIds
	 *            规则分组ID数组
	 * @param channelPolicyId
	 *            策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("channelRuleGroup-connectFee-set-save")
	public ModelAndView setConncectFeeSave(Double connectFee,
			String[] groupIds, String channelPolicyId) {
		channelRuleGroupService.batchSetConnectFee(connectFee, groupIds,
				channelPolicyId);
		return DialogResultUtils.closeAndReloadDiv(messageSource
				.get("channelPolicyRuleGroup.batch.set.connectFee.success"),
				"channelPolicyBox");
	}
}