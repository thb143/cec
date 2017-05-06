package cn.mopon.cec.site.actions.system;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.service.Circuit;
import cn.mopon.cec.core.service.HomeService;
import cn.mopon.cec.core.service.StatService;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 首页统计维护。
 */
@Controller
@RequestMapping("/system")
@Auth("ADMIN")
public class StatAction {

	@Resource
	private MessageSource messageSource;
	@Resource
	private HomeService homeService;
	@Resource
	private StatService statService;
	@Resource
	private Circuit circuit;

	/**
	 * 统计维护视图。
	 * 
	 * @param model
	 *            模型
	 */
	@RequestMapping("stat-view")
	public void dayStatView(Model model) {
		if (new DateTime().getHourOfDay() < circuit.getDayStatTime()) {
			model.addAttribute("beforeDate", DateUtils.getPrevDay(2));
		} else {
			model.addAttribute("beforeDate", DateUtils.getPrevDay());
		}
	}

	/**
	 * 日结维护统计。
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 返回提示信息。
	 */
	@RequestMapping("dayStat-save")
	public ModelAndView dayStatSave(Date startDate, Date endDate) {
		statService.deleteExistStatDateOrder(startDate, endDate);
		List<String> failDays = statService.syncTicketOrderDetail(
				DateUtils.format(startDate), DateUtils.format(endDate));
		if (CollectionUtils.isNotEmpty(failDays)) {
			return DialogResultUtils.reload(messageSource.get(
					"stat.part.success", StringUtils.join(failDays, ",")));
		} else {
			return DialogResultUtils.reload(messageSource.get("stat.success"));
		}
	}

	/**
	 * 重置最高订单日
	 * 
	 * @return 返回提示信息。
	 */
	@RequestMapping("resetMaxOrderDay")
	public ModelAndView resertMaxOrderDay() {
		homeService.resetMaxOrderDay();
		return DialogResultUtils.reload(messageSource
				.get("max.orderday.reset.success"));
	}

	/**
	 * 销售趋势统计维护。
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return 返回提示信息。
	 */
	@RequestMapping("syncSaleStatDay")
	public ModelAndView syncSaleStatDay(String start, String end) {
		homeService.syncSaleStatDay(start, end);
		return DialogResultUtils.reload(messageSource
				.get("sync.sale.statday.success"));
	}

	/**
	 * 累计订单信息维护。
	 * 
	 * @return 返回提示信息。
	 */
	@RequestMapping("syncSaleStatAll")
	public ModelAndView syncSaleStatAll() {
		homeService.syncSaleStatAll();
		return DialogResultUtils.reload(messageSource
				.get("sync.sale.statall.success"));
	}

	/**
	 * 当天销售排行维护。
	 * 
	 * @return 返回提示信息。
	 */
	@RequestMapping("syncTodayRanks")
	public ModelAndView syncTodayRanks() {
		homeService.syncTodayRanks();
		return DialogResultUtils.reload(messageSource
				.get("sync.today.ranks.success"));
	}
}
