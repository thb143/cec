package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.TicketAccessTypeService;
import cn.mopon.cec.core.service.TicketSettingsService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.NavTabResultUtils;

/**
 * 选座票接入设置管理。
 */
@Controller
@RequestMapping("/operate")
public class TicketSettingsAction {
	@Resource
	private TicketSettingsService ticketSettingsService;
	@Resource
	private TicketAccessTypeService ticketAccessTypeService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 编辑选座票接入设置。
	 * 
	 * @param model
	 *            数据模型
	 * @param id
	 *            选座票设置ID
	 */
	@Auth("CINEMA_VIEW")
	@RequestMapping("ticketSettings-edit")
	public void editTicketSettings(Model model, String id) {
		TicketSettings ticketSettings = ticketSettingsService
				.getTicketSettings(id);
		if (ticketSettings == null) {
			ticketSettings = new TicketSettings();
			ticketSettings.setId(id);
		}
		model.addAttribute("ticketSettings", ticketSettings);
		model.addAttribute("ticketAccessTypes",
				ticketAccessTypeService.getTicketAccessTypeList(id));
	}

	/**
	 * 更新选座票接入设置。
	 * 
	 * @param ticketSettings
	 *            选座票设置
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SET")
	@RequestMapping("ticketSettings-update")
	public ModelAndView updateTicketSettings(TicketSettings ticketSettings) {
		Cinema origCinema = cinemaService.getCinema(ticketSettings.getId());
		ticketSettingsService.updateTicketSettings(origCinema, ticketSettings);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("ticketSettings.edit.success"), "cinemaBox");
	}

}