package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.model.FilmSyncLogSearchModel;
import cn.mopon.cec.core.service.FilmSyncLogService;
import coo.core.security.annotations.Auth;

/**
 * 影片同步日志管理。
 */
@Controller
@RequestMapping("/operate")
@Auth("FILM_VIEW")
public class FilmSyncLogAction {
	@Resource
	private FilmSyncLogService filmSyncLogService;

	/**
	 * 查看影片同步日志。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("FILM_VIEW")
	@RequestMapping("filmSyncLog-list")
	public void list(Model model, FilmSyncLogSearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("filmSyncLogPage",
				filmSyncLogService.searchFilmSyncLog(searchModel));
	}

	/**
	 * 查看影片同步日志异常信息。
	 * 
	 * @param filmSyncLogId
	 *            影片同步日志ID
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("filmSyncLog-view")
	public void view(String filmSyncLogId, Model model) {
		model.addAttribute("filmSyncLog",
				filmSyncLogService.getFilmSyncLog(filmSyncLogId));
	}

	/**
	 * 查看影片异常日志列表。
	 * 
	 * @param filmSyncLogId
	 *            影片同步日志ID
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("filmErrorLog-list")
	public void listErrorLog(String filmSyncLogId, Model model) {
		model.addAttribute("filmSyncLog",
				filmSyncLogService.getFilmSyncLog(filmSyncLogId));
	}
}