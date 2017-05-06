package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.TermRangeQuery;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.CinemaTicketOrderDaily;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderStat;
import cn.mopon.cec.core.enums.TicketOrderKind;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.model.DailyStatModel;
import cn.mopon.cec.core.task.DailyStatRegenTask;
import coo.base.model.Page;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.security.annotations.SimpleLog;

/**
 * 统计service。
 */
@Service
public class StatService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	private ExecutorService dailyStatRegenTaskExecutor;
	@Resource
	private Dao<TicketOrderStat> ticketOrderStatDao;
	@Resource
	private Dao<CinemaTicketOrderDaily> cinemaTicketOrderDailyDao;
	@Resource
	private Dao<ChannelTicketOrderDaily> channelTicketOrderDailyDao;
	@Resource
	private Dao<CinemaChannelTicketOrderDaily> cinemaChannelTicketOrderDailyDao;
	@Resource
	private Dao<Channel> channelDao;
	@Resource
	private Dao<Cinema> cinemaDao;
	@Resource
	private Dao<TicketOrder> ticketOrderDao;
	@Resource
	private Circuit circuit;

	/**
	 * 删除指定日期段已经存在的统计记录。
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 */
	public void deleteExistStatDateOrder(Date startDate, Date endDate) {
		do {
			deleteExistStatDateOrder(startDate);
			startDate = DateUtils.getNextDay(startDate);
		} while (!startDate.after(endDate));
	}

	/**
	 * 重新生成指定日期段选座票订单日统计记录。
	 * 
	 * @param startDateStr
	 *            开始日期
	 * @param endDateStr
	 *            结束日期
	 * @return 失败的日期。
	 */
	@SimpleLog(code = "ticketOrder.stat.sync.log", vars = { "startDateStr",
			"endDateStr" })
	public List<String> syncTicketOrderDetail(String startDateStr,
			String endDateStr) {
		Date startDate = DateUtils.parse(startDateStr);
		Date endDate = DateUtils.parse(endDateStr);
		List<DailyStatRegenTask> tasks = new ArrayList<>();
		List<String> failedDays = new ArrayList<>();
		try {
			while (!startDate.after(endDate)) {
				failedDays.add(DateUtils.format(startDate));
				tasks.add(new DailyStatRegenTask(startDate.getTime()));
				startDate = DateUtils.getNextDay(startDate);
			}
			for (Future<String> result : dailyStatRegenTaskExecutor
					.invokeAll(tasks)) {
				failedDays.remove(result.get());
			}
		} catch (Exception e) {
			log.error("批量日结维护统计时发生异常。", e);
		}
		return failedDays;
	}

	/**
	 * 生成指定日期选座票订单日统计记录。
	 * 
	 * @param date
	 *            日期
	 */
	@Transactional
	public void syncTicketOrderDetail(Date date) {
		DailyStatModel model = getDailyStatModel(date);
		saveDailyStat(model);
	}

	/**
	 * 保存统计结果。
	 * 
	 * @param model
	 *            日统计模型
	 */
	private void saveDailyStat(DailyStatModel model) {
		saveStatDateOrder(model);
		saveCinemaTicketOrderDaily(model);
		saveChannelTicketOrderDaily(model);
		saveCinemaChannelTicketOrderDaily(model);
	}

	/**
	 * 获取统计模型。
	 * 
	 * @param statDate
	 *            统计日期
	 * @return 返回统计模型。
	 */
	private DailyStatModel getDailyStatModel(Date statDate) {
		List<Cinema> cinemas = cinemaDao.searchAll();
		List<Channel> channels = channelDao.searchAll();
		DailyStatModel model = new DailyStatModel(channels, cinemas, statDate);
		List<TicketOrder> normalOrders = searchOrderByDate(
				TicketOrderKind.NORMAL, statDate);
		for (TicketOrder order : normalOrders) {
			model.addNormalOrder(order, statDate);
		}
		List<TicketOrder> revokeOrders = searchOrderByDate(
				TicketOrderKind.REVOKE, statDate);
		for (TicketOrder order : revokeOrders) {
			model.addRevokeOrder(order, statDate);
		}
		return model;
	}

	/**
	 * 保存订单明细统计。
	 * 
	 * @param model
	 *            日统计模型
	 */
	private void saveStatDateOrder(DailyStatModel model) {
		for (TicketOrderStat stat : model.getStatList()) {
			ticketOrderStatDao.merge(stat);
		}
	}

	/**
	 * 保存影院日统计。
	 * 
	 * @param model
	 *            日统计模型
	 */
	private void saveCinemaTicketOrderDaily(DailyStatModel model) {
		for (Map.Entry<String, CinemaTicketOrderDaily> entry : model
				.getCinemaDailyMap().entrySet()) {
			CinemaTicketOrderDaily daily = entry.getValue();
			if (CollectionUtils.isNotEmpty(daily.getShowTypeStat())) {
				cinemaTicketOrderDailyDao.merge(daily);
			}
		}
	}

	/**
	 * 保存渠道日统计。
	 * 
	 * @param model
	 *            日统计模型
	 */
	private void saveChannelTicketOrderDaily(DailyStatModel model) {
		for (Map.Entry<String, ChannelTicketOrderDaily> entry : model
				.getChannelDailyMap().entrySet()) {
			ChannelTicketOrderDaily daily = entry.getValue();
			if (CollectionUtils.isNotEmpty(daily.getShowTypeStat())) {
				channelTicketOrderDailyDao.merge(daily);
			}
		}
	}

	/**
	 * 保存影院、渠道日统计。
	 * 
	 * @param model
	 *            日统计模型
	 */
	private void saveCinemaChannelTicketOrderDaily(DailyStatModel model) {
		for (Map.Entry<String, CinemaChannelTicketOrderDaily> entry : model
				.getCinemaChannelDailyMap().entrySet()) {
			CinemaChannelTicketOrderDaily daily = entry.getValue();
			if (CollectionUtils.isNotEmpty(daily.getShowTypeStat())) {
				cinemaChannelTicketOrderDailyDao.merge(daily);
			}
		}
	}

	/**
	 * 删除指定日期已经存在的统计记录。
	 * 
	 * @param date
	 *            日期
	 */
	@Transactional
	public void deleteExistStatDateOrder(Date date) {
		deleteTicketOrderStat(date);
		deleteCinemaTicketOrderDaily(date);
		deleteChannelTicketOrderDaily(date);
		deleteCinemaChannelTicketOrderDaily(date);
	}

	/***
	 * 删除指定天的订单明细统计。
	 * 
	 * @param date
	 *            日期
	 */
	private void deleteTicketOrderStat(Date date) {
		FullTextCriteria criteria = ticketOrderStatDao.createFullTextCriteria();
		criteria.addFilterField("statDate",
				DateUtils.format(date, DateUtils.MILLISECOND_N));
		int pageNo = 1;
		Page<TicketOrderStat> statPage;
		do {
			statPage = ticketOrderStatDao.searchPage(criteria, pageNo, 1000);
			ticketOrderStatDao.remove(statPage.getContents());
			pageNo++;
		} while (!statPage.getLast());
	}

	/**
	 * 删除指定天的影院日统计。
	 * 
	 * @param date
	 *            日期
	 */
	private void deleteCinemaTicketOrderDaily(Date date) {
		FullTextCriteria criteria = cinemaTicketOrderDailyDao
				.createFullTextCriteria();
		criteria.addFilterField("statDate",
				DateUtils.format(date, DateUtils.MILLISECOND_N));
		cinemaTicketOrderDailyDao.remove(cinemaTicketOrderDailyDao
				.searchBy(criteria));
	}

	/**
	 * 删除指定天的渠道日统计。
	 * 
	 * @param date
	 *            日期
	 */
	private void deleteChannelTicketOrderDaily(Date date) {
		FullTextCriteria criteria = channelTicketOrderDailyDao
				.createFullTextCriteria();
		criteria.addFilterField("statDate",
				DateUtils.format(date, DateUtils.MILLISECOND_N));
		channelTicketOrderDailyDao.remove(channelTicketOrderDailyDao
				.searchBy(criteria));
	}

	/**
	 * 删除指定天的影院、渠道日统计。
	 * 
	 * @param date
	 *            日期
	 */
	private void deleteCinemaChannelTicketOrderDaily(Date date) {
		FullTextCriteria criteria = cinemaChannelTicketOrderDailyDao
				.createFullTextCriteria();
		criteria.addFilterField("statDate",
				DateUtils.format(date, DateUtils.MILLISECOND_N));
		cinemaChannelTicketOrderDailyDao
				.remove(cinemaChannelTicketOrderDailyDao.searchBy(criteria));
	}

	/**
	 * 获取指定日期的订单。
	 * 
	 * @param kind
	 *            订单类别
	 * @param statDate
	 *            统计日期
	 * @return 返回对应订单类别的订单。
	 */
	private List<TicketOrder> searchOrderByDate(TicketOrderKind kind,
			Date statDate) {
		Date formatDate = DateUtils.parse(DateUtils.format(statDate));
		Date startDate = new DateTime(formatDate).plusHours(
				circuit.getDayStatTime()).toDate();
		Date endDate = new DateTime(startDate).plusHours(24).toDate();
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		if (kind == TicketOrderKind.NORMAL) {
			TermRangeQuery query = getDateRangeQuery("confirmTime", startDate,
					endDate);
			criteria.addLuceneQuery(query, BooleanClause.Occur.MUST);
			criteria.addFilterField("status",
					TicketOrderStatus.SUCCESS.getValue());
		} else if (kind == TicketOrderKind.REVOKE) {
			TermRangeQuery query = getDateRangeQuery("revokeTime", startDate,
					endDate);
			criteria.addLuceneQuery(query, BooleanClause.Occur.MUST);
			criteria.addFilterField("status",
					TicketOrderStatus.REVOKED.getValue());
		}
		int pageNo = 1;
		Page<TicketOrder> orderPage;
		List<TicketOrder> result = new ArrayList<>();
		do {
			orderPage = ticketOrderDao.searchPage(criteria, pageNo, 1000);
			pageNo++;
			result.addAll(orderPage.getContents());
		} while (!orderPage.getLast());
		return result;
	}

	/**
	 * 获取日期查询区间query。
	 * 
	 * @param field
	 *            字段名称
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 查询区间query
	 */
	private TermRangeQuery getDateRangeQuery(String field, Date startDate,
			Date endDate) {
		return TermRangeQuery
				.newStringRange(field,
						DateUtils.format(startDate, DateUtils.MILLISECOND_N),
						DateUtils.format(endDate, DateUtils.MILLISECOND_N),
						true, false);
	}

}
