package cn.mopon.cec.site.actions.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 影院管理。
 */
@Controller
@RequestMapping("/operate")
public class CinemaAction {
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;
	@Value(value = "${content.server.url}")
	private String url;

	/**
	 * 查看影院列表。
	 * 
	 * @param selectedId
	 *            选择的接入类型
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("CINEMA_VIEW")
	@RequestMapping("cinema-list")
	public void cinemaList(String selectedId, Model model,
			SearchModel searchModel) {
		model.addAttribute("selectedId", selectedId);
		model.addAttribute("cinemaPage",
				cinemaService.searchCinema(searchModel));
	}

	/**
	 * 新增影院。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("cinema-add")
	public void add(Model model) {
		model.addAttribute(new Cinema());
	}

	/**
	 * 保存影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("cinema-save")
	public ModelAndView save(Cinema cinema) {
		cinemaService.createCinema(cinema);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("cinema.add.success"),
				"/operate/cinema-list?selectedId=" + cinema.getId());
	}

	/**
	 * 编辑影院。
	 * 
	 * @param cinema
	 *            影院
	 * @param model
	 *            数据模型
	 */
	@Auth("CINEMA_VIEW")
	@RequestMapping("cinema-edit")
	public void edit(Cinema cinema, Model model) {
		url = url.endsWith("/") ? url : url + "/";
		model.addAttribute("url", url);
		model.addAttribute("cinema", cinema);
	}

	/**
	 * 更新影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("cinema-update")
	public ModelAndView update(Cinema cinema) {
		cinemaService.updateCinema(cinema);
		return NavTabResultUtils.forward(
				messageSource.get("cinema.edit.success"),
				"/operate/cinema-list?selectedId=" + cinema.getId());
	}

	/**
	 * 启用影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("cinema-enable")
	public ModelAndView enable(Cinema cinema) {
		List<Show> shows = cinemaService.enableCinema(cinema);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.forward(
				messageSource.get("cinema.enable.success"),
				"/operate/cinema-list?selectedId=" + cinema.getId());
	}

	/**
	 * 停用影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("cinema-disable")
	public ModelAndView disable(Cinema cinema) {
		Integer count = cinemaService.disableCinema(cinema);
		return NavTabResultUtils.forward(
				messageSource.get("cinema.disable.success", count),
				"/operate/cinema-list?selectedId=" + cinema.getId());
	}

	/**
	 * 开放销售影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("cinema-open-salable")
	public ModelAndView openSalable(Cinema cinema) {
		Integer count = cinemaService.openCinemaSalable(cinema);
		return NavTabResultUtils.forward(
				messageSource.get("cinema.open.salable.success", count),
				"/operate/cinema-list?selectedId=" + cinema.getId());
	}

	/**
	 * 关闭销售影院。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SWITCH")
	@RequestMapping("cinema-close-salable")
	public ModelAndView closeSalable(Cinema cinema) {
		Integer count = cinemaService.closeCinemaSalable(cinema);
		return NavTabResultUtils.forward(
				messageSource.get("cinema.close.salable.success", count),
				"/operate/cinema-list?selectedId=" + cinema.getId());
	}
}