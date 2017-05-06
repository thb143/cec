package cn.mopon.cec.api.ticket;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.api.ApiReplyCode;
import cn.mopon.cec.api.ticket.v1.SubmitOrderQuery;
import cn.mopon.cec.core.access.snack.SnackPrintStatusService;
import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.entity.BenefitCardConsumeOrder;
import cn.mopon.cec.core.entity.BenefitCardConsumeSnackOrder;
import cn.mopon.cec.core.entity.BenefitCardRechargeOrder;
import cn.mopon.cec.core.entity.BenefitCardSettle;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.BenefitCardUser;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.entity.SnackOrderItem;
import cn.mopon.cec.core.entity.SnackVoucher;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.enums.BenefitCardRechargeStatus;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.OpenStatus;
import cn.mopon.cec.core.enums.PolicyStatus;
import cn.mopon.cec.core.enums.PrintStatus;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.SnackStatus;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.mail.MailService;
import cn.mopon.cec.core.mail.ShowLockSeatsFailedModel;
import cn.mopon.cec.core.model.ShowSeat;
import cn.mopon.cec.core.service.BenefitCardOrderService;
import cn.mopon.cec.core.service.BenefitCardService;
import cn.mopon.cec.core.service.BenefitCardUserService;
import cn.mopon.cec.core.service.Circuit;
import cn.mopon.cec.core.service.HomeService;
import cn.mopon.cec.core.service.SerialNumberService;
import cn.mopon.cec.core.service.ShowSeatService;
import cn.mopon.cec.core.service.SnackChannelService;
import cn.mopon.cec.core.service.VoucherService;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;

/**
 * 票务接口面板。
 */
@Service
public class TicketFacade {
	@Resource
	private Dao<Channel> channelDao;
	@Resource
	private Dao<Cinema> cinemaDao;
	@Resource
	private Dao<Film> filmDao;
	@Resource
	private Dao<ChannelShow> channelShowDao;
	@Resource
	private Dao<TicketOrder> ticketOrderDao;
	@Resource
	private Dao<TicketVoucher> ticketVoucherDao;
	@Resource
	private Dao<SnackVoucher> snackVoucherDao;
	@Resource
	private Dao<SnackChannel> snackChannelDao;
	@Resource
	private Dao<SnackOrder> snackOrderDao;
	@Resource
	private SnackChannelService snackChannelService;
	@Resource
	private Dao<BenefitCardConsumeOrder> benefitCardConsumeOrderDao;
	@Resource
	private Dao<BenefitCardConsumeSnackOrder> benefitCardConsumeSnackOrderDao;
	@Resource
	private BenefitCardService benefitCardService;
	@Resource
	private BenefitCardOrderService benefitCardOrderService;
	@Resource
	private ShowSeatService showSeatService;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private MailService mailService;
	@Resource
	private Circuit circuit;
	@Resource
	private VoucherService voucherService;
	@Resource
	private HomeService homeService;
	@Resource
	private Dao<BenefitCard> benefitCardDao;
	@Resource
	private Dao<BenefitCardType> benefitCardTypeDao;
	@Resource
	private Dao<Snack> snackDao;
	@Resource
	private BenefitCardUserService benefitCardUserService;
	@Resource
	private SnackPrintStatusService snackPrintStatusService;

	/**
	 * 查询所有渠道列表。
	 * 
	 * @return 返回渠道列表。
	 */
	public List<Channel> queryChannels() {
		return channelDao.getAll("code", true);
	}

	/**
	 * 查询渠道。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @return 返回渠道。
	 */
	public Channel queryChannel(String channelCode) {
		Channel channel = channelDao.searchUnique("code", channelCode);
		if (channel == null) {
			ApiException.thrown(ApiReplyCode.CHANNEL_NOT_EXIST);
		}
		if (!channel.getOpened()) {
			ApiException.thrown(ApiReplyCode.CHANNEL_CLOSED);
		}
		return channel;
	}

	/**
	 * 查询影院。
	 * 
	 * @param cinemaCode
	 *            影院编码。
	 * @return 返回影院。
	 */
	public Cinema queryCinema(String cinemaCode) {
		Cinema cinema = cinemaDao.searchUnique("code", cinemaCode);
		if (cinema == null) {
			ApiException.thrown(TicketReplyCode.CINEMA_NOT_EXIST);
		}
		return cinema;
	}

	/**
	 * 查询影厅。
	 * 
	 * @param cinemaCode
	 *            影院编号
	 * @param hallCode
	 *            影厅编号
	 * @return 返回影厅。
	 */
	public Hall queryHall(String cinemaCode, String hallCode) {
		Cinema cinema = queryCinema(cinemaCode);
		Hall hall = cinema.getHall(hallCode);
		if (hall == null) {
			ApiException.thrown(TicketReplyCode.HALL_NOT_EXIST);
		}
		return hall;
	}

	/**
	 * 查询大于指定日期的影片。
	 * 
	 * @param startDate
	 *            起始日期
	 * @return 返回影片列表。
	 */
	public List<Film> queryFilms(Date startDate) {
		FullTextCriteria criteria = filmDao.createFullTextCriteria();
		criteria.addRangeField("publishDate",
				DateUtils.format(startDate, DateUtils.DAY_N), null);
		criteria.addSortDesc("publishDate", SortField.Type.LONG);
		return filmDao.searchBy(criteria);
	}

	/**
	 * 查询影片。
	 * 
	 * @param filmCode
	 *            影片编码
	 * @return 返回影片。
	 */
	public Film queryFilm(String filmCode) {
		// 把影片编码第4位替换成X进行查找。
		char[] chars = filmCode.toCharArray();
		chars[3] = 'X';
		filmCode = new String(chars);
		Film film = filmDao.searchUnique("code", filmCode);
		if (film == null) {
			ApiException.thrown(TicketReplyCode.FILM_NOT_EXIST);
		}
		return film;
	}

