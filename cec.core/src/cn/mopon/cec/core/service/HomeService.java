package cn.mopon.cec.core.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeQuery;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.ChannelPolicyLog;
import cn.mopon.cec.core.entity.ChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.StatDim;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.enums.TicketOrderType;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.HomeModel;
import cn.mopon.cec.core.model.HomeModel.BusinessOverviewModel;
import cn.mopon.cec.core.model.HomeModel.SalesChartListModel;
import cn.mopon.cec.core.model.HomeModel.SalesChartModel;
import cn.mopon.cec.core.model.HomeModel.TodayRanksModel;
import cn.mopon.cec.core.model.HomeModel.TodoListModel;
import cn.mopon.cec.core.model.StatSearchModel;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Line;

import coo.base.model.Page;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.security.annotations.SimpleLog;

/**
 * 首页管理。
 */
@Service
public class HomeService {
	@Resource
	private Dao<Cinema> cinemaDao;
	@Resource
	private Dao<Channel> channelDao;
	@Resource
	private Dao<CinemaPolicy> cinemaPolicyDao;
	@Resource
	private Dao<ChannelPolicy> channelPolicyDao;
	@Resource
	private Dao<SpecialPolicy> specialPolicyDao;
	@Resource
	private Dao<CinemaPolicyLog> cinemaPolicyLogDao;
	@Resource
	private Dao<ChannelPolicyLog> channelPolicyLogDao;
	@Resource
	private Dao<SpecialPolicyLog> specialPolicyLogDao;
	@Resource
	private Dao<TicketOrder> ticketOrderDao;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Double> redisHashStat;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> redisStat;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource
	private ChannelTicketOrderDailyService channelTicketOrderDailyService;
	@Resource
	private Circuit circuit;

	/** 销售排行统计前缀 */
	private static final String RANKS = "RANKS";
	private static final String SALE_STAT_ALL = "SALE_STAT_ALL";
	private static final String SALE_STAT_DAY = "SALE_STAT_DAY";
	private static final String MAX_ORDER_DAY = "MAX_ORDER_COUNT_DAY";

	/**
	 * 获取业务概览模型。
	 * 
	 * @return 返回业务概览模型。
	 */
	@Transactional(readOnly = true)
	public BusinessOverviewModel getBusinessOverviewModel() {
		BusinessOverviewModel model = new HomeModel().new BusinessOverviewModel();
		model.setCinemaCount(cinemaDao.count());
		model.setChannelCount(channelDao.count());

		model.setOrderCount(getInt(SALE_STAT_ALL, StatDim.ORDER.getValue()));
		model.setTicketCount(getInt(SALE_STAT_ALL, StatDim.TICKET.getValue()));
		model.setOrderAmount(getDouble(SALE_STAT_ALL, StatDim.AMOUNT.getValue()));

		model.setTodayOrderCount(getInt(SALE_STAT_DAY,
				getDayStatField(StatDim.ORDER)));
		model.setTodayTicketCount(getInt(SALE_STAT_DAY,
				getDayStatField(StatDim.TICKET)));
		model.setTodayOrderAmount(getDouble(SALE_STAT_DAY,
				getDayStatField(StatDim.AMOUNT)));
		String maxDate = redisStat.get(MAX_ORDER_DAY);
		if (StringUtils.isNotEmpty(maxDate)) {
			model.setTopOrderDate(DateUtils.parse(redisStat.get(MAX_ORDER_DAY),
					DateUtils.DAY));
			model.setTopOrderCount(getInt(SALE_STAT_DAY, maxDate + "_"
					+ StatDim.ORDER.getValue()));
		}
		return model;
	}

