package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.model.ShowSyncLogSearchModel;
import cn.mopon.cec.core.service.ShowSyncLogService;
import coo.core.security.annotations.Auth;

/**
 * 排期同步日志管理。
 */
@Controller
@RequestMapping("/product")
@Auth("PRODUCT_VIEW")
public class ShowSyncLogAction {
	@Resource
	private ShowSyncLogService showSyncLogService;

	/**
	 * 查看排期同步日志列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("showSyncLog-list")
	public void list(Model model, ShowSyncLogSearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("showSyncLogPage",
				showSyncLogService.searchShowSyncLog(searchModel));
	}

	/**
	 * 查看排期同步日志异常信息。
	 * 
	 * @param showSyncLogId
	 *            排期同步日志ID
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("showSyncLog-view")
	public void view(String showSyncLogId, Model model) {
		model.addAttribute("showSyncLog",
				showSyncLogService.getShowSyncLog(showSyncLogId));
	}

	/**
	 * 查看排期异常日志列表。
	 * 
	 * @param showSyncLogId
	 *            排期同步日志ID
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("showErrorLog-list")
	public void listErrorLog(String showSyncLogId, Model model) {
		model.addAttribute("showSyncLog",
				showSyncLogService.getShowSyncLog(showSyncLogId));
	}
}