	/**
	 * 查询渠道在指定影院的渠道排期。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param cinemaCode
	 *            影院编码
	 * @param startDate
	 *            放映时间的起始日期
	 * @param status
	 *            状态
	 * @return 返回渠道在指定影院的渠道排期。
	 */
	public List<ChannelShow> queryChannelShowByCinema(String channelCode,
			String cinemaCode, Date startDate, ShelveStatus status) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("cinema.code", cinemaCode);
		if (status != null) {
			criteria.addFilterField("status", status.getValue());
		}
		criteria.addRangeField("showTime",
				DateUtils.format(startDate, DateUtils.DAY_N), null);
		criteria.addSortDesc("showTime", SortField.Type.LONG);
		return channelShowDao.searchBy(criteria);
	}

	/**
	 * 查询指定影院的卖品列表。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param cinemaCode
	 *            影院编码
	 * @param status
	 *            卖品状态
	 * @return 返回指定影院的卖品列表。
	 */
	public List<SnackChannel> querySnackChannels(String channelCode,
			String cinemaCode, SnackStatus status) {
		FullTextCriteria criteria = snackChannelDao.createFullTextCriteria();
		criteria.addFilterField("status", OpenStatus.OPENED.getValue());
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("snack.cinema.code", cinemaCode);
		criteria.addSortDesc("snack.type.createDate", SortField.Type.LONG);
		if (status != null) {
			criteria.addFilterField("snack.status", status.getValue());
			return snackChannelDao.searchBy(criteria);
		} else {
			criteria.addFilterField("snack.status", SnackStatus.ON.getValue(),
					SnackStatus.OFF.getValue());
			return snackChannelDao.searchBy(criteria);
		}
	}

	/**
	 * 查询渠道排期。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param channelShowCode
	 *            渠道排期编码
	 * @return 返回渠道排期。
	 */
	public ChannelShow queryChannelShow(String channelCode,
			String channelShowCode) {
		ChannelShow channelShow = getChannelShow(channelCode, channelShowCode);
		if (channelShow == null) {
			ApiException.thrown(TicketReplyCode.CHANNELSHOW_NOT_EXIST);
		}
		if (!channelShow.isSalable()) {
			ApiException.thrown(TicketReplyCode.CHANNELSHOW_INVALID);
		}
		return channelShow;
	}

	/**
	 * 获取指定状态的排期座位列表。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param status
	 *            排期座位状态
	 * @return 返回指定状态的排期座位列表。
	 */
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus status) {
		if (status != null) {
			return showSeatService.getShowSeats(channelShow, status);
		} else {
			return showSeatService.getShowSeats(channelShow);
		}
	}

	/**
	 * 查询替换的渠道排期。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param channelShowCode
	 *            渠道排期编码
	 * @return 返回替换的渠道排期。
	 */
	public ChannelShow queryReplaceChannelShow(String channelCode,
			String channelShowCode) {
		ChannelShow channelShow = getChannelShow(channelCode, channelShowCode);
		if (channelShow == null) {
			ApiException.thrown(TicketReplyCode.CHANNELSHOW_NOT_EXIST);
		}
		if (channelShow.getStatus() != ShelveStatus.INVALID) {
			ApiException.thrown(TicketReplyCode.CHANNELSHOW_NO_REPLACE);
		}
		ChannelShow replaceChannelShow = getCurrentValidChannelShow(channelShow);
		if (replaceChannelShow == null) {
			ApiException.thrown(TicketReplyCode.CHANNELSHOW_NO_REPLACE);
		}
		return replaceChannelShow;
	}

	/**
	 * 锁定座位。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param channelOrderCode
	 *            渠道订单号
	 * @param seatCodes
	 *            座位编码（逗号分割）
	 * @param submitPrice
	 *            票房价
	 * @return 返回新建的选座票订单。
	 */
	public TicketOrder lockSeats(ChannelShow channelShow,
			String channelOrderCode, String seatCodes, Double submitPrice) {
		if (submitPrice != null
				&& submitPrice.doubleValue() != channelShow.getSubmitPrice()
						.doubleValue()) {
			ApiException.thrown(TicketReplyCode.SUBMITPRICE_NOT_MATCH);
		}
		List<ShowSeat> showSeats = showSeatService.getShowSeats(channelShow,
				seatCodes);
		TicketOrder order = TicketOrder.create(channelShow, showSeats);
		order.setChannelOrderCode(channelOrderCode);
		// 锁座前需要生成订单号，调用锁座接口时使用。
		order.setCode(serialNumberService.getTicketOrderCode());
		TicketAccessService accessService = getAccessService(order);
		TicketOrder externalOrder = accessService.lockSeat(order);
		order.setCinemaOrderCode(externalOrder.getCinemaOrderCode());
		Date expireTime = new DateTime(order.getCreateTime()).plusMinutes(
				circuit.getLockSeatTime()).toDate();
		order.setExpireTime(expireTime);
		ticketOrderDao.save(order);
		showSeatService.updateShowSeats(order, SellStatus.DISABLED);
		return order;
	}

	/**
	 * 释放座位。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void releaseSeats(TicketOrder order) {
		if (order.getStatus() != TicketOrderStatus.UNPAID) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		TicketAccessService accessService = getAccessService(order);
		accessService.releaseSeat(order);
		order.setStatus(TicketOrderStatus.CANCELED);
		showSeatService.updateShowSeats(order, SellStatus.ENABLED);
	}

	/**
	 * 支付订单。
	 * 
	 * @param order
	 *            选座票订单
	 * @param query
	 *            确认订单请求
	 */
	@Transactional
	public void payOrder(TicketOrder order, SubmitOrderQuery query) {
		if (!order.isSubmitable()) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		if (!order.isSeatsMatched(query.getOrderSeats())) {
			ApiException.thrown(TicketReplyCode.ORDER_SEATS_UNMATCH);
		}
		if (!checkOrderSeats(query.getOrderSeats())) {
			ApiException.thrown(TicketReplyCode.SEATS_FORMAT_ERROR);
		}
		if (StringUtils.isNotEmpty(query.getSnacks())) {
			setSnackOrder(order, query.getSnacks());
		}
		if (StringUtils.isNotEmpty(query.getBenefit())) {
			setBenefitCardOrder(order, query.getBenefit(),
					query.getOrderSeats(), query.getSnacks());
		}
		if (!order.isSettlePriceMatched(query.getOrderSeats())) {
			ApiException.thrown(TicketReplyCode.SUBMITPRICE_NOT_MATCH);
		}
		if (StringUtils.isNotEmpty(query.getSnacks())
				&& order.getSnackOrder() != null
				&& !order.getSnackOrder().isSnackSettlePriceMatched(
						query.getSnacks())) {
			ApiException.thrown(TicketReplyCode.SNACK_SETTLEPRICE_NOT_MATCH);
		}
		order.setSaleAmount(query.getOrderSeats());
		order.setPayTime(new Date());
		order.setMobile(query.getMobile());
		order.setStatus(TicketOrderStatus.PAID);
		if (order.getSnackOrder() != null) {
			order.getSnackOrder().setMobile(order.getMobile());
			order.getSnackOrder().setStatus(order.getStatus());
		}
	}

	/**
	 * 检查座位信息格式。
	 * 
	 * @param orderSeats
	 * @return 检查通过返回true，不通过返回false。
	 */
	private boolean checkOrderSeats(String orderSeats) {
		for (String orderSeat : orderSeats.split(",")) {
			String[] seatsInfos = orderSeat.split(":");
			if (seatsInfos.length <= 1) {
				return false;
			}
			try {
				Double.parseDouble(seatsInfos[1]);
				if (seatsInfos.length > 2) {
					Double.parseDouble(seatsInfos[2]);
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查卖品信息格式。
	 * 
	 * @param snacks
	 *            卖品信息
	 * @return 检查通过返回true，不通过返回false。
	 */
	private boolean checkSnacks(String snacks) {
		for (String info : snacks.split(",")) {
			String[] snackInfos = info.split(":");
			if (snackInfos.length != 4) {
				return false;
			}
			try {
				Double.parseDouble(snackInfos[1]);
				Integer.parseInt(snackInfos[2]);
				Double.parseDouble(snackInfos[3]);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查权益卡信息格式。
	 * 
	 * @param benefit
	 *            权益卡信息
	 * @return 检查通过返回true，不通过返回false。
	 */
	private boolean checkBenefitFormat(String benefit) {
		String[] info = benefit.split(":");
		if (info.length != 3) {
			return false;
		}
		try {
			Double.parseDouble(info[2]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 确认卖品时检查权益卡信息格式。
	 * 
	 * @param benefit
	 *            权益卡信息
	 * @return 检查通过返回true，不通过返回false。
	 */
	private boolean checkSnackBenefitFormat(String benefit) {
		String[] info = benefit.split(":");
		if (info.length != 2) {
			return false;
		}
		return true;
	}

	/**
	 * 设置订单卖品信息。
	 * 
	 * @param order
	 *            选座票订单
	 * @param snacks
	 *            卖品信息
	 */
	private void setSnackOrder(TicketOrder order, String snacks) {
		if (!checkSnacks(snacks)) {
			ApiException.thrown(TicketReplyCode.SNACK_FORMAT_ERROR);
		}
		SnackOrder snackOrder = SnackOrder.create(order);
		String[] snackInfos = snacks.split(",");
		for (String info : snackInfos) {
			String[] snackItems = info.split(":");
			SnackChannel snackChannel = snackChannelService.searchSnackChannel(
					order.getChannel().getCode(), snackItems[0]);
			if (snackChannel == null
					|| snackChannel.getSnack() == null
					|| !order.getCinema().getSnacks()
							.contains(snackChannel.getSnack())) {
				ApiException.thrown(TicketReplyCode.SNACK_NOT_EXIST);
			}
			if (snackChannel.getStatus() != OpenStatus.OPENED
					|| snackChannel.getSnack().getStatus() != SnackStatus.ON) {
				ApiException.thrown(TicketReplyCode.SNACK_STATUS_INVALID);
			}
			SnackOrderItem orderItem = SnackOrderItem
					.create(snackChannel, info);
			snackOrder.addOrderItem(orderItem);
		}
		snackOrderDao.save(snackOrder);
		order.setSnackOrder(snackOrder);
		order.setSnackCount(snackOrder.getSnackCount());
	}

	/**
	 * 设置权益卡订单。
	 * 
	 * @param order
	 *            选座票订单
	 * @param benefit
	 *            权益卡信息
	 * @param seat
	 *            订单座位信息
	 * @param snack
	 *            卖品信息
	 */
	private void setBenefitCardOrder(TicketOrder order, String benefit,
			String seat, String snack) {
		if (!checkBenefitFormat(benefit)) {
			ApiException.thrown(TicketReplyCode.BENEFIT_FORMAT_ERROR);
		}
		String[] benefitInfo = benefit.split(":");
		String mobile = benefitInfo[0];
		String benefitCardTypeCode = benefitInfo[1];
		Double discountAmount = Double.parseDouble(benefitInfo[2]);

		BenefitCard benefitCard = benefitCardService.getBenefitCard(order
				.getChannel().getCode(), benefitCardTypeCode, mobile);
		// 确认权益卡是否存在
		if (benefitCard == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_NOT_EXIST);
		}
		BenefitCardSettle settle = getBenefitCardSettle(benefitCard.getType(),
				order.getChannelShowCode());
		// 校验权益卡
		checkBenefitCard(benefitCard, order, discountAmount, settle);
		// 重新设定选座票订单价格，返回订单的总优惠金额
		Double totalDisCountAmount = resetTicketOrderPrice(order, settle);
		if (order.getSnackOrder() != null) {
			SnackOrder snackOrder = order.getSnackOrder();
			resetSnackOrderPriceInfo(snackOrder);
			for (SnackOrderItem snackOrderItem : snackOrder.getOrderItems()) {
				BenefitCardTypeSnackRule snackRule = benefitCard.getType()
						.getMatchedSnackRule(snackOrderItem.getSnack());
				if (snackRule == null
						|| snackRule.getValid() != ValidStatus.VALID
						|| snackRule.getEnabled() != EnabledStatus.ENABLED) {
					ApiException
							.thrown(TicketReplyCode.BENEFITCARD_SNACK_REFUSE);
				}
				totalDisCountAmount += resetSnackOrderItemPrice(snackOrderItem,
						snackRule);
				snackOrder.setChannelAmount(NumberUtils.add(
						snackOrder.getChannelAmount(),
						snackOrderItem.getChannelPrice()
								* snackOrderItem.getCount()));
				snackOrder.setCinemaAmount(NumberUtils.add(
						snackOrder.getCinemaAmount(),
						snackOrderItem.getCinemaPrice()
								* snackOrderItem.getCount()));
				snackOrder.setChannelFee(NumberUtils.add(
						snackOrder.getChannelFee(),
						snackOrderItem.getChannelFee()
								* snackOrderItem.getCount()));
				snackOrder.setDiscountAmount(NumberUtils.add(
						snackOrder.getDiscountAmount(),
						snackOrderItem.getDiscountPrice()
								* snackOrderItem.getCount()));
			}
		}
		order.setDiscountAmount(totalDisCountAmount);
		// 创建权益卡消费订单
		BenefitCardConsumeOrder benefitCardConsumeOrder = BenefitCardConsumeOrder
				.create(order, benefitCard, totalDisCountAmount,
						settle.getRule());
		if (benefitCard.getFirstCinema() == null) {
			benefitCard.setFirstCinema(order.getCinema());
		}
		benefitCardConsumeOrderDao.save(benefitCardConsumeOrder);
		order.setBenefitCardConsumeOrder(benefitCardConsumeOrder);

		if (!order.isSettlePriceMatched(seat)) {
			ApiException.thrown(TicketReplyCode.SUBMITPRICE_NOT_MATCH);
		}
		if (StringUtils.isNotEmpty(snack) && order.getSnackOrder() != null
				&& !order.getSnackOrder().isSnackSettlePriceMatched(snack)) {
			ApiException.thrown(TicketReplyCode.SNACK_SETTLEPRICE_NOT_MATCH);
		}
		// 优惠次数变更
		incrDiscountCount(benefitCard,
				order.getTicketCount() + order.getSnackCount(),
				totalDisCountAmount);
	}

	/**
	 * 扣减已优惠次数。
	 * 
	 * @param order
	 *            选座票订单
	 */
	private void decrDiscountCount(TicketOrder order) {
		BenefitCardConsumeOrder consumeOrder = order
				.getBenefitCardConsumeOrder();
		if (order.getBenefitCardConsumeOrder() == null) {
			return;
		}
		BenefitCard card = consumeOrder.getCard();
		// 减去已使用次数与金额
		card.setAvailableDiscountCount(card.getAvailableDiscountCount()
				+ consumeOrder.getDiscountCount());
		card.setDiscountAmount(NumberUtils.sub(card.getDiscountAmount(),
				consumeOrder.getDiscountAmount()));
		if (order.getPayTime().before(DateUtils.getToday()) || card.isExpire()) {
			return;
		}
		benefitCardOrderService.decrDailyDiscountCount(card.getCode(),
				consumeOrder.getDiscountCount());
	}

	/**
	 * 扣减已优惠次数。
	 * 
	 * @param order
	 *            权益卡订单
	 */
	private void decrSnackDiscountCount(SnackOrder order) {
		BenefitCardConsumeSnackOrder consumeOrder = order
				.getBenefitCardConsumeSnackOrder();
		if (order.getBenefitCardConsumeSnackOrder() == null) {
			return;
		}
		BenefitCard card = consumeOrder.getCard();
		// 减去已使用次数与金额
		card.setAvailableDiscountCount(card.getAvailableDiscountCount()
				+ consumeOrder.getDiscountCount());
		card.setDiscountAmount(NumberUtils.sub(card.getDiscountAmount(),
				consumeOrder.getDiscountAmount()));
		if (order.getCreateTime().before(DateUtils.getToday())
				|| card.isExpire()) {
			return;
		}
		benefitCardOrderService.decrDailyDiscountCount(card.getCode(),
				consumeOrder.getDiscountCount());
	}

	/**
	 * 增加权益卡已优惠次数。
	 * 
	 * @param card
	 *            权益卡
	 * @param count
	 *            优惠次数
	 * @param disCountAmount
	 *            优惠金额
	 */
	private void incrDiscountCount(BenefitCard card, Integer count,
			Double disCountAmount) {
		// 增加已使用次数与金额
		card.setAvailableDiscountCount(card.getAvailableDiscountCount() - count);
		card.setDiscountAmount(NumberUtils.add(card.getDiscountAmount(),
				disCountAmount));
		benefitCardOrderService.incrDailyDiscountCount(card.getCode(), count);
	}

	/**
	 * 重新设定选座票订单价格。
	 * 
	 * @param order
	 *            选座票订单
	 * @param benefitCardType
	 *            权益卡类型
	 * @param settle
	 *            权益卡结算价格
	 * @return 返回订单的总优惠金额。
	 */
	private Double resetTicketOrderPrice(TicketOrder order,
			BenefitCardSettle settle) {
		Double totalDisCountAmount = 0D;
		resetTicketOrderPriceInfo(order, settle);
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			resetTicketOrderItemPrice(orderItem, settle);
			order.setCinemaAmount(NumberUtils.add(order.getCinemaAmount(),
					orderItem.getCinemaPrice()));
			order.setChannelAmount(NumberUtils.add(order.getChannelAmount(),
					orderItem.getChannelPrice()));
			order.setSubmitAmount(NumberUtils.add(order.getSubmitAmount(),
					orderItem.getSubmitPrice()));
			order.setCircuitFee(NumberUtils.add(order.getCircuitFee(),
					orderItem.getCircuitFee()));
			order.setSubsidyFee(NumberUtils.add(order.getSubsidyFee(),
					orderItem.getSubsidyFee()));

			totalDisCountAmount = NumberUtils.add(totalDisCountAmount,
					settle.getDiscountPrice());
		}
		return totalDisCountAmount;
	}

	/**
	 * 重置选座票订单价格信息。
	 * 
	 * @param order
	 *            选座票订单
	 * @param settle
	 *            权益卡结算价格
	 */
	private void resetTicketOrderPriceInfo(TicketOrder order,
			BenefitCardSettle settle) {
		order.setChannelAmount(0D);
		order.setCinemaAmount(0D);
		order.setSubmitAmount(0D);
		order.setCircuitFee(0D);
		order.setSubsidyFee(0D);
		order.setCinemaRule(settle.getChannelShow().getCinemaRule());
		order.setChannelRule(settle.getChannelShow().getChannelRule());
		order.setSpecialRule(settle.getChannelShow().getSpecialRule());
		order.setSpecialChannel(settle.getChannelShow().getSpecialChannel());
		order.setSingleCinemaPrice(settle.getCinemaPrice());
		order.setSingleChannelPrice(settle.getChannelPrice());
		order.setSingleSubmitPrice(settle.getSubmitPrice());
		order.setSingleConnectFee(settle.getChannelShow().getConnectFee());
		order.setSingleCircuitFee(settle.getCircuitFee());
		order.setSingleSubsidyFee(settle.getSubsidyFee());
	}

	/**
	 * 重新设定选座票订单明细价格。
	 * 
	 * @param orderItem
	 *            选座票订单明细
	 * @param settle
	 *            权益卡结算价格
	 */
	private void resetTicketOrderItemPrice(TicketOrderItem orderItem,
			BenefitCardSettle settle) {
		orderItem.setChannelPrice(settle.getChannelPrice());
		orderItem.setCinemaPrice(settle.getCinemaPrice());
		orderItem.setSubmitPrice(settle.getSubmitPrice());
		orderItem.setCircuitFee(settle.getCircuitFee());
		orderItem.setSubsidyFee(settle.getSubsidyFee());
		orderItem.setDiscountPrice(settle.getDiscountPrice());
	}

	/**
	 * 重置卖品订单价格信息。
	 * 
	 * @param order
	 *            卖品订单
	 */
	private void resetSnackOrderPriceInfo(SnackOrder order) {
		order.setCinemaAmount(0D);
		order.setChannelAmount(0D);
		order.setChannelFee(0D);
	}

	/**
	 * 重新设定卖品订单明细价格。
	 * 
	 * @param orderItem
	 *            卖品订单明细
	 * @param snackRule
	 *            权益卡卖品规则
	 * @return 返回订单明细的优惠金额。
	 */
	private Double resetSnackOrderItemPrice(SnackOrderItem orderItem,
			BenefitCardTypeSnackRule snackRule) {
		Double origChannelPrice = orderItem.getChannelPrice();
		orderItem.setSnackRule(snackRule);
		orderItem.setCinemaPrice(snackRule.getSettleRule().cal(
				orderItem.getCinemaPrice()) > 0 ? snackRule.getSettleRule()
				.cal(orderItem.getCinemaPrice()) : 0D);
		orderItem.setChannelPrice(snackRule.getSettleRule().cal(
				orderItem.getChannelPrice()) > 0 ? snackRule.getSettleRule()
				.cal(orderItem.getChannelPrice()) : 0D);
		orderItem.setChannelFee(NumberUtils.sub(orderItem.getSalePrice(),
				orderItem.getChannelPrice()));
		orderItem.setDiscountPrice(NumberUtils.sub(origChannelPrice,
				orderItem.getChannelPrice()));
		return (NumberUtils.sub(origChannelPrice, orderItem.getChannelPrice()))
				* orderItem.getCount();
	}

	/**
	 * 校验权益卡。
	 * 
	 * @param benefitCard
	 *            权益卡
	 * @param order
	 *            订单
	 * @param discountAmount
	 *            优惠价格
	 * @param settle
	 *            权益卡结算价格
	 */
	private void checkBenefitCard(BenefitCard benefitCard, TicketOrder order,
			Double discountAmount, BenefitCardSettle settle) {
		if (settle == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_REFUSE);
		}
		// 判断权益卡是否可用
		checkBenefitCardIsValid(benefitCard,
				order.getTicketCount() + order.getSnackCount());
		// 判断优惠价格是否正确
		if (discountAmount.doubleValue() != settle.getDiscountPrice()
				.doubleValue()) {
			ApiException
					.thrown(TicketReplyCode.BENEFITCARD_DISCOUNTPRICE_INVALID);
		}
	}

	/**
	 * 使用权益卡。
	 * 
	 * @param snackOrder
	 *            卖品订单
	 * @param benefit
	 *            权益卡信息
	 * @param snacks
	 *            卖品信息
	 */
	private void setBenefitCardSnackOrder(SnackOrder snackOrder,
			String benefit, String snacks) {
		if (!checkSnackBenefitFormat(benefit)) {
			ApiException.thrown(TicketReplyCode.BENEFIT_FORMAT_ERROR);
		}
		String[] benefitInfo = benefit.split(":");
		String mobile = benefitInfo[0];
		String benefitCardTypeCode = benefitInfo[1];

		BenefitCard benefitCard = benefitCardService.getBenefitCard(snackOrder
				.getChannel().getCode(), benefitCardTypeCode, mobile);
		// 确认权益卡是否存在
		if (benefitCard == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_NOT_EXIST);
		}
		if (!benefitCard.getType().getChannels()
				.contains(snackOrder.getChannel())) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_SNACK_REFUSE);
		}
		// 判断权益卡是否可用
		checkBenefitCardIsValid(benefitCard, snackOrder.getSnackCount());
		Double totalDisCountAmount = 0D;
		resetSnackOrderPriceInfo(snackOrder);
		for (SnackOrderItem item : snackOrder.getOrderItems()) {
			BenefitCardTypeSnackRule snackRule = benefitCard.getType()
					.getMatchedSnackRule(item.getSnack());
			if (snackRule == null || snackRule.getValid() != ValidStatus.VALID
					|| snackRule.getEnabled() != EnabledStatus.ENABLED) {
				ApiException.thrown(TicketReplyCode.BENEFITCARD_SNACK_REFUSE);
			}
			totalDisCountAmount += resetSnackOrderItemPrice(item, snackRule);
			snackOrder.setChannelAmount(NumberUtils.add(
					snackOrder.getChannelAmount(), item.getChannelPrice()
							* item.getCount()));
			snackOrder.setCinemaAmount(NumberUtils.add(
					snackOrder.getCinemaAmount(),
					item.getCinemaPrice() * item.getCount()));
			snackOrder.setChannelFee(NumberUtils.add(
					snackOrder.getChannelFee(),
					item.getChannelFee() * item.getCount()));
			snackOrder.setDiscountAmount(NumberUtils.add(
					snackOrder.getDiscountAmount(), item.getDiscountPrice()
							* item.getCount()));
		}
		// 创建权益卡消费订单
		BenefitCardConsumeSnackOrder consumeSnackOrder = BenefitCardConsumeSnackOrder
				.create(snackOrder, benefitCard, totalDisCountAmount);
		if (benefitCard.getFirstCinema() == null) {
			benefitCard.setFirstCinema(snackOrder.getCinema());
		}
		benefitCardConsumeSnackOrderDao.save(consumeSnackOrder);
		snackOrder.setBenefitCardConsumeSnackOrder(consumeSnackOrder);

		if (!snackOrder.isSnackSettlePriceMatched(snacks)) {
			ApiException.thrown(TicketReplyCode.SNACK_SETTLEPRICE_NOT_MATCH);
		}
		// 优惠次数变更
		incrDiscountCount(benefitCard, snackOrder.getSnackCount(),
				totalDisCountAmount);
	}

	/**
	 * 获取渠道排期关联的权益卡结算价格。
	 * 
	 * @param benefitCardType
	 *            权益卡类型
	 * @param channelShowCode
	 *            渠道排期编号
	 * @return 返回渠道排期关联的权益卡结算价格。
	 */
	private BenefitCardSettle getBenefitCardSettle(
			BenefitCardType benefitCardType, String channelShowCode) {
		ChannelShow channelShow = channelShowDao.findUnique("code",
				channelShowCode);
		// 渠道排期关联的权益卡结算价格类别是否有此卡类
		for (BenefitCardSettle settle : channelShow.getBenefitCardSettles()) {
			if (benefitCardType.getId().equals(
					settle.getRule().getType().getId())) {
				return settle;
			}
		}
		return null;
	}

	/**
	 * 判断权益卡是否可用。
	 * 
	 * @param benefitCard
	 *            权益卡
	 * @param count
	 *            需要优惠次数
	 */
	private void checkBenefitCardIsValid(BenefitCard benefitCard, Integer count) {
		// 判断权益卡是否有效
		if (!benefitCard.isValid()) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_STATUS_INVALID);
		}
		// 判断权益卡优惠总次数
		if (benefitCard.getAvailableDiscountCount() - count < 0) {
			ApiException
					.thrown(TicketReplyCode.BENEFITCARD_TOTAL_COUNT_INSUFFICIENT);
		}
		// 判断权益卡当天优惠次数
		if (benefitCard.getType().getDailyDiscountCount() < count
				+ benefitCard.getDailyDiscountCount()) {
			ApiException
					.thrown(TicketReplyCode.BENEFITCARD_DAILY_COUNT_INSUFFICIENT);
		}
	}

	/**
	 * 确认订单。
	 * 
	 * @param order
	 *            选座票订单
	 */
	@Transactional
	public void submitOrder(TicketOrder order) {
		TicketAccessService accessService = getAccessService(order);
		TicketOrder externalTicketOrder = accessService.submitOrder(order);
		order.confirm(externalTicketOrder);
		createVoucher(order, externalTicketOrder);
		homeService.incrTicketOrderStat(order);
	}

	/**
	 * 退票。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回是否退票成功。
	 */
	public Boolean revokeTicket(TicketOrder order) {
		if (!order.isRevokeable()) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		if (!order.isOverRevokeTime()) {
			ApiException.thrown(TicketReplyCode.ORDER_OVER_REVOKETIME);
		}
		TicketAccessService accessService = getAccessService(order);
		Boolean revoked = accessService.refundTicket(order);
		if (revoked) {
			order.setRevokeTime(new Date());
			order.setStatus(TicketOrderStatus.REVOKED);
			if (order.getSnackOrder() != null) {
				processSnackOrder(order);
			}
			decrDiscountCount(order);
		} else {
			ApiException.thrown(TicketReplyCode.ORDER_REVOKE_FAILED);
		}
		return revoked;
	}

	/**
	 * 标记退票。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void markTicketRevoked(TicketOrder order) {
		if (!order.isRevokeable()) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		order.setRevokeTime(new Date());
		order.setStatus(TicketOrderStatus.REVOKED);
		if (order.getBenefitCardConsumeOrder() != null) {
			decrDiscountCount(order);
		}
		if (order.getSnackOrder() != null) {
			processSnackOrder(order);
		}
	}

	/**
	 * 处理订单对应的卖品订单。
	 * 
	 * @param order
	 *            选座票订单
	 */
	private void processSnackOrder(TicketOrder order) {
		SnackOrder snackOrder = order.getSnackOrder();
		snackOrder.setStatus(order.getStatus());
		if (order.getStatus() == TicketOrderStatus.REVOKED) {
			snackOrder.setRevokeTime(order.getRevokeTime());
		}
	}

	/**
	 * 根据订单编码和凭证编码查询打印状态。
	 * 
	 * @param orderCode
	 *            影院编码
	 * @param code
	 *            凭证编码
	 * @return 返回打票信息。
	 */
	public TicketOrder queryPrintByCode(String orderCode, String code) {
		FullTextCriteria criteria = ticketVoucherDao.createFullTextCriteria();
		criteria.addFilterField("order.code", orderCode);
		criteria.addFilterField("code", code);
		TicketVoucher voucher = ticketVoucherDao.searchUnique(criteria);
		return queryPrintStatus(voucher);
	}

	/**
	 * 根据订单编码、取票号和取票验证码查询凭证打票信息。
	 * 
	 * @param orderCode
	 *            影院编码
	 * @param printCode
	 *            取票号
	 * @param verifyCode
	 *            取票验证码
	 * @return 返回打票信息。
	 */
	public TicketOrder queryByVerifyCode(String orderCode, String printCode,
			String verifyCode) {
		FullTextCriteria criteria = ticketVoucherDao.createFullTextCriteria();
		criteria.addFilterField("order.code", orderCode);
		criteria.addFilterField("printCode", printCode);
		criteria.addFilterField("verifyCode", verifyCode);
		TicketVoucher voucher = ticketVoucherDao.searchUnique(criteria);
		return queryPrintStatus(voucher);
	}

	/**
	 * 确认打票。
	 * 
	 * @param orderCode
	 *            影院编码
	 * @param code
	 *            凭证编码
	 * @return 返回打票信息。
	 */
	public boolean confirmPrint(String orderCode, String code) {
		FullTextCriteria criteria = ticketVoucherDao.createFullTextCriteria();
		criteria.addFilterField("order.code", orderCode);
		criteria.addFilterField("code", code);
		TicketVoucher voucher = ticketVoucherDao.searchUnique(criteria);
		return confirmPrintVoucher(voucher);
	}

	/**
	 * 确认打票。
	 * 
	 * @param orderCode
	 *            影院编码
	 * @param printCode
	 *            取票号
	 * @param verifyCode
	 *            取票验证码
	 * @return 返回打票信息。
	 */
	public boolean confirmPrint(String orderCode, String printCode,
			String verifyCode) {
		FullTextCriteria criteria = ticketVoucherDao.createFullTextCriteria();
		criteria.addFilterField("order.code", orderCode);
		criteria.addFilterField("printCode", printCode);
		criteria.addFilterField("verifyCode", verifyCode);
		TicketVoucher voucher = ticketVoucherDao.searchUnique(criteria);
		return confirmPrintVoucher(voucher);
	}

	/**
	 * 更换凭证。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void changeVoucher(TicketOrder order) {
		TicketVoucher voucher = order.getVoucher();
		if (voucher == null) {
			ApiException.thrown(TicketReplyCode.VOUCHER_NOT_EXIST);
		}
		if (!voucher.isChangeable()) {
			ApiException.thrown(TicketReplyCode.VOUCHER_STATUS_INVALID);
		}
		// 注销原来的凭证号再生成新的凭证号。
		voucherService.desTicketVoucherCode(order);
		String newCode = voucherService
				.genTicketVoucherCode(voucher.getOrder());
		voucher.setCode(newCode);
	}

	/**
	 * 重置凭证。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void resetVoucher(TicketOrder order) {
		TicketVoucher voucher = order.getVoucher();
		if (voucher == null) {
			ApiException.thrown(TicketReplyCode.VOUCHER_NOT_EXIST);
		}
		if (!voucher.isResetable()) {
			ApiException.thrown(TicketReplyCode.VOUCHER_STATUS_INVALID);
		}
		voucher.setPrintable(true);
		voucher.setReprintCount(voucher.getReprintCount() + 1);
	}

	/**
	 * 发送锁座失败邮件。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param ex
	 *            异常
	 */
	public void sendSeatsLocakFailMail(ChannelShow channelShow, Exception ex) {
		List<User> users = circuit.getOrderWarnUsers();
		ShowLockSeatsFailedModel model = new ShowLockSeatsFailedModel(
				channelShow, ex, users);
		mailService.send(model);
	}

	/**
	 * 查询渠道指定订单号的选座票订单。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param orderCode
	 *            订单号
	 * @return 返回渠道指定订单号的选座票订单。
	 */
	public TicketOrder queryTicketOrder(String channelCode, String orderCode) {
		FullTextCriteria criteria = ticketOrderDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("code", orderCode);
		TicketOrder order = ticketOrderDao.searchUnique(criteria);
		if (order == null) {
			ApiException.thrown(TicketReplyCode.ORDER_NOT_EXIST);
		}
		return order;
	}

	/**
	 * 开卡。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param mobile
	 *            用户手机号
	 * @param typeCode
	 *            卡类编号
	 * @param channelOrderCode
	 *            渠道开卡订单号
	 * @param initAmount
	 *            开卡金额
	 * @param birthday
	 *            会员生日
	 * @return 返回卡。
	 */
	@Transactional
	public BenefitCard openBenefitCard(String channelCode, String mobile,
			String typeCode, String channelOrderCode, Double initAmount,
			String birthday) {
		// 检查卡信息。
		checkBenefitCard(channelCode, mobile, typeCode, channelOrderCode);
		// 检查卡类信息。
		Channel channel = queryChannel(channelCode);
		BenefitCardType cardType = getBenefitCardType(typeCode);
		checkBenefitCardType(cardType);
		if (!cardType.containsChannel(channel)) {
			ApiException.thrown(TicketReplyCode.BENEFITCARDTYPE_NOT_EXIST);
		}
		checkAmount(initAmount, cardType.getInitAmount(),
				TicketReplyCode.OPEN_BENEFITCARD_AMOUNT_INVALID);
		BenefitCard card = new BenefitCard();
		if (checkBirthday(birthday)) {
			card.setBirthday(DateUtils.parse(birthday, DateUtils.DAY));
		}
		card.setChannelOrderCode(channelOrderCode);
		card.setType(cardType);
		card.setCode(serialNumberService.getBenefitCardCode(cardType
				.getPrefix()));
		card.setChannel(channel);
		BenefitCardUser user = getBenefitCardUser(channelCode, mobile);
		card.setUser(user);
		card.setInitAmount(initAmount);
		card.setTotalDiscountCount(cardType.getTotalDiscountCount());
		card.setAvailableDiscountCount(cardType.getTotalDiscountCount());
		card.setStartDate(DateUtils.getToday());
		card.setEndDate(new DateTime(DateUtils.getToday()).plusMonths(
				Integer.parseInt(cardType.getValidMonth().getValue())).toDate());
		card.setCreateDate(new Date());
		benefitCardDao.save(card);
		user.getCards().add(card);
		return card;
	}

	/**
	 * 校验生日格式是否正确。
	 * 
	 * @param birthday
	 *            生日日期字符串
	 * @return 如果为yyyy-MM-dd格式，返回true，为空返回false，否则向上一层抛API异常。
	 */
	private Boolean checkBirthday(String birthday) {
		if (StringUtils.isNotEmpty(birthday)) {
			if (DateUtils.isDate(birthday, DateUtils.DAY)) {
				return true;
			} else {
				ApiException
						.thrown(TicketReplyCode.BENEFITCARD_BIRTHDAY_FORMAT_INVALID);
			}
		}
		return false;
	}

	/**
	 * 查询开卡订单。
	 * 
	 * @param channelOrderCode
	 *            渠道开卡订单号
	 * @param channelCode
	 *            渠道编号
	 * @return 返回开卡订单。
	 */
	public BenefitCard queryBenefitCardByChannelOrderCode(
			String channelOrderCode, String channelCode) {
		BenefitCard benefitCard = benefitCardService
				.getBenefitCardByChannelOrderCode(channelOrderCode, channelCode);
		if (benefitCard == null) {
			ApiException
					.thrown(TicketReplyCode.OPEN_BENEFITCARD_ORDER_NOT_EXIST);
		}
		return benefitCard;
	}

	/**
	 * 续费。
	 * 
	 * @param card
	 *            权益卡
	 * @param channelOrderCode
	 *            渠道订单好
	 * @param rechargeAmount
	 *            续费金额
	 * @return 返回续费情况。
	 */
	public BenefitCardRechargeOrder saveRechargeBenefitCardOrder(
			BenefitCard card, String channelOrderCode, Double rechargeAmount) {
		BenefitCardRechargeOrder order = benefitCardOrderService
				.getBenefitCardRechargeOrder(card.getChannel().getCode(),
						channelOrderCode);
		if (order != null) {
			ApiException.thrown(TicketReplyCode.CHANNEL_ORDERCODE_EXISTS);
		}
		checkBenefitCardType(card.getType());
		checkAmount(rechargeAmount, card.getType().getRechargeAmount(),
				TicketReplyCode.RECHARGE_BENEFITCARD_AMOUNT_INVALID);
		return benefitCardOrderService.createBenefitCardRechargeOrder(card,
				card.getChannel(), rechargeAmount, channelOrderCode);
	}

	/**
	 * 获取权益卡。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param mobile
	 *            手机号码
	 * @param typeCode
	 *            卡类编号
	 * @return 权益卡
	 */
	public BenefitCard getBenefitCard(String channelCode, String mobile,
			String typeCode) {
		return benefitCardService.getBenefitCard(channelCode, typeCode, mobile);
	}

	/**
	 * 续费更改卡信息。
	 * 
	 * @param card
	 *            权益卡
	 * @param rechargeOrder
	 *            续费订单
	 * @return 返回续费情况。
	 */
	@Transactional
	public void rechargeBenefitCard(BenefitCard card,
			BenefitCardRechargeOrder rechargeOrder) {
		try {
			benefitCardService.rechargeBenefitCard(card);
			rechargeOrder.setStatus(BenefitCardRechargeStatus.SUCCESS);
			rechargeOrder.setEndDate(card.getEndDate());
			rechargeOrder.setDiscountCount(card.getAvailableDiscountCount());
			rechargeOrder.setTotalDiscountCount(card.getTotalDiscountCount());
			rechargeOrder.setExpireCount(rechargeOrder.getOldDiscountCount()
					+ card.getType().getTotalDiscountCount()
					- card.getAvailableDiscountCount());
		} catch (Exception e) {
			rechargeOrder.setStatus(BenefitCardRechargeStatus.FAIL);
		}
	}

	/**
	 * 查询续费订单。
	 * 
	 * @param channelOrderCode
	 *            渠道续费订单号
	 * @param channelCode
	 *            渠道编号
	 * @return 返回续费订单。
	 */
	public BenefitCardRechargeOrder queryBenefitCardRechargeOrder(
			String channelCode, String channelOrderCode) {
		BenefitCardRechargeOrder rechargeOrder = benefitCardOrderService
				.getBenefitCardRechargeOrder(channelCode, channelOrderCode);
		if (rechargeOrder == null) {
			ApiException
					.thrown(TicketReplyCode.BENEFITCARD_RECHARGE_ORDER_NOT_EXIST);
		}
		if (rechargeOrder.getStatus() != BenefitCardRechargeStatus.SUCCESS) {
			ApiException.thrown(TicketReplyCode.RECHARGE_BENEFITCARD_FAIL);
		}
		return rechargeOrder;
	}

	/**
	 * 查询卡信息。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param typeCode
	 *            卡类编号
	 * @param mobile
	 *            开卡手机号码
	 * @return 返回卡信息。
	 */
	public BenefitCard queryBenefitCard(String channelCode, String typeCode,
			String mobile) {
		BenefitCard benefitCard = benefitCardService.getBenefitCard(
				channelCode, typeCode, mobile);
		if (benefitCard == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_NOT_EXIST);
		}
		return benefitCard;
	}

	/**
	 * 查询渠道卡类。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @return 返回卡类列表。
	 */
	public List<BenefitCardType> queryBenefitCardTypes(String channelCode) {
		Channel channel = queryChannel(channelCode);
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		List<BenefitCardType> types = benefitCardTypeDao.searchBy(criteria);
		Iterator<BenefitCardType> iter = types.iterator();
		while (iter.hasNext()) {
			BenefitCardType type = iter.next();
			if (!type.containsChannel(channel)
					|| (type.isBounded() && type.getStatus() != PolicyStatus.APPROVED)) {
				iter.remove();
			}
		}
		return types;
	}

	/**
	 * 查询打票状态。
	 * 
	 * @param voucher
	 *            选座票凭证
	 * @return 返回打票状态。
	 */
	private TicketOrder queryPrintStatus(TicketVoucher voucher) {
		if (voucher == null) {
			ApiException.thrown(TicketReplyCode.VOUCHER_NOT_EXIST);
		}
		if (voucher.getOrder().getStatus() != TicketOrderStatus.SUCCESS) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		if (voucher.getStatus() == PrintStatus.YES) {
			TicketOrder order = voucher.getOrder();
			for (TicketOrderItem item : order.getOrderItems()) {
				if (voucher.getPrintable()) {
					item.setPrintStatus(PrintStatus.NO);
				} else {
					item.setPrintStatus(PrintStatus.YES);
				}
			}
			return order;
		} else {
			TicketAccessService accessService = getAccessService(voucher
					.getOrder());
			return accessService.queryPrint(voucher.getOrder());
		}
	}

	/**
	 * 创建凭证。
	 * 
	 * @param order
	 *            订单
	 * @param externalOrder
	 *            外部订单
	 */
	private void createVoucher(TicketOrder order, TicketOrder externalOrder) {
		TicketVoucher voucher = new TicketVoucher();
		BeanUtils.copyFields(externalOrder.getVoucher(), voucher);
		voucher.setOrder(order);
		voucher.setId(order.getId());
		voucher.setCode(voucherService.genTicketVoucherCode(order));
		voucher.setGenTime(new Date());
		Date showTime = voucher.getOrder().getShowTime();
		voucher.setExpireTime(DateUtils.getNextDay(showTime));
		order.setVoucher(voucher);
		ticketVoucherDao.save(voucher);
	}

	/**
	 * 确认打票。
	 * 
	 * @param voucher
	 *            选座票凭证
	 * @return 打票成功返回true，否则返回false。
	 */
	private boolean confirmPrintVoucher(TicketVoucher voucher) {
		verifyTicketVoucher(voucher);
		voucher.setPrintable(false);
		voucher.setStatus(PrintStatus.YES);
		if (voucher.getReprintCount() == 0) {
			voucher.setConfirmPrintTime(new Date());
		}
		TicketAccessService accessService = getAccessService(voucher.getOrder());
		return accessService.confirmPrint(voucher.getOrder());
	}

	/**
	 * 根据渠道编码和渠道排期编码获取渠道排期。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param channelShowCode
	 *            渠道排期编码
	 * @return 返回渠道排期。
	 */
	private ChannelShow getChannelShow(String channelCode,
			String channelShowCode) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("code", channelShowCode);
		return channelShowDao.searchUnique(criteria);
	}

	/**
	 * 根据指定的渠道排期获取当前有效的渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回指定的渠道排期对应当前有效的渠道排期。
	 */
	private ChannelShow getCurrentValidChannelShow(ChannelShow channelShow) {
		FullTextCriteria criteria = channelShowDao.createFullTextCriteria();
		criteria.addFilterField("cinema.id", channelShow.getCinema().getId());
		criteria.addFilterField("channel.id", channelShow.getChannel().getId());
		criteria.addFilterField("showCode", channelShow.getShowCode());
		criteria.addFilterField("status", ShelveStatus.ON.getValue(),
				ShelveStatus.OFF.getValue());
		return channelShowDao.searchUnique(criteria);
	}

	/**
	 * 根据卡类编号获取有效的唯一卡类。
	 * 
	 * @param typeCode
	 *            卡类编号
	 * @return 卡类。
	 */
	private BenefitCardType getBenefitCardType(String typeCode) {
		FullTextCriteria criteria = benefitCardTypeDao.createFullTextCriteria();
		criteria.addFilterField("code", typeCode);
		criteria.addFilterField("valid", ValidStatus.VALID.getValue());
		List<BenefitCardType> cardTypes = benefitCardTypeDao.searchBy(criteria);
		if (CollectionUtils.isEmpty(cardTypes)) {
			return null;
		} else if (cardTypes.size() == 1) {
			return cardTypes.get(0);
		} else {
			for (BenefitCardType cardType : cardTypes) {
				if (cardType.getStatus() == PolicyStatus.APPROVED) {
					return cardType;
				}
			}
		}
		return null;
	}

	/**
	 * 检查卡信息。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param mobile
	 *            手机号码
	 * @param typeCode
	 *            卡类编号
	 * @param channelOrderCode
	 *            渠道订单号
	 */
	private void checkBenefitCard(String channelCode, String mobile,
			String typeCode, String channelOrderCode) {
		// 检查权益卡是否存在。
		if (benefitCardService.getBenefitCard(channelCode, typeCode, mobile) != null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_EXIST);
		}
		// 检查渠订单号是否存在。
		BenefitCard queryChannelOrderCode = benefitCardService
				.getBenefitCardByChannelOrderCode(channelOrderCode, channelCode);
		if (queryChannelOrderCode != null) {
			ApiException.thrown(TicketReplyCode.CHANNEL_ORDERCODE_EXISTS);
		}
	}

	/**
	 * 检查金额。
	 * 
	 * @param paramAmout
	 *            入参金额
	 * @param cardAmount
	 *            卡类金额
	 * @param code
	 *            错误码
	 */
	private void checkAmount(Double paramAmout, Double cardAmount,
			TicketReplyCode code) {
		if (paramAmout.doubleValue() != cardAmount.doubleValue()) {
			ApiException.thrown(code);
		}
	}

	/**
	 * 检查权益卡类。
	 * 
	 * @param typeCode
	 *            卡类编号。
	 * @return 卡类。
	 */
	private void checkBenefitCardType(BenefitCardType cardType) {
		if (cardType == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARDTYPE_NOT_EXIST);
		}
		if (cardType.getEnabled() != EnabledStatus.ENABLED) {
			ApiException.thrown(TicketReplyCode.BENEFITCARDTYPE_DISABLED);
		}
	}

	/**
	 * 获取权益卡用户。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param mobile
	 *            手机号码
	 * @return 返回权益卡用户对象。
	 */
	private BenefitCardUser getBenefitCardUser(String channelCode, String mobile) {
		BenefitCardUser benefitCardUser = benefitCardUserService
				.getBenefitCardUser(mobile);
		if (benefitCardUser == null) {
			return benefitCardUserService.createBenefitCardUser(
					queryChannel(channelCode), mobile);
		} else {
			return benefitCardUser;
		}
	}

	/**
	 * 验证凭证。
	 * 
	 * @param voucher
	 *            凭证
	 */
	private void verifyTicketVoucher(TicketVoucher voucher) {
		if (voucher == null) {
			ApiException.thrown(TicketReplyCode.VOUCHER_NOT_EXIST);
		}
		if (!voucher.isVerifyable()) {
			ApiException.thrown(TicketReplyCode.VOUCHER_STATUS_INVALID);
		}
	}

	/**
	 * 获取接入接口服务。
	 * 
	 * @param order
	 *            订单
	 * @return 返回接入接口服务。
	 */
	private TicketAccessService getAccessService(TicketOrder order) {
		return TicketAccessServiceFactory.getTicketService(order.getCinema()
				.getTicketSettings());
	}

	/**
	 * 更改手机号码。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param typeCode
	 *            卡类编号
	 * @param oldMobile
	 *            旧手机号码
	 * @param newMobile
	 *            新手机号码
	 */
	public void changeMobile(String channelCode, String typeCode,
			String oldMobile, String newMobile) {
		BenefitCard card = benefitCardService.getBenefitCard(channelCode,
				typeCode, oldMobile);
		if (card == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_NOT_EXIST);
		}
		if (card.isExpire()) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_EXPIRED);
		}
		BenefitCardUser origUser = benefitCardUserService
				.getBenefitCardUser(newMobile);
		if (origUser != null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_USER_EXIST);
		}
		BenefitCardUser user = card.getUser();
		user.setMobile(newMobile);
	}

	/**
	 * 确认卖品。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param channelOrderCode
	 *            渠道订单号
	 * @param mobile
	 *            手机号
	 * @param snacks
	 *            卖品信息
	 * @param benefit
	 *            权益卡信息
	 * @return 返回卖品订单。
	 */
	@Transactional
	public SnackOrder submitSnack(String channelCode, String channelOrderCode,
			String mobile, String snacks, String benefit) {
		if (!checkSnacks(snacks)) {
			ApiException.thrown(TicketReplyCode.SNACK_FORMAT_ERROR);
		}
		SnackOrder order = createSnackOrder(channelCode, channelOrderCode,
				mobile, snacks);
		if (StringUtils.isNotEmpty(benefit)) {
			setBenefitCardSnackOrder(order, benefit, snacks);
		}
		if (!order.isSnackSettlePriceMatched(snacks)) {
			ApiException.thrown(TicketReplyCode.SNACK_SETTLEPRICE_NOT_MATCH);
		}
		createSnackVoucher(order);
		return order;
	}

	/**
	 * 创建卖品订单。
	 * 
	 * @param channelCode
	 *            渠道编号
	 * @param channelOrderCode
	 *            渠道订单号
	 * @param mobile
	 *            手机号
	 * @param snacks
	 *            卖品信息
	 * @return 返回卖品订单。
	 */
	private SnackOrder createSnackOrder(String channelCode,
			String channelOrderCode, String mobile, String snacks) {
		Channel channel = queryChannel(channelCode);
		SnackOrder order = new SnackOrder();
		String[] snackInfos = snacks.split(",");
		for (String info : snackInfos) {
			String[] snackItems = info.split(":");
			SnackChannel snackChannel = snackChannelService.searchSnackChannel(
					channelCode, snackItems[0]);
			if (snackChannel == null || snackChannel.getSnack() == null) {
				ApiException.thrown(TicketReplyCode.SNACK_NOT_EXIST);
			}
			if (!snackChannel.isSalable()) {
				ApiException.thrown(TicketReplyCode.SNACK_STATUS_INVALID);
			}
			if (!channel.getOpenedCinemas().contains(
					snackChannel.getSnack().getCinema())) {
				ApiException.thrown(TicketReplyCode.CINEMA_NOT_OPENED);
			}
			if (order.getCinema() == null) {
				order.setCinema(snackChannel.getSnack().getCinema());
			}
			SnackOrderItem orderItem = SnackOrderItem
					.create(snackChannel, info);
			order.addOrderItem(orderItem);
		}
		order.setCode(serialNumberService.getTicketOrderCode());
		order.setMobile(mobile);
		order.setChannel(channel);
		order.setChannelOrderCode(channelOrderCode);
		order.setStatus(TicketOrderStatus.SUCCESS);
		order.setCreateTime(new Date());
		snackOrderDao.save(order);
		return order;
	}

	/**
	 * 创建卖品凭证。
	 * 
	 * @param order
	 *            卖品订单
	 */
	private void createSnackVoucher(SnackOrder order) {
		SnackVoucher voucher = new SnackVoucher();
		voucher.setSnackOrder(order);
		voucher.setId(order.getId());
		voucher.setCode(voucherService.genSnackVoucherCode());
		voucher.setGenTime(new Date());
		order.setVoucher(voucher);
		snackVoucherDao.save(voucher);
	}

	/**
	 * 查询渠道指定订单号的卖品订单。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param orderCode
	 *            订单号
	 * @return 返回渠道指定订单号的卖品订单。
	 */
	public SnackOrder querySnackOrder(String channelCode, String orderCode) {
		FullTextCriteria criteria = snackOrderDao.createFullTextCriteria();
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("code", orderCode);
		SnackOrder order = snackOrderDao.searchUnique(criteria);
		if (order == null) {
			ApiException.thrown(TicketReplyCode.ORDER_NOT_EXIST);
		}
		if (order.getTicketOrder() != null) {
			ApiException.thrown(TicketReplyCode.SNACK_TICKET_EXIST);
		}
		return order;
	}

	/**
	 * 退订卖品。
	 * 
	 * @param order
	 *            卖品订单
	 */
	public void revokeSnack(SnackOrder order) {
		if (!order.isRevokeable()) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		// if (!order.isOverRevokeTime()) {
		// ApiException.thrown(TicketReplyCode.ORDER_OVER_REVOKETIME);
		// }
		if (!snackPrintStatusService.querySnackPrintStatus(order)) {
			ApiException.thrown(TicketReplyCode.SNACK_ORDER_PRINT);
		}
		order.setRevokeTime(new Date());
		order.setStatus(TicketOrderStatus.REVOKED);
		decrSnackDiscountCount(order);
	}

	/**
	 * 标记退订卖品。
	 * 
	 * @param order
	 *            卖品订单
	 */
	public void markSnackRevoked(SnackOrder order) {
		if (!order.isRevokeable()) {
			ApiException.thrown(TicketReplyCode.ORDER_STATUS_INVALID);
		}
		order.setRevokeTime(new Date());
		order.setStatus(TicketOrderStatus.REVOKED);
		decrSnackDiscountCount(order);
	}
}