	/**
	 * 获取待办事项模型。
	 * 
	 * @return 返回待办事项模型。
	 */
	@Transactional(readOnly = true)
	public TodoListModel getTodoListModel() {
		TodoListModel model = new HomeModel().new TodoListModel();
		model.setAuditCinemaPolicyCount(countCinemaPolicy(AuditStatus.AUDIT));
		model.setApproveCinemaPolicyCount(countCinemaPolicy(AuditStatus.APPROVE));
		model.setAuditChannelPolicyCount(countChannelPolicy(AuditStatus.AUDIT));
		model.setApproveChannelPolicyCount(countChannelPolicy(AuditStatus.APPROVE));
		model.setAuditSpecialPolicyCount(countSpecialPolicy(AuditStatus.AUDIT));
		model.setApproveSpecialPolicyCount(countSpecialPolicy(AuditStatus.APPROVE));
		model.setWillExpireCinemaPolicys(searchWillExpiredCinemaPolicys(30));
		model.setWillExpireChannelPolicys(searchWillExpireChannelPolicy(30));
		model.setWillExpireSpecialPolicys(searchWillExpireSpecialPolicy(30));
		return model;
	}

	/**
	 * 获取销售趋势模型。
	 * 
	 * @return 返回销售趋势模型。
	 */
	@Transactional(readOnly = true)
	public Option getSalesChartOption() {
		Option option = new Option();
		option.title("近30天销售趋势", "单位（元）");
		option.tooltip(new Tooltip().trigger(Trigger.axis).formatter(
				"{b}<br/>{a}：{c}"));
		option.legend("订单金额");
		option.toolbox()
				.feature(Tool.dataView, new MagicType(Magic.line, Magic.bar),
						Tool.restore, Tool.saveAsImage).show(true);
		option.calculable(true);

		CategoryAxis categoryAxis = new CategoryAxis();
		categoryAxis.boundaryGap(false);
		SalesChartListModel model = getSalesCharModel();

		categoryAxis.data(model.getDays());
		option.xAxis(categoryAxis);

		ValueAxis valueAxis = new ValueAxis();
		valueAxis.axisLabel().formatter("{value} 元");
		option.yAxis(valueAxis).animation();

		Line line = new Line();
		line.smooth(true).name("订单金额").data(model.getAmounts());
		option.series(line);
		return option;
	}

	/**
	 * 获取今日排行模型。
	 * 
	 * @return 返回今日排行模型。
	 */
	@Transactional(readOnly = true)
	public TodayRanksModel getTodayRanksModel() {
		TodayRanksModel model = new HomeModel().new TodayRanksModel();
		String key = getRanksCacheKey();
		Set<String> fields = redisHashStat.keys(key);
		for (String field : fields) {
			model.addRank(field, getDouble(key, field));
		}
		return model;
	}

	/**
	 * 累增订单统计。
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	@Transactional(readOnly = true)
	public void incrTicketOrderStat(TicketOrder ticketOrder) {
		incrTodayRanks(ticketOrder);
		incrSaleStatAll(ticketOrder);
		incrSaleStatDay(ticketOrder);
	}

	/**
	 * 统计日期段的销售趋势。
	 * 
	 * @param startDateStr
	 *            开始日期
	 * @param endDateStr
	 *            结束日期
	 */
	@Transactional
	@SimpleLog(code = "sale.stat.day.log", vars = { "startDateStr",
			"endDateStr" })
	public void syncSaleStatDay(String startDateStr, String endDateStr) {
		Date startDate = DateUtils.parse(startDateStr);
		Date endDate = DateUtils.parse(endDateStr);
		if (startDate.before(DateUtils.getToday())) {
			DateTime start = new DateTime(startDate);
			DateTime end = new DateTime(endDate);
			do {
				doSyncSaleStatDay(start.toDate());
				start = start.plusDays(1);
			} while (!start.isAfter(end)
					&& start.isBefore(new DateTime(DateUtils.getToday())));
		}
	}

