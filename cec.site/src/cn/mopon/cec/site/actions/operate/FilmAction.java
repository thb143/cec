package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.model.FilmSyncModel;
import cn.mopon.cec.core.service.FilmService;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 影片管理。
 */
@Controller
@RequestMapping("/operate")
public class FilmAction {
	@Resource
	private FilmService filmService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看影片列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("FILM_VIEW")
	@RequestMapping("film-list")
	public void list(Model model, SearchModel searchModel) {
		model.addAttribute("filmPage", filmService.searchFilm(searchModel));
	}

	/**
	 * 查看影片。
	 * 
	 * @param model
	 *            数据模型
	 * @param film
	 *            影片
	 */
	@Auth("FILM_VIEW")
	@RequestMapping("film-view")
	public void view(Model model, Film film) {
		model.addAttribute(film);
	}

	/**
	 * 同步影片。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("film-sync-view")
	public void syncFilmForCsp(Model model) {
		model.addAttribute("searchModel", new FilmSyncModel());
	}

	/**
	 * 同步影片。
	 * 
	 * @param filmSyncModel
	 *            同步影片模型
	 * 
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("film-sync")
	public ModelAndView syncFilms(FilmSyncModel filmSyncModel) {
		filmService.syncFilms(filmSyncModel);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("film.sync.success"));
	}
}