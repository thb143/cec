package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.ChannelSettings;
import cn.mopon.cec.core.service.ChannelSettingsService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 渠道设置。
 */
@Controller
@RequestMapping("/operate")
@Auth("CHANNEL_SET")
public class ChannelSettingsAction {
	@Resource
	private ChannelSettingsService settingsService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 设置渠道。
	 * 
	 * @param settingsId
	 *            渠道设置ID
	 * @param model
	 *            数据模型
	 */
	@Auth("CHANNEL_SET")
	@RequestMapping("channel-set")
	public void set(String settingsId, Model model) {
		model.addAttribute("channelSettings",
				settingsService.getChannelSettings(settingsId));
	}

	/**
	 * 保存渠道设置。
	 * 
	 * @param channelSettings
	 *            渠道设置
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("CHANNEL_SET")
	@RequestMapping("channel-set-save")
	public ModelAndView setSave(ChannelSettings channelSettings) {
		settingsService.updateChannelSettings(channelSettings);
		return DialogResultUtils.closeAndReloadNavTab(
				messageSource.get("channel.set.success"), "channel-list");
	}
}