	/**
	 * 总订单信息维护。
	 */
	@Transactional
	@SimpleLog(code = "sale.stat.all.log")
	public void syncSaleStatAll() {
		redisHashStat.delete(SALE_STAT_ALL, StatDim.ORDER.getValue());
		redisHashStat.delete(SALE_STAT_ALL, StatDim.TICKET.getValue());
		redisHashStat.delete(SALE_STAT_ALL, StatDim.AMOUNT.getValue());
		SalesChartModel model = getAllStatFromOrderDaily();
		redisHashStat.increment(SALE_STAT_ALL, StatDim.ORDER.getValue(), model
				.getOrderCount().doubleValue());
		redisHashStat.increment(SALE_STAT_ALL, StatDim.TICKET.getValue(), model
				.getTicketCount().doubleValue());
		redisHashStat.increment(SALE_STAT_ALL, StatDim.AMOUNT.getValue(),
				model.getOrderAmount());
		// 再累加当天的
		Iterator<TicketOrder> orders = getTodayTicketOrders();
		while (orders.hasNext()) {
			TicketOrder order = orders.next();
			redisHashStat
					.increment(SALE_STAT_ALL, StatDim.ORDER.getValue(), 1D);
			redisHashStat.increment(SALE_STAT_ALL, StatDim.TICKET.getValue(),
					order.getTicketCount().doubleValue());
			redisHashStat.increment(SALE_STAT_ALL, StatDim.AMOUNT.getValue(),
					order.getAmount());
		}
	}

	/**
	 * 重置最高订单日。
	 */
	@Transactional
	@SimpleLog(code = "sale.stat.max.log")
	public void resetMaxOrderDay() {
		String maxDate = DateUtils.format(getConfirmDate(new Date()));
		Double maxAmount = 0D;
		Set<String> fields = redisHashStat.keys(SALE_STAT_DAY);
		for (String field : fields) {
			String[] farr = field.split("_");
			if (farr[1].contains(StatDim.ORDER.getValue())) {
				Double value = getDouble(SALE_STAT_DAY, field);
				if (value >= maxAmount) {
					maxDate = farr[0];
					maxAmount = value;
				}
			}
		}
		redisStat.set(MAX_ORDER_DAY, maxDate);
	}

	/**
	 * 维护当天销售排行。
	 */
	@Transactional
	@SimpleLog(code = "sale.stat.today.log")
	public void syncTodayRanks() {
		Iterator<TicketOrder> orders = getTodayTicketOrders();
		redisTemplate.delete(getRanksCacheKey());
		redisHashStat.delete(SALE_STAT_DAY,
				getDayStatField(new Date(), StatDim.ORDER));
		redisHashStat.delete(SALE_STAT_DAY,
				getDayStatField(new Date(), StatDim.TICKET));
		redisHashStat.delete(SALE_STAT_DAY,
				getDayStatField(new Date(), StatDim.AMOUNT));
		while (orders.hasNext()) {
			TicketOrder order = orders.next();
			incrTodayRanks(order);
			incrSaleStatDay(order);
		}
	}

	/**
	 * 根据日结时间，获取订单日期。
	 * 
	 * @param date
	 *            确认订单时间
	 * 
	 * @return 日结日期。
	 */
	private Date getConfirmDate(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.minusHours(circuit.getDayStatTime()).toDate();
	}

	/**
	 * 获取当天的订单。
	 * 
	 * @return 订单列表。
	 */
	private Iterator<TicketOrder> getTodayTicketOrders() {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		Date startDate = new DateTime(DateUtils.getToday()).plusHours(
				circuit.getDayStatTime()).toDate();
		Date endDate = new DateTime(startDate).plusHours(24).toDate();
		TermRangeQuery query = TermRangeQuery.newStringRange("confirmTime",
				DateUtils.format(startDate, DateUtils.MILLISECOND_N),
				DateUtils.format(endDate, DateUtils.MILLISECOND_N), true, true);
		criteria.addLuceneQuery(query, BooleanClause.Occur.MUST);
		criteria.addFilterField("status", TicketOrderStatus.SUCCESS.getValue(),
				TicketOrderStatus.REVOKED.getValue());
		return ticketOrderDao.searchByLazy(criteria);
	}

