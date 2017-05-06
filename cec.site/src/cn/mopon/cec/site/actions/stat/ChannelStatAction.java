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

import cn.mopon.cec.core.entity.ChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.CinemaChannelTicketOrderDaily;
import cn.mopon.cec.core.model.StatSearchModel;
import cn.mopon.cec.core.service.ChannelTicketOrderDailyService;
import cn.mopon.cec.core.service.CinemaChannelTicketOrderDailyService;
import cn.mopon.cec.core.service.TicketOrderStatService;
import coo.base.model.Page;
import coo.base.util.StringUtils;
import coo.core.jxls.Excel;
import coo.core.jxls.ExcelData;
import coo.core.security.annotations.Auth;

/**
 * 渠道统计。
 */
@Controller
@RequestMapping("/stat")
public class ChannelStatAction extends BaseStatAction {
	@Resource
	private ChannelTicketOrderDailyService channelTicketOrderDailyService;
	@Resource
	private CinemaChannelTicketOrderDailyService cinemaChannelTicketOrderDailyService;
	@Resource
	private TicketOrderStatService ticketOrderStatService;

	/**
	 * 渠道结算列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("channelTicketOrderDaily-list")
	@Auth("CHANNEL_SETTLE_STAT")
	public void list(Model model, StatSearchModel searchModel) {
		setBeforeMonthIfNull(searchModel);
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channelTicketNormalOrderPage",
				channelTicketOrderDailyService
						.searchChannelTicketOrderStat(searchModel));
	}

	/**
	 * 渠道正常订单名细。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            查询对象
	 */
	@RequestMapping("channelTicketOrderStat-view")
	@Auth("CHANNEL_SETTLE_STAT")
	public void normalView(Model model, StatSearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channelTicketPage", channelTicketOrderDailyService
				.searchTicketOrderStat(searchModel));
	}

	/**
	 * 渠道結算导出报表。
	 * 
	 * @param searchModel
	 *            数据模型
	 * @param response
	 *            response 对象
	 * @throws IOException
	 *             io异常
	 */
	@RequestMapping("channelStat-export")
	@Auth("CHANNEL_SETTLE_STAT")
	public void export(StatSearchModel searchModel, HttpServletResponse response)
			throws IOException {
		setBeforeMonthIfNull(searchModel);
		Page<ChannelTicketOrderDaily> channelDailys = channelTicketOrderDailyService
				.searchChannelTicketOrderStat(searchModel);
		String channelId = searchModel.getChannelId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dailys", channelDailys.getContents());
		map.put("startDate", getDateStr(searchModel.getStartDate()));
		map.put("endDate", getDateStr(searchModel.getEndDate()));
		map.put("list", getNormalOrder(searchModel));
		searchModel.setChannelId(channelId);
		ExcelData data = new ExcelData();
		String fileName = "channel_sett" + getCurrentDate() + ".xls";
		data.setTemplateFileName("channel_sett.xls");
		data.setSheetModels(channelDailys.getContents());
		data.setSheetNames(getSheetNames(channelDailys.getContents()));
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
	 * 获取工作表名称。
	 * 
	 * @param list
	 *            数据列表
	 * @return 返回工作表名称。
	 */
	private List<String> getSheetNames(List<ChannelTicketOrderDaily> list) {
		List<String> listSheetNames = new ArrayList<String>();
		for (ChannelTicketOrderDaily daily : list) {
			listSheetNames
					.add(filterExcelKeyWords(daily.getChannel().getName()));
		}
		return listSheetNames;
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
		if (searchModel.getChannelId().contains(",")) {
			String[] channelIds = searchModel.getChannelId().split(",");
			for (String channelId : channelIds) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				searchModel.setChannelId(channelId);
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
	 * 渠道销售统计列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("channelSaleStat-list")
	@Auth("CHANNEL_SALE_STAT")
	public void channelSaleStat(Model model, StatSearchModel searchModel) {
		setCurrentMonthIfNull(searchModel);
		Page<ChannelTicketOrderDaily> channelDailys = channelTicketOrderDailyService
				.searchChannelTicketOrderStat(searchModel);
		model.addAttribute("channelDailys", channelDailys);
		model.addAttribute("searchModel", searchModel);
	}

	/**
	 * 渠道影院销售统计列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("channelCinemaSaleStat-list")
	@Auth("CHANNEL_SALE_STAT")
	public void channelCinemaSaleStat(Model model, StatSearchModel searchModel) {
		searchModel.setPageSize(Integer.MAX_VALUE);
		Page<CinemaChannelTicketOrderDaily> dailyPage = cinemaChannelTicketOrderDailyService
				.searchCinemaChannelTicketOrderStat(searchModel);
		model.addAttribute("dailyPage", dailyPage);
		model.addAttribute("searchModel", searchModel);
	}

	/**
	 * 导出渠道维度报表。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @param response
	 *            response 对象
	 * @throws IOException
	 *             io异常
	 */
	@RequestMapping("channelSaleStat-export")
	@Auth("CHANNEL_SALE_STAT")
	public void channelSaleStatExport(StatSearchModel searchModel,
			HttpServletResponse response) throws IOException {
		setCurrentMonthIfNull(searchModel);
		searchModel.setPageSize(Integer.MAX_VALUE);
		Page<ChannelTicketOrderDaily> channelDailys = channelTicketOrderDailyService
				.searchChannelTicketOrderStat(searchModel);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dailys", channelDailys.getContents());
		map.put("startDate", getDateStr(searchModel.getStartDate()));
		map.put("endDate", getDateStr(searchModel.getEndDate()));
		List<String> listSheetNames = new ArrayList<String>();
		for (ChannelTicketOrderDaily daily : channelDailys.getContents()) {
			listSheetNames
					.add(filterExcelKeyWords(daily.getChannel().getName()));
		}
		map.put("list", getChannelCinemaStat(searchModel));
		ExcelData data = new ExcelData();
		String fileName = "channel_sale_report" + getCurrentDate() + ".xls";
		data.setTemplateFileName("channel_sale_report.xls");
		data.setSheetModels(channelDailys.getContents());
		data.setSheetNames(listSheetNames);
		data.setSheetModelName("dailys1");
		data.setOtherModel(map);
		data.setStartSheetNum(1);
		Excel excel = new Excel();
		excel.init(data);
		mergeCinemaAndChannelHeader(excel);
		setResponseHeader(response, fileName);
		excel.writeTo(response.getOutputStream());
	}

	/**
	 * 渠道影院销售统计。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 渠道影院销售统计列表。
	 */
	private List<Map<String, Object>> getChannelCinemaStat(
			StatSearchModel searchModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(searchModel.getChannelId())
				&& searchModel.getChannelId().contains(",")) {
			String[] channels = searchModel.getChannelId().split(",");
			for (String channel : channels) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				searchModel.setChannelId(channel);
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
}
