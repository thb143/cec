package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaChannelTicketOrderDaily;
import cn.mopon.cec.core.entity.CinemaTicketOrderDaily;
import cn.mopon.cec.core.entity.DailyStatEntity;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderStat;
import cn.mopon.cec.core.enums.TicketOrderKind;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;

/**
 * 日统计模型。
 */
public class DailyStatModel {
	private Date statDate;
	/** 订单明细集合 */
	private List<TicketOrderStat> statList = new ArrayList<>();
	/** 影院日统计 */
	private Map<String, CinemaTicketOrderDaily> cinemaDailyMap = new HashMap<>();
	/** 渠道日统计 */
	private Map<String, ChannelTicketOrderDaily> channelDailyMap = new HashMap<>();
	/** 影院渠道日统计 */
	private Map<String, CinemaChannelTicketOrderDaily> cinemaChannelDailyMap = new HashMap<>();

	/**
	 * 构造方法。
	 * 
	 * @param channels
	 *            渠道列表
	 * @param cinemas
	 *            影院列表
	 * @param date
	 *            统计日期
	 */
	public DailyStatModel(List<Channel> channels, List<Cinema> cinemas,
			Date date) {
		this.statDate = date;
		for (Channel channel : channels) {
			channelDailyMap.put(channel.getId(), new ChannelTicketOrderDaily(
					channel, date));
		}
		for (Cinema cinema : cinemas) {
			cinemaDailyMap.put(cinema.getId(), new CinemaTicketOrderDaily(
					cinema, date));
		}
		for (Channel channel : channels) {
			for (Cinema cinema : cinemas) {
				String key = channel.getId() + "_" + cinema.getId();
				cinemaChannelDailyMap
						.put(key, new CinemaChannelTicketOrderDaily(cinema,
								channel, date));
			}
		}
	}

	/**
	 * 添加正常订单。
	 * 
	 * @param order
	 *            订单
	 * @param date
	 *            统计日期
	 */
	public void addNormalOrder(TicketOrder order, Date date) {
		TicketOrderStat stat = new TicketOrderStat(order,
				TicketOrderKind.NORMAL, date);
		addTicketOrderStat(stat);
	}

	/**
	 * 添加退票订单。
	 * 
	 * @param order
	 *            订单
	 * @param date
	 *            统计日期
	 */
	public void addRevokeOrder(TicketOrder order, Date date) {
		TicketOrderStat stat = new TicketOrderStat(order,
				TicketOrderKind.REVOKE, date);
		addTicketOrderStat(stat);

		String confirmDate = DateUtils.format(order.getConfirmTime());
		String revokeDate = DateUtils.format(order.getRevokeTime());
		// 当天出票成功且退票的订单，记录一条正常，并记录一条退票的。
		if (confirmDate.equals(revokeDate)) {
			addNormalOrder(order, date);
		}
	}

	/**
	 * 添加统计明细。
	 * 
	 * @param stat
	 *            统计明细对象。
	 */
	private void addTicketOrderStat(TicketOrderStat stat) {
		statList.add(stat);
		String cinemaId = stat.getCinema().getId();
		String channelId = stat.getChannel().getId();
		if (stat.getKind() == TicketOrderKind.NORMAL) {
			doNormal(stat, cinemaDailyMap.get(cinemaId));
			doNormal(stat, channelDailyMap.get(channelId));
			doNormal(stat,
					cinemaChannelDailyMap.get(channelId + "_" + cinemaId));
		} else {
			doRevoke(stat, cinemaDailyMap.get(cinemaId));
			doRevoke(stat, channelDailyMap.get(channelId));
			doRevoke(stat,
					cinemaChannelDailyMap.get(channelId + "_" + cinemaId));
		}
	}

	/**
	 * 处理正常订单。
	 * 
	 * @param orderStat
	 *            正常订单明细
	 * @param orderDaily
	 *            日统计
	 */
	private void doNormal(TicketOrderStat orderStat, DailyStatEntity orderDaily) {
		doNormalOrderStat(orderStat, orderDaily);
		doNormalShowTypeStat(orderStat, orderDaily);
	}

	/**
	 * 处理退票订单。
	 * 
	 * @param orderStat
	 *            退票订单明细
	 * @param orderDaily
	 *            日统计
	 */
	private void doRevoke(TicketOrderStat orderStat, DailyStatEntity orderDaily) {
		doRevokeOrderStat(orderStat, orderDaily);
		doRevokeShowTypeStat(orderStat, orderDaily);
	}

	/**
	 * 处理正常订单统计。
	 * 
	 * @param order
	 *            订单对象
	 * @param orderDaily
	 *            渠道统计日统计对象
	 */
	private void doNormalOrderStat(TicketOrderStat order,
			DailyStatEntity orderDaily) {
		NormalOrderStat normal = orderDaily.getNormalOrderStat();
		normal.setOrderCount(normal.getOrderCount() + 1);
		normal.setTicketCount(normal.getTicketCount() + order.getTicketCount());
		normal.setCinemaAmount(NumberUtils.add(normal.getCinemaAmount(),
				order.getCinemaAmount()));
		normal.setChannelAmount(NumberUtils.add(normal.getChannelAmount(),
				order.getChannelAmount()));
		normal.setServiceFee(NumberUtils.add(normal.getServiceFee(),
				order.getServiceFee()));
		normal.setChannelFee(NumberUtils.add(normal.getChannelFee(),
				order.getChannelFee()));
		normal.setCircuitFee(NumberUtils.add(normal.getCircuitFee(),
				order.getCircuitFee()));
		normal.setSubsidyFee(NumberUtils.add(normal.getSubsidyFee(),
				order.getSubsidyFee()));
		normal.setAmount(NumberUtils.add(normal.getAmount(), order.getAmount()));
		normal.setSubmitAmount(NumberUtils.add(normal.getSubmitAmount(),
				order.getSubmitAmount()));
		normal.setConnectFee(NumberUtils.add(normal.getConnectFee(),
				order.getConnectFee()));
		orderDaily.setNormalOrderStat(normal);
	}