	/**
	 * 从日统计表获取总订单信息。
	 * 
	 * @return 模型。
	 */
	private SalesChartModel getAllStatFromOrderDaily() {
		Integer orderCount = 0;
		Integer ticketCount = 0;
		Double orderAmount = 0D;
		StatSearchModel searchModel = new StatSearchModel();
		searchModel.setPageSize(Integer.MAX_VALUE);
		Page<ChannelTicketOrderDaily> pages = channelTicketOrderDailyService
				.searchChannelTicketOrderStat(searchModel);
		for (ChannelTicketOrderDaily daily : pages.getContents()) {
			orderCount += daily.getNormalOrderStat().getOrderCount();
			ticketCount += daily.getNormalOrderStat().getTicketCount();
			orderAmount = NumberUtils.add(orderAmount, daily
					.getNormalOrderStat().getAmount());
		}
		SalesChartModel model = new HomeModel().new SalesChartModel();
		model.setOrderCount(orderCount);
		model.setTicketCount(ticketCount);
		model.setOrderAmount(orderAmount);
		return model;
	}

	/**
	 * 处理单天的日统计数据。
	 * 
	 * @param start
	 *            日期
	 */
	private void doSyncSaleStatDay(Date start) {
		String statDate = DateUtils.format(start);
		redisHashStat.delete(SALE_STAT_DAY,
				getDayStatField(statDate, StatDim.ORDER));
		redisHashStat.delete(SALE_STAT_DAY,
				getDayStatField(statDate, StatDim.TICKET));
		redisHashStat.delete(SALE_STAT_DAY,
				getDayStatField(statDate, StatDim.AMOUNT));
		ChannelTicketOrderDaily daily = channelTicketOrderDailyService
				.searchTicketOrderStatForDay(start);
		redisHashStat.increment(SALE_STAT_DAY,
				getDayStatField(statDate, StatDim.ORDER), daily
						.getNormalOrderStat().getOrderCount().doubleValue());
		redisHashStat.increment(SALE_STAT_DAY,
				getDayStatField(statDate, StatDim.TICKET), daily
						.getNormalOrderStat().getTicketCount().doubleValue());
		redisHashStat.increment(SALE_STAT_DAY,
				getDayStatField(statDate, StatDim.AMOUNT), daily
						.getNormalOrderStat().getAmount());
	}

	/**
	 * 获取销售图模型列表。
	 * 
	 * @return 销售图模型列表。
	 */
	private SalesChartListModel getSalesCharModel() {
		SalesChartListModel model = new HomeModel().new SalesChartListModel();
		Set<String> keys = redisHashStat.keys(SALE_STAT_DAY);
		for (String key : keys) {
			model.addStat(key, getDouble(SALE_STAT_DAY, key));
		}

		return model;
	}

	/**
	 * 获取long 类型的递增值。
	 * 
	 * @param key
	 *            缓存Key
	 * @param field
	 *            缓存Field
	 * @return 值
	 */
	private Long getLong(String key, String field) {
		return redisHashStat.increment(key, field, 0D).longValue();
	}

	/**
	 * 获取Integer 类型的递增值。
	 * 
	 * @param key
	 *            缓存Key
	 * @param field
	 *            缓存Field
	 * @return 值
	 */
	private Integer getInt(String key, String field) {
		return getLong(key, field).intValue();
	}

	/**
	 * 获取double 类型的递增值。
	 * 
	 * @param key
	 *            缓存Key
	 * @param field
	 *            缓存Field
	 * @return 值
	 */
	private Double getDouble(String key, String field) {
		return redisHashStat.increment(key, field, 0D);
	}

	/**
	 * 增加天销售统计。
	 * 
	 * @param order
	 *            订单
	 */
	private void incrSaleStatDay(TicketOrder order) {
		long orderCount = redisHashStat.increment(SALE_STAT_DAY,
				getDayStatField(order.getConfirmTime(), StatDim.ORDER), 1D)
				.longValue();
		redisHashStat.increment(SALE_STAT_DAY,
				getDayStatField(order.getConfirmTime(), StatDim.TICKET), order
						.getTicketCount().doubleValue());
		redisHashStat.increment(SALE_STAT_DAY,
				getDayStatField(order.getConfirmTime(), StatDim.AMOUNT),
				order.getAmount());

		// 判断最高订单日
		setMaxOrderDay(orderCount, order.getConfirmTime());
	}

