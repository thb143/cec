package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.model.SnackChannelCopyModel;
import cn.mopon.cec.core.model.SnackChannelListModel;
import cn.mopon.cec.core.service.SnackChannelService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 卖品分类管理。
 */
@Controller
@RequestMapping("/product")
public class SnackChannelAction {
	@Resource
	private SnackChannelService snackChannelService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 开放卖品渠道。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("snackChannel-open")
	public ModelAndView open(SnackChannel snackChannel) {
		snackChannelService.openSnackChannel(snackChannel);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("snackChannel.open.success"), "snackListBox");
	}

	/**
	 * 关闭卖品渠道。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("snackChannel-close")
	public ModelAndView close(SnackChannel snackChannel) {
		snackChannelService.closeSnackChannel(snackChannel);
		return NavTabResultUtils
				.reloadDiv(messageSource.get("snackChannel.close.success"),
						"snackListBox");
	}

	/**
	 * 编辑卖品渠道。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackChannel-edit")
	public void edit(SnackChannel snackChannel, Model model) {
		model.addAttribute("snackChannel",
				snackChannelService.getSnackChannel(snackChannel.getId()));
	}

	/**
	 * 保存卖品渠道设置。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackChannel-update")
	public ModelAndView save(SnackChannel snackChannel) {
		snackChannelService.updateSnackChannel(snackChannel);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("snackChannel.edit.success"), "snackListBox");
	}

	/**
	 * 新增卖品渠道。
	 * 
	 * @param model
	 *            数据模型
	 * @param snack
	 *            卖品
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackChannel-add")
	public void addChannels(Model model, Snack snack) {
		SnackChannelListModel snackChannelListModel = snackChannelService
				.getSnackChannelListModel(snack.getId());
		model.addAttribute("snackChannelListModel", snackChannelListModel);
	}

	/**
	 * 保存卖品渠道。
	 * 
	 * @param snackChannelListModel
	 *            卖品渠道列表模型
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackChannel-save")
	public ModelAndView saveChannels(SnackChannelListModel snackChannelListModel) {
		snackChannelService.saveSnackChannels(snackChannelListModel);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("snackChannel.add.success"), "snackListBox");
	}

	/**
	 * 复制卖品渠道。
	 * 
	 * @param model
	 *            数据模型
	 * @param snackChannel
	 *            卖品渠道
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackChannel-copy")
	public void copyChannel(Model model, SnackChannel snackChannel) {
		SnackChannel origSnackChannel = snackChannelService
				.getSnackChannel(snackChannel.getId());
		SnackChannelListModel snackChannelListModel = snackChannelService
				.getSnackChannelListModel(origSnackChannel.getSnack().getId());
		SnackChannelCopyModel snackChannelCopyModel = new SnackChannelCopyModel();
		snackChannelCopyModel.setOrigSnackChannelId(origSnackChannel.getId());
		model.addAttribute("snackChannelCopyModel", snackChannelCopyModel);
		model.addAttribute("snackChannelListModel", snackChannelListModel);
	}

	/**
	 * 保存复制卖品渠道。
	 * 
	 * @param snackChannelCopyModel
	 *            保存渠道复制模型
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackChannel-copy-save")
	public ModelAndView copySaveChannels(
			SnackChannelCopyModel snackChannelCopyModel) {
		snackChannelService.copySnackChannel(snackChannelCopyModel);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("snackChannel.copy.success"), "snackListBox");
	}
}