	/**
	 * 处理退票订单统计。
	 * 
	 * @param order
	 *            订单对象
	 * @param orderDaily
	 *            渠道统计日统计对象
	 */
	private void doRevokeOrderStat(TicketOrderStat order,
			DailyStatEntity orderDaily) {
		RevokeOrderStat stat = orderDaily.getRefundOrderStat();
		stat.setOrderCount(stat.getOrderCount() + 1);
		stat.setTicketCount(stat.getTicketCount() + order.getTicketCount());
		stat.setCinemaAmount(NumberUtils.add(stat.getCinemaAmount(),
				order.getCinemaAmount()));
		stat.setChannelAmount(NumberUtils.add(stat.getChannelAmount(),
				order.getChannelAmount()));
		stat.setServiceFee(NumberUtils.add(stat.getServiceFee(),
				order.getServiceFee()));
		stat.setChannelFee(NumberUtils.add(stat.getChannelFee(),
				order.getChannelFee()));
		stat.setCircuitFee(NumberUtils.add(stat.getCircuitFee(),
				order.getCircuitFee()));
		stat.setSubsidyAmount(NumberUtils.add(stat.getSubsidyAmount(),
				order.getSubsidyFee()));
		stat.setRevokeAmount(NumberUtils.add(stat.getRevokeAmount(),
				order.getAmount()));
		stat.setAmount(NumberUtils.add(stat.getAmount(), order.getAmount()));
		orderDaily.setRefundOrderStat(stat);
	}

	/**
	 * 处理正常放映类型统计。
	 * 
	 * @param order
	 *            订单
	 * @param orderDaily
	 *            渠道统计日统计对象
	 */
	private void doNormalShowTypeStat(TicketOrderStat order,
			DailyStatEntity orderDaily) {
		List<ShowTypeStat> stats = orderDaily.getShowTypeStat();
		ShowTypeStat stat = null;
		if (CollectionUtils.isNotEmpty(stats)) {
			Iterator<ShowTypeStat> iter = stats.iterator();
			while (iter.hasNext()) {
				ShowTypeStat showTypeStat = iter.next();
				if (showTypeStat.getShowType() == order.getShowType()) {
					stat = showTypeStat;
					iter.remove();
					break;
				}
			}
		}
		if (stat == null) {
			stat = new ShowTypeStat();
			stat.setShowType(order.getShowType());
		}
		stat.setSaleOrderCount(stat.getSaleOrderCount() + 1);
		stat.setSaleCount(stat.getSaleCount() + order.getTicketCount());
		stat.setChannelAmount(NumberUtils.add(stat.getChannelAmount(),
				order.getChannelAmount()));
		stat.setCinemaAmount(NumberUtils.add(stat.getCinemaAmount(),
				order.getCinemaAmount()));
		stats.add(stat);
		orderDaily.setShowTypeStat(stats);
	}

	/**
	 * 处理退款放映类型统计。
	 * 
	 * @param order
	 *            订单
	 * @param orderDaily
	 *            渠道统计日统计对象
	 */
	private void doRevokeShowTypeStat(TicketOrderStat order,
			DailyStatEntity orderDaily) {
		List<ShowTypeStat> stats = orderDaily.getShowTypeStat();
		ShowTypeStat stat = null;
		if (CollectionUtils.isNotEmpty(stats)) {
			Iterator<ShowTypeStat> iter = stats.iterator();
			while (iter.hasNext()) {
				ShowTypeStat showTypeStat = iter.next();
				if (showTypeStat.getShowType() == order.getShowType()) {
					stat = showTypeStat;
					iter.remove();
					break;
				}
			}
		}
		if (stat == null) {
			stat = new ShowTypeStat();
			stat.setShowType(order.getShowType());
		}
		stat.setRefundCount(stat.getRefundCount() + order.getTicketCount());
		stat.setRevokeOrderCount(stat.getRevokeOrderCount() + 1);
		setRefundSettlePrice(order, orderDaily, stat);
		stat.setRefundAmount(NumberUtils.add(stat.getRefundAmount(),
				order.getAmount()));
		stats.add(stat);
		orderDaily.setShowTypeStat(stats);
	}

	/**
	 * 设置退票结算金额。
	 * 
	 * @param order
	 *            订单统计对象
	 * @param orderDaily
	 *            日结对象
	 * @param stat
	 *            放映类型
	 */
	private void setRefundSettlePrice(TicketOrderStat order,
			DailyStatEntity orderDaily, ShowTypeStat stat) {
		if (orderDaily instanceof CinemaTicketOrderDaily) {
			stat.setRefundSettlePrice(stat.getRefundSettlePrice()
					+ order.getCinemaAmount());
		} else if (orderDaily instanceof ChannelTicketOrderDaily) {
			stat.setRefundSettlePrice(stat.getRefundSettlePrice()
					+ order.getChannelAmount());
		}
	}

	public Date getStatDate() {
		return statDate;
	}

	public List<TicketOrderStat> getStatList() {
		return statList;
	}

	public Map<String, CinemaTicketOrderDaily> getCinemaDailyMap() {
		return cinemaDailyMap;
	}

	public Map<String, ChannelTicketOrderDaily> getChannelDailyMap() {
		return channelDailyMap;
	}

	public Map<String, CinemaChannelTicketOrderDaily> getCinemaChannelDailyMap() {
		return cinemaChannelDailyMap;
	}
}