	/**
	 * 设置最高订单日。
	 * 
	 * @param orderCount
	 *            订单总数
	 * @param date
	 *            日期
	 */
	private void setMaxOrderDay(long orderCount, Date date) {
		String day = redisStat.get(MAX_ORDER_DAY);
		if (StringUtils.isEmpty(day)) {
			redisStat.set(MAX_ORDER_DAY,
					DateUtils.format(getConfirmDate(new Date())));
		} else {
			long maxOrderCount = getLong(SALE_STAT_DAY,
					getDayStatField(day, StatDim.ORDER));
			if (orderCount >= maxOrderCount) {
				redisStat.set(MAX_ORDER_DAY,
						DateUtils.format(getConfirmDate(date)));
			}
		}
	}

	/**
	 * 累增总订单统计。
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	private void incrSaleStatAll(TicketOrder ticketOrder) {
		redisHashStat.increment(SALE_STAT_ALL, StatDim.ORDER.getValue(), 1D);
		redisHashStat.increment(SALE_STAT_ALL, StatDim.TICKET.getValue(),
				ticketOrder.getTicketCount().doubleValue());
		redisHashStat.increment(SALE_STAT_ALL, StatDim.AMOUNT.getValue(),
				ticketOrder.getAmount());
	}

	/**
	 * 累增销售排行数据
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	private void incrTodayRanks(TicketOrder ticketOrder) {
		incrCinemaTodayRanks(ticketOrder);
		incrChannelTodayRanks(ticketOrder);
		incrFilmTodayRanks(ticketOrder);
		incrSpecialPolicyTodayRanks(ticketOrder);
	}

	/**
	 * 累增影院销售排行数据。
	 * 
	 * @param order
	 *            订单
	 */
	private void incrCinemaTodayRanks(TicketOrder order) {
		Cinema cinema = order.getCinema();
		String ranksCacheKey = getRanksCacheKey(order);
		redisHashStat.increment(ranksCacheKey,
				getCinemaRanksField(cinema, StatDim.ORDER), 1D);
		redisHashStat.increment(ranksCacheKey,
				getCinemaRanksField(cinema, StatDim.TICKET), order
						.getTicketCount().doubleValue());
		redisHashStat.increment(ranksCacheKey,
				getCinemaRanksField(cinema, StatDim.AMOUNT), order.getAmount());
	}

	/**
	 * 累增渠道销售排行数据。
	 * 
	 * @param order
	 *            订单
	 */
	private void incrChannelTodayRanks(TicketOrder order) {
		Channel channel = order.getChannel();
		String ranksCacheKey = getRanksCacheKey(order);
		redisHashStat.increment(ranksCacheKey,
				getChannelRanksField(channel, StatDim.ORDER), 1D);
		redisHashStat.increment(ranksCacheKey,
				getChannelRanksField(channel, StatDim.TICKET), order
						.getTicketCount().doubleValue());
		redisHashStat.increment(ranksCacheKey,
				getChannelRanksField(channel, StatDim.AMOUNT),
				order.getAmount());
	}

	/**
	 * 累增影片销售排行数据。
	 * 
	 * @param order
	 *            订单
	 */
	private void incrFilmTodayRanks(TicketOrder order) {
		Film film = order.getFilm();
		String ranksCacheKey = getRanksCacheKey(order);
		redisHashStat.increment(ranksCacheKey,
				getFilmRanksField(film, StatDim.ORDER), 1D);
		redisHashStat.increment(ranksCacheKey,
				getFilmRanksField(film, StatDim.TICKET), order.getTicketCount()
						.doubleValue());
		redisHashStat.increment(ranksCacheKey,
				getFilmRanksField(film, StatDim.AMOUNT), order.getAmount());
	}

