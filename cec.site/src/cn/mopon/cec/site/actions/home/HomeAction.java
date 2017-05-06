package cn.mopon.cec.site.actions.home;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.service.HomeService;

import com.github.abel533.echarts.Option;

import coo.core.security.annotations.Auth;

/**
 * 首页。
 */
@Controller("site.home")
@RequestMapping("/home")
@Auth
public class HomeAction {
	@Resource
	private HomeService homeService;

	/**
	 * 业务概览。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("business-overview")
	public void systemOverview(Model model) {
		model.addAttribute("businessOverview",
				homeService.getBusinessOverviewModel());
	}

	/**
	 * 待办事项。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("todo-list")
	public void todoList(Model model) {
		model.addAttribute("todoListModel", homeService.getTodoListModel());
	}

	/**
	 * 销售趋势。
	 */
	@RequestMapping("sales-chart")
	public void salesChart() {
	}

	/**
	 * 销售趋势数据。
	 * 
	 * @return 返回销售趋势数据。
	 */
	@RequestMapping("sales-chart-data")
	public Option salesChartData() {
		return homeService.getSalesChartOption();
	}

	/**
	 * 今日排行。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("today-ranks")
	public void todayRanks(Model model) {
		model.addAttribute("todayRanksModel", homeService.getTodayRanksModel());
	}
}
