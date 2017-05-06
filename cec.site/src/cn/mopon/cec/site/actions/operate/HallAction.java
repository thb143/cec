package cn.mopon.cec.site.actions.operate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.HallType;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.HallService;
import cn.mopon.cec.core.service.HallTypeService;
import cn.mopon.cec.core.service.SeatService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.NavTabResultUtils;

/**
 * 影厅管理。
 */
@Controller
@RequestMapping("/operate")
public class HallAction {
	@Resource
	private HallService hallService;
	@Resource
	private SeatService seatService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private HallTypeService hallTypeService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看影厅列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaId
	 *            所属影院ID
	 */
	@Auth("CINEMA_VIEW")
	@RequestMapping("hall-list")
	public void list(Model model, String cinemaId) {
		Cinema cinema = cinemaService.getCinema(cinemaId);
		List<HallType> hallTypeList = hallTypeService.getHallTypeList();
		model.addAttribute("hallTypeList", hallTypeList);
		model.addAttribute("cinema", cinema);
	}

	/**
	 * 同步影厅。
	 * 
	 * @param cinema
	 *            所属影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hall-sync")
	public ModelAndView sync(Cinema cinema) {
		hallService.syncHalls(cinema);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("hall.sync.success"), "cinemaBox");
	}

	/**
	 * 更新影厅。
	 * 
	 * @param type
	 *            影厅类型数组
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hall-update")
	public ModelAndView update(String[] type) {
		hallService.updateHall(type);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("hall.edit.success"), "cinemaBox");
	}

	/**
	 * 启用影厅。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("hall-enable")
	public ModelAndView enable(Hall hall) {
		List<Show> shows = hallService.enableHall(hall);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("hall.enable.success"), "cinemaBox");
	}

	/**
	 * 停用影厅。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("hall-disable")
	public ModelAndView disable(Hall hall) {
		Integer count = hallService.disableHall(hall);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("hall.disable.success", count), "cinemaBox");
	}

	/**
	 * 同步影厅座位。
	 * 
	 * @param hall
	 *            影厅
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hall-seats-sync")
	public ModelAndView syncSeats(Hall hall) {
		seatService.syncSeats(hall, new ArrayList<Hall>());
		return NavTabResultUtils.reloadDiv(
				messageSource.get("hall.sync.seats.success"), "cinemaBox");
	}
}