	/**
	 * 累增特殊定价销售排行数据。
	 * 
	 * @param order
	 *            订单
	 */
	private void incrSpecialPolicyTodayRanks(TicketOrder order) {
		if (order.getType() != TicketOrderType.SPECIAL) {
			return;
		}
		SpecialPolicy specialPolicy = order.getSpecialRule().getPolicy();
		String ranksCacheKey = getRanksCacheKey(order);
		redisHashStat.increment(ranksCacheKey,
				getSpecialPolicyRanksField(specialPolicy, StatDim.ORDER), 1D);
		redisHashStat.increment(ranksCacheKey,
				getSpecialPolicyRanksField(specialPolicy, StatDim.TICKET),
				order.getTicketCount().doubleValue());
		redisHashStat.increment(ranksCacheKey,
				getSpecialPolicyRanksField(specialPolicy, StatDim.AMOUNT),
				order.getAmount());
		// 计算渠道补贴金额。
		double channelSettlePrice = NumberUtils.add(order.getSubmitAmount(),
				order.getConnectFee());
		double channelSubsidyFee = NumberUtils.sub(channelSettlePrice,
				order.getTicketAmount());
		// 如果渠道补贴金额大于0，则累记到redis中。
		if (channelSubsidyFee > 0) {
			redisHashStat.increment(
					ranksCacheKey,
					getSpecialPolicyRanksField(specialPolicy,
							StatDim.CHANNELSUBSIDYORDER), 1D);
			redisHashStat.increment(
					ranksCacheKey,
					getSpecialPolicyRanksField(specialPolicy,
							StatDim.CHANNELSUBSIDYTICKET), order
							.getTicketCount().doubleValue());
			redisHashStat.increment(
					ranksCacheKey,
					getSpecialPolicyRanksField(specialPolicy,
							StatDim.CHANNELSUBSIDYAMOUNT), channelSubsidyFee);
		}
	}

	/**
	 * 获取销售排行redis key。
	 * 
	 * @return 销售排行的缓存key 。
	 */
	private String getRanksCacheKey() {
		return RANKS + "_" + DateUtils.format(getConfirmDate(new Date()));
	}

	/**
	 * 获取销售排行redis key。
	 * 
	 * @param order
	 *            订单
	 * 
	 * @return 销售排行的缓存key 。
	 */
	private String getRanksCacheKey(TicketOrder order) {
		return RANKS + "_"
				+ DateUtils.format(getConfirmDate(order.getConfirmTime()));
	}

	/**
	 * 获取日统计field
	 * 
	 * @param date
	 *            日期
	 * @param dim
	 *            统计维度
	 * @return field
	 */
	private String getDayStatField(Date date, StatDim dim) {
		return DateUtils.format(getConfirmDate(date)) + "_" + dim.getValue();
	}

	/**
	 * 获取日统计field
	 * 
	 * @param date
	 *            日期
	 * @param dim
	 *            统计维度
	 * @return field
	 */
	private String getDayStatField(String date, StatDim dim) {
		return date + "_" + dim.getValue();
	}

	/**
	 * 获取日统计field
	 * 
	 * @param dim
	 *            统计维度
	 * @return field
	 */
	private String getDayStatField(StatDim dim) {
		return getDayStatField(new Date(), dim);
	}

	/**
	 * 获取影院销售排行的缓存field。
	 * 
	 * @param cinema
	 *            影院
	 * @param dim
	 *            统计维度
	 * @return 影院销售排行的缓存field。
	 */
	private String getCinemaRanksField(Cinema cinema, StatDim dim) {
		return "CIN_" + cinema.getId() + "_" + cinema.getName() + "_"
				+ dim.getValue();
	}

	/**
	 * 获取渠道销售排行的缓存field。
	 * 
	 * @param channel
	 *            渠道
	 * @param dim
	 *            统计维度
	 * @return 影院销售排行的缓存field。
	 */
	private String getChannelRanksField(Channel channel, StatDim dim) {
		return "CHA_" + channel.getId() + "_" + channel.getName() + "_"
				+ dim.getValue();
	}

	/**
	 * 获取影片销售排行的缓存field。
	 * 
	 * @param film
	 *            影片
	 * @param dim
	 *            统计维度
	 * @return 影院销售排行的缓存field。
	 */
	private String getFilmRanksField(Film film, StatDim dim) {
		return "FIL_" + film.getId() + "_" + film.getName() + "_"
				+ dim.getValue();
	}

