package cn.mopon.cec.site.actions.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.entity.CinemaChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.CinemaTicketOrderDaily;
import cn.mopon.cec.core.model.StatSearchModel;
import cn.mopon.cec.core.service.CinemaChannelTicketOrderDailyService;
import cn.mopon.cec.core.service.CinemaTicketOrderDailyService;
import cn.mopon.cec.core.service.TicketOrderStatService;
import coo.base.model.Page;
import coo.core.jxls.Excel;
import coo.core.jxls.ExcelData;
import coo.core.security.annotations.Auth;

/**
 * 影院统计。
 */
@Controller
@RequestMapping("/stat")
public class CinemaStatAction extends BaseStatAction {
	@Resource
	private CinemaTicketOrderDailyService cinemaTicketOrderDailyService;
	@Resource
	private CinemaChannelTicketOrderDailyService cinemaChannelTicketOrderDailyService;
	@Resource
	private TicketOrderStatService ticketOrderStatService;

	/**
	 * 影院结算列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("cinemaTicketOrderDaily-list")
	@Auth("CINEMA_SETTLE_STAT")
	public void list(Model model, StatSearchModel searchModel) {
		setBeforeMonthIfNull(searchModel);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaTicketOrderDailyPage",
				cinemaTicketOrderDailyService
						.searchCinemaTicketOrderStat(searchModel));
	}

	/**
	 * 正常订单名细。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            查询对象
	 */
	@RequestMapping("ticketOrderStat-view")
	@Auth("CINEMA_SETTLE_STAT")
	public void orderStatView(Model model, StatSearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("ticketOrderStatPage",
				ticketOrderStatService.searchTicketOrderStat(searchModel));
	}

	/**
	 * 影院結算导出报表。
	 * 
	 * @param searchModel
	 *            数据模型
	 * @param response
	 *            response 对象
	 * @throws IOException
	 *             io异常
	 */
	@RequestMapping("cinemaStat-export")
	@Auth("CINEMA_SETTLE_STAT")
	public void export(StatSearchModel searchModel, HttpServletResponse response)
			throws IOException {
		setBeforeMonthIfNull(searchModel);
		Page<CinemaTicketOrderDaily> cinemaDailys = cinemaTicketOrderDailyService
				.searchCinemaTicketOrderStat(searchModel);
		String cinemaId = searchModel.getCinemaId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dailys", cinemaDailys.getContents());
		map.put("startDate", getDateStr(searchModel.getStartDate()));
		map.put("endDate", getDateStr(searchModel.getEndDate()));
		map.put("list", getNormalOrder(searchModel));
		searchModel.setCinemaId(cinemaId);
		ExcelData data = new ExcelData();
		String fileName = "cinema_sett" + getCurrentDate() + ".xls";
		data.setTemplateFileName("cinema_sett.xls");
		data.setSheetModels(cinemaDailys.getContents());
		data.setSheetNames(getSheetNames(cinemaDailys.getContents()));
		data.setOtherModel(map);
		data.setSheetModelName("test");
		data.setStartSheetNum(1);
		Excel excel = new Excel();
		excel.init(data);
		mergeCinemaSettHeader(excel);
		setResponseHeader(response, fileName);
		excel.writeTo(response.getOutputStream());
	}

	/**
	 * 影院销售统计。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @param response
	 *            response 对象
	 * @throws IOException
	 *             io异常
	 */
	@RequestMapping("cinemaSaleStat-export")
	@Auth("CINEMA_SALE_STAT")
	public void cinemaSaleStatExport(StatSearchModel searchModel,
			HttpServletResponse response) throws IOException {
		setCurrentMonthIfNull(searchModel);
		searchModel.setPageSize(Integer.MAX_VALUE);
		Page<CinemaTicketOrderDaily> cinemaDailys = cinemaTicketOrderDailyService
				.searchCinemaTicketOrderStat(searchModel);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dailys", cinemaDailys.getContents());
		map.put("startDate", getDateStr(searchModel.getStartDate()));
		map.put("endDate", getDateStr(searchModel.getEndDate()));
		map.put("list", getCinemaChannelStat(searchModel));
		ExcelData data = new ExcelData();
		String fileName = "cinema_sale_report" + getCurrentDate() + ".xls";
		data.setTemplateFileName("cinema_sale_report.xls");
		data.setSheetModels(cinemaDailys.getContents());
		data.setSheetNames(getSheetNames(cinemaDailys.getContents()));
		data.setOtherModel(map);
		data.setSheetModelName("test");
		data.setStartSheetNum(1);
		Excel excel = new Excel();
		excel.init(data);
		mergeCinemaAndChannelHeader(excel);
		setResponseHeader(response, fileName);
		excel.writeTo(response.getOutputStream());
	}

	/**
	 * 获取工作表名称。
	 * 
	 * @param list
	 *            数据列表
	 * @return 返回工作表名称。
	 */
	private List<String> getSheetNames(List<CinemaTicketOrderDaily> list) {
		List<String> listSheetNames = new ArrayList<String>();
		for (CinemaTicketOrderDaily daily : list) {
			listSheetNames
					.add(filterExcelKeyWords(daily.getCinema().getName()));
		}
		return listSheetNames;
	}

	/**
	 * 影院渠道销售统计。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 影院渠道销售统计列表。
	 */
	private List<Map<String, Object>> getCinemaChannelStat(
			StatSearchModel searchModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (searchModel.getCinemaId().contains(",")) {
			String[] cinemaIds = searchModel.getCinemaId().split(",");
			for (String cinemaId : cinemaIds) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				searchModel.setCinemaId(cinemaId);
				map1.put("list", cinemaChannelTicketOrderDailyService
						.searchCinemaChannelTicketOrderStat(searchModel)
						.getContents());
				list.add(map1);
			}
		} else {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("list", cinemaChannelTicketOrderDailyService
					.searchCinemaChannelTicketOrderStat(searchModel)
					.getContents());
			list.add(map1);
		}
		return list;
	}

	/**
	 * 正常订单明细。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回正常订单明细。
	 */
	private List<Map<String, Object>> getNormalOrder(StatSearchModel searchModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (searchModel.getCinemaId().contains(",")) {
			String[] cinemaIds = searchModel.getCinemaId().split(",");
			for (String cinemaId : cinemaIds) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				searchModel.setCinemaId(cinemaId);
				map1.put("list", ticketOrderStatService
						.searchTicketOrderStatList(searchModel));
				list.add(map1);
			}
		} else {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("list", ticketOrderStatService
					.searchTicketOrderStatList(searchModel));
			list.add(map1);
		}
		return list;
	}

	/**
	 * 影院销售统计列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("cinemaSaleStat-list")
	@Auth("CINEMA_SALE_STAT")
	public void cinemaSaleStat(Model model, StatSearchModel searchModel) {
		setCurrentMonthIfNull(searchModel);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaChannelSaleStat",
				cinemaTicketOrderDailyService
						.searchCinemaTicketOrderStat(searchModel));
	}

	/**
	 * 影院渠道销售统计列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("cinemaChannelSaleStat-list")
	@Auth("CINEMA_SALE_STAT")
	public void cinemaChannelSaleStat(Model model, StatSearchModel searchModel) {
		searchModel.setPageSize(Integer.MAX_VALUE);
		Page<CinemaChannelTicketOrderDaily> dailyPage = cinemaChannelTicketOrderDailyService
				.searchCinemaChannelTicketOrderStat(searchModel);
		model.addAttribute("dailyPage", dailyPage);
		model.addAttribute("searchModel", searchModel);
	}

}
