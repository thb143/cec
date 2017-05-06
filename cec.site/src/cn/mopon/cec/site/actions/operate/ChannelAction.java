package cn.mopon.cec.site.actions.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.ChannelShowService;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 渠道管理。
 */
@Controller
@RequestMapping("/operate")
public class ChannelAction {
	@Resource
	private ChannelService channelService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看渠道列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("CHANNEL_VIEW")
	@RequestMapping("channel-list")
	public void list(Model model, SearchModel searchModel) {
		model.addAttribute("channelPage",
				channelService.searchChannel(searchModel));
	}

	/**
	 * 新增接口渠道。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("CHANNEL_MANAGE")
	@RequestMapping("channel-add")
	public void add(Model model) {
		model.addAttribute(new Channel());
	}

	/**
	 * 保存接口渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回提示信息。
	 */
	@Auth("CHANNEL_MANAGE")
	@RequestMapping("channel-save")
	public ModelAndView save(Channel channel) {
		channelService.createChannel(channel);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("channel.add.success"));
	}

	/**
	 * 编辑渠道。
	 * 
	 * @param channelId
	 *            渠道ID
	 * @param model
	 *            数据模型
	 */
	@Auth("CHANNEL_MANAGE")
	@RequestMapping("channel-edit")
	public void edit(String channelId, Model model) {
		model.addAttribute(channelService.getChannel(channelId));
	}

	/**
	 * 查看渠道。
	 * 
	 * @param channelId
	 *            渠道ID
	 * @param model
	 *            数据模型
	 */
	@Auth("CHANNEL_VIEW")
	@RequestMapping("channel-view")
	public void view(String channelId, Model model) {
		model.addAttribute(channelService.getChannel(channelId));
	}

	/**
	 * 更新渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("CHANNEL_MANAGE")
	@RequestMapping("channel-update")
	public ModelAndView update(Channel channel) {
		channelService.updateChannel(channel);
		return DialogResultUtils.closeAndReloadNavTab(
				messageSource.get("channel.edit.success"), "channel-list");
	}

	/**
	 * 开放渠道。
	 * 
	 * @param channel
	 *            渠道ID
	 * @return 返回提示信息。
	 */
	@Auth("CHANNEL_SWITCH")
	@RequestMapping("channel-enable")
	public ModelAndView enable(Channel channel) {
		List<Show> shows = channelService.enableChannel(channel);
		channelShowService.batchGenChannelShows(shows);
		return DialogResultUtils.reload(messageSource
				.get("channel.enable.success"));
	}

	/**
	 * 关闭渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回提示信息。
	 */
	@Auth("CHANNEL_SWITCH")
	@RequestMapping("channel-disable")
	public ModelAndView disable(Channel channel) {
		Integer count = channelService.disableChannel(channel);
		return DialogResultUtils.reload(messageSource.get(
				"channel.disable.success", count));
	}

	/**
	 * 启售渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("channel-open-salable")
	public ModelAndView openSalable(Channel channel) {
		Integer count = channelService.openChannelSalable(channel);
		return NavTabResultUtils.reload(messageSource.get(
				"channel.open.salable.success", count));
	}

	/**
	 * 停售渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("channel-close-salable")
	public ModelAndView closeSalable(Channel channel) {
		Integer count = channelService.closeChannelSalable(channel);
		return NavTabResultUtils.reload(messageSource.get(
				"channel.close.salable.success", count));
	}
}