	/**
	 * 获取特殊策略销售排行的缓存field。
	 * 
	 * @param specialPolicy
	 *            特殊策略
	 * @param dim
	 *            统计维度
	 * @return 影院销售排行的缓存field。
	 */
	private String getSpecialPolicyRanksField(SpecialPolicy specialPolicy,
			StatDim dim) {
		return "SPO_" + specialPolicy.getId() + "_" + specialPolicy.getName()
				+ "_" + dim.getValue();
	}

	/**
	 * 统计指定状态的影院策略数量。
	 * 
	 * @param status
	 *            策略状态
	 * @return 返回指定状态的影院策略数量。
	 */
	private Integer countCinemaPolicy(AuditStatus status) {
		FullTextCriteria criteria = cinemaPolicyLogDao.createFullTextCriteria();
		criteria.addFilterField("status", status.getValue());
		return cinemaPolicyLogDao.count(criteria);
	}

	/**
	 * 统计指定状态的渠道策略数量。
	 * 
	 * @param status
	 *            策略状态
	 * @return 返回指定状态的渠道策略数量。
	 */
	private Integer countChannelPolicy(AuditStatus status) {
		FullTextCriteria criteria = channelPolicyLogDao
				.createFullTextCriteria();
		criteria.addFilterField("status", status.getValue());
		return cinemaPolicyLogDao.count(criteria);
	}

	/**
	 * 统计指定状态的特价策略数量。
	 * 
	 * @param status
	 *            策略状态
	 * @return 返回指定状态的特价策略数量。
	 */
	private Integer countSpecialPolicy(AuditStatus status) {
		FullTextCriteria criteria = specialPolicyLogDao
				.createFullTextCriteria();
		criteria.addFilterField("status", status.getValue());
		return cinemaPolicyLogDao.count(criteria);
	}

	/**
	 * 全文搜索将在指定天数内过期的影院结算策略。
	 * 
	 * @param days
	 *            天数
	 * @return 返回将在指定天数内过期的影院结算策略。
	 */
	private List<CinemaPolicy> searchWillExpiredCinemaPolicys(Integer days) {
		FullTextCriteria criteria = cinemaPolicyDao.createFullTextCriteria();
		setWillExpirePolicyCondition(criteria, days);
		return cinemaPolicyDao.searchBy(criteria);
	}

	/**
	 * 全文搜索将在指定天数内过期的渠道结算策略。
	 * 
	 * @param days
	 *            天数
	 * @return 返回将在指定天数内过期的渠道结算策略。
	 */
	private List<ChannelPolicy> searchWillExpireChannelPolicy(Integer days) {
		FullTextCriteria criteria = channelPolicyDao.createFullTextCriteria();
		setWillExpirePolicyCondition(criteria, days);
		return channelPolicyDao.searchBy(criteria);
	}

	/**
	 * 全文搜索将在指定天数内停用的特价结算策略。
	 * 
	 * @param days
	 *            天数
	 * @return 返回将在指定天数内停用的特价结算策略。
	 */
	private List<SpecialPolicy> searchWillExpireSpecialPolicy(Integer days) {
		FullTextCriteria criteria = specialPolicyDao.createFullTextCriteria();
		setWillExpirePolicyCondition(criteria, days);
		return specialPolicyDao.searchBy(criteria);
	}

	/**
	 * 设置即将到期策略搜索条件。
	 * 
	 * @param criteria
	 *            搜索条件
	 * @param days
	 *            天数
	 */
	private void setWillExpirePolicyCondition(FullTextCriteria criteria,
			Integer days) {
		String lowerDate = DateUtils.format(DateUtils.getToday(),
				DateUtils.MILLISECOND_N);
		String upperDate = DateUtils.format(DateUtils.getNextDay(days),
				DateUtils.MILLISECOND_N);
		criteria.addRangeField("endDate", lowerDate, upperDate);
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		criteria.addSortAsc("endDate", SortField.Type.STRING);
	}
}
