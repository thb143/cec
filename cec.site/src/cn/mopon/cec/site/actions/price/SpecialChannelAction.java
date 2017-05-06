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
import cn.mopon.cec.core.assist.settle.RoundSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.model.SpecialChannelCopyModel;
import cn.mopon.cec.core.model.SpecialChannelListModel;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.Circuit;
import cn.mopon.cec.core.service.SpecialChannelService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 特殊定价规则渠道管理。
 */
@Controller
@RequestMapping("/price")
public class SpecialChannelAction {
	@Resource
	private SpecialChannelService specialChannelService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private Circuit circuit;
	@Resource
	private MessageSource messageSource;

	/**
	 * 开放特殊定价规则渠道。
	 * 
	 * @param specialChannel
	 *            特殊定价规则渠道
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialChannel-open")
	public ModelAndView open(SpecialChannel specialChannel) {
		List<Show> shows = specialChannelService
				.openSpecialChannel(specialChannel);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("specialChannel.open.success"),
				"specialPolicyBox");
	}

	/**
	 * 关闭特殊定价规则渠道。
	 * 
	 * @param specialChannel
	 *            特殊定价规则渠道
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialChannel-close")
	public ModelAndView close(SpecialChannel specialChannel) {
		List<Show> shows = specialChannelService
				.closeSpecialChannel(specialChannel);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("specialChannel.close.success"),
				"specialPolicyBox");
	}

	/**
	 * 设置特殊定价规则渠道。
	 * 
	 * @param specialChannelId
	 *            特殊定价规则渠道ID
	 * @param model
	 *            数据模型
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-edit")
	public void set(String specialChannelId, Model model) {
		model.addAttribute("specialChannel",
				specialChannelService.getSpecialChannel(specialChannelId));
		model.addAttribute("settleRuleTypes",
				circuit.getSettleRuleTypesExcludeRound());
	}

	/**
	 * 保存特殊定价规则渠道设置。
	 * 
	 * @param specialChannel
	 *            特殊定价规则渠道
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-update")
	public ModelAndView save(SpecialChannel specialChannel) {
		specialChannelService.updateSpecialChannel(specialChannel);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("specialChannel.edit.success"),
				"specialPolicyBox");
	}

	/**
	 * 设置特殊定价规则渠道。
	 * 
	 * @param model
	 *            数据模型
	 * @param specialRuleId
	 *            特殊定价规则ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-add")
	public void addChannels(Model model, String specialRuleId) {
		SpecialChannelListModel specialChannelListModel = specialChannelService
				.getSpecialChannelListModel(specialRuleId);
		model.addAttribute("specialChannelListModel", specialChannelListModel);
	}

	/**
	 * 保存特殊定价规则渠道。
	 * 
	 * @param specialChannelListModel
	 *            特殊定价规则渠道列表模型
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-save")
	public ModelAndView saveChannels(
			SpecialChannelListModel specialChannelListModel) {
		specialChannelService.saveSpecialChannels(specialChannelListModel);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("specialChannel.add.success"),
				"specialPolicyBox");
	}

	/**
	 * 复制特殊定价规则渠道。
	 * 
	 * @param model
	 *            数据模型
	 * @param specialChannelId
	 *            特殊定价规则渠道ID
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-copy")
	public void copyChannel(Model model, String specialChannelId) {
		SpecialChannel specialChannel = specialChannelService
				.getSpecialChannel(specialChannelId);
		SpecialChannelListModel specialChannelListModel = specialChannelService
				.getSpecialChannelListModel(specialChannel.getRule().getId());
		SpecialChannelCopyModel specialChannelCopyModel = new SpecialChannelCopyModel();
		specialChannelCopyModel.setOrigSpecialChannelId(specialChannel.getId());
		model.addAttribute("specialChannelCopyModel", specialChannelCopyModel);
		model.addAttribute("specialChannelListModel", specialChannelListModel);
	}

	/**
	 * 保存复制特殊定价规则渠道。
	 * 
	 * @param specialChannelCopyModel
	 *            保存渠道复制模型
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-copy-save")
	public ModelAndView copySaveChannels(
			SpecialChannelCopyModel specialChannelCopyModel) {
		specialChannelService.copySpecialChannel(specialChannelCopyModel);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("specialChannel.copy.success"),
				"specialPolicyBox");
	}

	/**
	 * 删除特殊定价规则渠道。
	 * 
	 * @param specialChannelId
	 *            特殊定价规则渠道ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialChannel-delete")
	public ModelAndView delete(String specialChannelId) {
		specialChannelService.deleteSpecialChannel(specialChannelService
				.getSpecialChannel(specialChannelId));
		return DialogResultUtils.reloadDiv(
				messageSource.get("specialChannel.delete.success"),
				"specialPolicyBox");
	}

	/**
	 * 包装渠道规则。
	 * 
	 * @param request
	 *            请求对象
	 * @param specialChannelListModel
	 *            渠道列表
	 * @return 返回包装后的渠道。
	 */
	@ModelAttribute
	public SpecialChannelListModel wrapSpecialChannelListModel(
			HttpServletRequest request,
			SpecialChannelListModel specialChannelListModel) {
		String uri = request.getRequestURI();
		if (uri.endsWith("specialChannel-save")) {
			SettleRuleType type = IEnumUtils.getIEnumByValue(
					SettleRuleType.class,
					request.getParameter("settleRule.type"));
			if (type == SettleRuleType.ROUND) {
				specialChannelListModel.setSettleRule(RoundSettleRule
						.wrapRoundSettleRule(request));
			} else {
				specialChannelListModel.setSettleRule(SettleRule
						.createSettleRule(specialChannelListModel
								.getSettleRule().getType()));
			}
		}
		return specialChannelListModel;
	}

	/**
	 * 包装特殊定价规则渠道。
	 * 
	 * @param request
	 *            请求对象
	 * @param specialChannel
	 *            特殊定价规则渠道
	 * @return 返回包装后的特殊定价规则渠道。
	 */
	@ModelAttribute
	public SpecialChannel wrapSpecialChannel(HttpServletRequest request,
			SpecialChannel specialChannel) {
		String uri = request.getRequestURI();
		if (uri.endsWith("specialChannel-update")) {
			SettleRuleType type = IEnumUtils.getIEnumByValue(
					SettleRuleType.class,
					request.getParameter("settleRule.type"));
			if (type == SettleRuleType.ROUND) {
				specialChannel.setSettleRule(RoundSettleRule
						.wrapRoundSettleRule(request));
			} else {
				specialChannel.setSettleRule(SettleRule
						.createSettleRule(specialChannel.getSettleRule()
								.getType()));
			}
		}
		return specialChannel;
	}
}