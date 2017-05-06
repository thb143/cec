package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import cn.mopon.cec.core.enums.ChannelShowType;
import cn.mopon.cec.core.enums.ChannelType;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.enums.PrintStatus;
import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.enums.TicketOrderType;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumTextBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 选座票订单。
 */
@Entity
@Table(name = "CEC_TicketOrder")
@Indexed(index = "TicketOrder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TicketOrder extends UuidEntity {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Cinema cinema;
	/** 关联影厅 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallId")
	@IndexedEmbedded(includePaths = "name")
	private Hall hall;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Channel channel;
	/** 关联影片 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filmId")
	@IndexedEmbedded(includePaths = { "code", "name" })
	private Film film;
	/** 接入商 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumTextBridge.class))
	private Provider provider;
	/** 影院排期编码 */
	@Field(analyze = Analyze.NO)
	private String showCode;
	/** 渠道排期编码 */
	@Field(analyze = Analyze.NO)
	private String channelShowCode;
	/** 影片编码 */
	@Field(analyze = Analyze.NO)
	private String filmCode;
	/** 影片语言 */
	private String language;
	/** 放映类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ShowType showType;
	/** 放映时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date showTime;
	/** 时长（分钟） */
	private Integer duration;
	/** 停售时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date stopSellTime;
	/** 停退时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date stopRevokeTime;
	/** 最低价 */
	private Double minPrice = 0D;
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 关联影院结算规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaRuleId")
	@IndexedEmbedded(includePaths = "id")
	private CinemaRule cinemaRule;
	/** 关联渠道结算规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelRuleId")
	@IndexedEmbedded(includePaths = "id")
	private ChannelRule channelRule;
	/** 关联特殊定价规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialRuleId")
	@IndexedEmbedded(includePaths = "id")
	private SpecialRule specialRule;
	/** 关联特殊定价渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialChannelId")
	@IndexedEmbedded(includePaths = "id")
	private SpecialChannel specialChannel;
	/** 单张影院结算价 */
	private Double singleCinemaPrice = 0D;
	/** 单张渠道结算价 */
	private Double singleChannelPrice = 0D;
	/** 单张票房价 */
	private Double singleSubmitPrice = 0D;
	/** 单张接入费 */
	private Double singleConnectFee = 0D;
	/** 单张手续费 */
	private Double singleCircuitFee = 0D;
	/** 单张补贴费 */
	private Double singleSubsidyFee = 0D;
	/** 渠道排期创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date channelShowCreateDate;
	/** 渠道排期最后修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date channelShowModifyDate;
	/** 类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private TicketOrderType type = TicketOrderType.NORMAL;
	/** 订单号 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 影院订单号 */
	@Field(analyze = Analyze.NO)
	private String cinemaOrderCode;
	/** 渠道订单号 */
	@Field(analyze = Analyze.NO)
	private String channelOrderCode;
	/** 手机号码 */
	@Field(analyze = Analyze.NO)
	private String mobile;
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date createTime;
	/** 过期时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date expireTime;
	/** 支付时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date payTime;
	/** 确认时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date confirmTime;
	/** 订单金额 */
	private Double amount = 0D;
	/** 选座票销售金额 */
	private Double ticketAmount = 0D;
	/** 选座票数量 */
	private Integer ticketCount = 0;
	/** 卖品数量 */
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IntegerBridge.class))
	private Integer snackCount = 0;
	/** 影院结算金额 */
	private Double cinemaAmount = 0D;
	/** 渠道结算金额 */
	private Double channelAmount = 0D;
	/** 票房金额 */
	private Double submitAmount = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 手续费 */
	private Double circuitFee = 0D;
	/** 补贴费 */
	private Double subsidyFee = 0D;
	/** 渠道费 */
	private Double channelFee = 0D;
	/** 服务费 */
	private Double serviceFee = 0D;
	/** 优惠金额 */
	private Double discountAmount = 0D;
	/** 订单状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private TicketOrderStatus status = TicketOrderStatus.UNPAID;
	/** 退票时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date revokeTime;
	/** 关联凭证 */
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@IndexedEmbedded(includePaths = { "code", "printCode", "verifyCode" })
	private TicketVoucher voucher;
	/** 关联权益卡消费订单 */
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ContainedIn
	private BenefitCardConsumeOrder benefitCardConsumeOrder;
	/** 关联卖品订单 */
	@OneToOne(mappedBy = "ticketOrder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ContainedIn
	private SnackOrder snackOrder;
	/** 关联订单明细 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<TicketOrderItem> orderItems = new ArrayList<TicketOrderItem>();

	/**
	 * 获取影院结算规则名称。
	 * 
	 * @return 返回影院结算规则名称。
	 */
	public String getCinemaRuleName() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getName();
		case SPECIAL:
			return getSpecialRule().getName();
		default:
			return null;
		}
	}

	/**
	 * 获取影院结算规则颜色。
	 * 
	 * @return 返回影院结算规则颜色。
	 */
	public String getCinemaRuleColor() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getEnabled().getColor();
		case SPECIAL:
			return getSpecialRule().getEnabled().getColor();
		default:
			return null;
		}
	}

	/**
	 * 获取影院结算规则摘要。
	 * 
	 * @return 返回影院结算规则摘要。
	 */
	public String getCinemaRuleSummary() {
		switch (type) {
		case NORMAL:
			return getCinemaRule().getSummary();
		case SPECIAL:
			return getSpecialRule().getSummary();
		default:
			return null;
		}
	}

	/**
	 * 获取渠道结算规则名称。
	 * 
	 * @return 返回渠道结算规则名称。
	 */
	public String getChannelRuleName() {
		switch (type) {
		case NORMAL:
			return getChannelRule().getName();
		case SPECIAL:
			return getSpecialRule().getName();
		default:
			return null;
		}
	}

	/**
	 * 获取渠道结算规则颜色。
	 * 
	 * @return 返回渠道结算规则颜色。
	 */
	public String getChannelRuleColor() {
		switch (type) {
		case NORMAL:
			return getChannelRule().getEnabled().getColor();
		case SPECIAL:
			return getSpecialChannel().getEnabled().getColor();
		default:
			return null;
		}
	}

	/**
	 * 获取渠道结算规则摘要。
	 * 
	 * @return 返回渠道结算规则摘要。
	 */
	public String getChannelRuleSummary() {
		switch (type) {
		case NORMAL:
			return getChannelRule().getSummary();
		case SPECIAL:
			return getSpecialChannel().getSummary();
		default:
			return null;
		}
	}

	/**
	 * 判断订单是否可以确认。
	 * 
	 * @return 返回订单是否可以确认。
	 */
	public Boolean isSubmitable() {
		return getCinema().getSalable() && getChannel().getSalable()
				&& getCinema().getStatus() == EnabledStatus.ENABLED
				&& getCinema().getTicketSetted()
				&& getCinema().provideShowType(showType)
				&& getHall().getStatus() == HallStatus.ENABLED
				&& getChannel().getOpened()
				&& status == TicketOrderStatus.UNPAID
				&& expireTime.after(new Date());
	}

	/**
	 * 判断订单座位是否匹配。
	 * 
	 * @param orderSeats
	 *            订单座位
	 * @return 返回订单座位是否匹配。
	 */
	public Boolean isSeatsMatched(String orderSeats) {
		String[] seats = orderSeats.split(",");
		if (seats.length != getOrderItems().size()) {
			return false;
		}
		for (String orderSeat : seats) {
			String seatCode = orderSeat.split(":")[0];
			if (getOrderItem(seatCode) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断结算价是否正确。
	 * 
	 * @param orderSeats
	 *            订单座位
	 * @return 返回结算价是否匹配。
	 */
	public Boolean isSettlePriceMatched(String orderSeats) {
		if (getChannel().getType() == ChannelType.OTHER) {
			String[] seats = orderSeats.split(",");
			for (String orderSeat : seats) {
				String[] seatInfo = orderSeat.split(":");
				if (seatInfo.length == 3) {
					String seatCode = seatInfo[0];
					Double settlePrice = NumberUtils.halfUp(Double
							.parseDouble(seatInfo[2]));
					if (!checkOrderItemSettlePrice(seatCode, settlePrice)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 判断订单是否可以标记失败。
	 * 
	 * @return 返回订单是否可以标记失败。
	 */
	public Boolean isMarkFailable() {
		return status == TicketOrderStatus.PAID;
	}

	/**
	 * 判断订单是否可以退票。
	 * 
	 * @return 返回订单是否可以退票。
	 */
	public Boolean isRevokeable() {
		return status == TicketOrderStatus.SUCCESS
				&& getVoucher().getStatus() == PrintStatus.NO;
	}

	/**
	 * 判断订单是否已过期。
	 * 
	 * @return 返回订单是否已过期。
	 */
	public Boolean isExpired() {
		return getExpireTime().before(new Date());
	}

	/**
	 * 判断订单是否超过退票时间。
	 * 
	 * @return 返回订单是否可以退票。
	 */
	public Boolean isOverRevokeTime() {
		return getStopRevokeTime().after(new Date());
	}

	/**
	 * 创建选座票订单。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param showSeats
	 *            排期座位列表
	 * @return 返回选座票订单。
	 */
	public static TicketOrder create(ChannelShow channelShow,
			List<ShowSeat> showSeats) {
		TicketOrder order = create(channelShow);
		for (ShowSeat showSeat : showSeats) {
			TicketOrderItem orderItem = TicketOrderItem.create(channelShow,
					showSeat);
			order.addOrderItem(orderItem);
		}
		return order;
	}

	/**
	 * 确认订单。
	 * 
	 * @param externalOrder
	 *            外部订单
	 */
	public void confirm(TicketOrder externalOrder) {
		// 火凤凰锁座时不生成订单号，所以需要在确认订单时设置订单号。
		if (StringUtils.isNotEmpty(externalOrder.getCinemaOrderCode())) {
			cinemaOrderCode = externalOrder.getCinemaOrderCode();
		}
		// 设置票号
		for (TicketOrderItem externalOrderItem : externalOrder.getOrderItems()) {
			TicketOrderItem orderItem = getOrderItem(externalOrderItem
					.getSeatCode());
			if (orderItem != null) {
				orderItem.setTicketCode(externalOrderItem.getTicketCode());
			}
		}
		confirmTime = new Date();
		status = TicketOrderStatus.SUCCESS;
		if (getSnackOrder() != null) {
			getSnackOrder().setStatus(status);
		}
	}

	/**
	 * 添加订单明细。
	 * 
	 * @param orderItem
	 *            订单明细
	 */
	private void addOrderItem(TicketOrderItem orderItem) {
		ticketCount++;
		cinemaAmount = NumberUtils
				.add(cinemaAmount, orderItem.getCinemaPrice());
		channelAmount = NumberUtils.add(channelAmount,
				orderItem.getChannelPrice());
		submitAmount = NumberUtils
				.add(submitAmount, orderItem.getSubmitPrice());
		connectFee = NumberUtils.add(connectFee, orderItem.getConnectFee());
		circuitFee = NumberUtils.add(circuitFee, orderItem.getCircuitFee());
		subsidyFee = NumberUtils.add(subsidyFee, orderItem.getSubsidyFee());
		orderItem.setOrder(this);
		orderItems.add(orderItem);
	}

	/**
	 * 设置订单销售金额以及订单明细的销售价。
	 * 
	 * @param orderSeats
	 *            座位信息
	 */
	public void setSaleAmount(String orderSeats) {
		for (String orderSeat : orderSeats.split(",")) {
			String seatCode = orderSeat.split(":")[0];
			TicketOrderItem orderItem = getOrderItem(seatCode);
			orderItem.setSalePrice(orderSeat);
			// 累计订单金额、服务费、渠道费
			ticketAmount = NumberUtils.add(ticketAmount,
					orderItem.getSalePrice());
			serviceFee = NumberUtils.add(serviceFee, orderItem.getServiceFee());
			channelFee = NumberUtils.add(channelFee, orderItem.getChannelFee());
		}
		// 如果是自有渠道，可能会提交票房价，所以要重新计算票房金额。
		if (getChannel().getType() == ChannelType.OWN) {
			submitAmount = 0D;
			for (TicketOrderItem orderItem : getOrderItems()) {
				submitAmount = NumberUtils.add(submitAmount,
						orderItem.getSubmitPrice());
			}
		}
		if (getSnackOrder() != null) {
			amount = NumberUtils.add(ticketAmount, getSnackOrder().getAmount());
		} else {
			amount = ticketAmount;
		}
	}

	/**
	 * 创建选座票订单。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回选座票订单。
	 */
	private static TicketOrder create(ChannelShow channelShow) {
		TicketOrder order = new TicketOrder();
		order.copyShowInfo(channelShow);
		order.copyPriceInfo(channelShow);
		order.setCreateTime(new Date());
		if (channelShow.getType() == ChannelShowType.SPECIAL) {
			order.setType(TicketOrderType.SPECIAL);
		}
		return order;
	}

	/**
	 * 复制排期信息。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	private void copyShowInfo(ChannelShow channelShow) {
		setCinema(channelShow.getCinema());
		setHall(channelShow.getHall());
		setChannel(channelShow.getChannel());
		setFilm(channelShow.getFilm());
		setProvider(channelShow.getProvider());
		setShowCode(channelShow.getShowCode());
		setChannelShowCode(channelShow.getCode());
		setFilmCode(channelShow.getFilmCode());
		setLanguage(channelShow.getLanguage());
		setShowType(channelShow.getShowType());
		setShowTime(channelShow.getShowTime());
		setDuration(channelShow.getDuration());
		setStopSellTime(channelShow.getStopSellTime());
		setStopRevokeTime(channelShow.getStopRevokeTime());
		setMinPrice(channelShow.getMinPrice());
		setStdPrice(channelShow.getStdPrice());
		setChannelShowCreateDate(channelShow.getCreateDate());
		setChannelShowModifyDate(channelShow.getModifyDate());
	}

	/**
	 * 复制价格信息。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	private void copyPriceInfo(ChannelShow channelShow) {
		setCinemaRule(channelShow.getCinemaRule());
		setChannelRule(channelShow.getChannelRule());
		setSpecialRule(channelShow.getSpecialRule());
		setSpecialChannel(channelShow.getSpecialChannel());
		setSingleCinemaPrice(channelShow.getCinemaPrice());
		setSingleChannelPrice(channelShow.getChannelPrice());
		setSingleSubmitPrice(channelShow.getSubmitPrice());
		setSingleConnectFee(channelShow.getConnectFee());
		setSingleCircuitFee(channelShow.getCircuitFee());
		setSingleSubsidyFee(channelShow.getSubsidyFee());
	}

	/**
	 * 根据座位编码获取订单明细。
	 * 
	 * @param seatCode
	 *            座位编码
	 * @return 返回座位编码对应的订单明细。
	 */
	private TicketOrderItem getOrderItem(String seatCode) {
		for (TicketOrderItem item : getOrderItems()) {
			if (item.getSeatCode().equals(seatCode)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 校验订单明细中的结算价。
	 * 
	 * @param seatCode
	 *            座位编码
	 * @param settlePrice
	 *            结算价
	 * @return 返回订单明细中的结算价是否正确。
	 */
	private Boolean checkOrderItemSettlePrice(String seatCode,
			Double settlePrice) {
		for (TicketOrderItem item : getOrderItems()) {
			if (item.getSeatCode().equals(seatCode)
					&& NumberUtils.add(item.getChannelPrice(),
							item.getConnectFee()).doubleValue() != settlePrice
							.doubleValue()) {
				return false;
			}
		}
		return true;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String channelShowCode) {
		this.channelShowCode = channelShowCode;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(Date stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public Date getStopRevokeTime() {
		return stopRevokeTime;
	}

	public void setStopRevokeTime(Date stopRevokeTime) {
		this.stopRevokeTime = stopRevokeTime;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public CinemaRule getCinemaRule() {
		return cinemaRule;
	}

	public void setCinemaRule(CinemaRule cinemaRule) {
		this.cinemaRule = cinemaRule;
	}

	public ChannelRule getChannelRule() {
		return channelRule;
	}

	public void setChannelRule(ChannelRule channelRule) {
		this.channelRule = channelRule;
	}

	public SpecialRule getSpecialRule() {
		return specialRule;
	}

	public void setSpecialRule(SpecialRule specialRule) {
		this.specialRule = specialRule;
	}

	public SpecialChannel getSpecialChannel() {
		return specialChannel;
	}

	public void setSpecialChannel(SpecialChannel specialChannel) {
		this.specialChannel = specialChannel;
	}

	public Double getSingleCinemaPrice() {
		return singleCinemaPrice;
	}

	public void setSingleCinemaPrice(Double singleCinemaPrice) {
		this.singleCinemaPrice = singleCinemaPrice;
	}

	public Double getSingleChannelPrice() {
		return singleChannelPrice;
	}

	public void setSingleChannelPrice(Double singleChannelPrice) {
		this.singleChannelPrice = singleChannelPrice;
	}

	public Double getSingleSubmitPrice() {
		return singleSubmitPrice;
	}

	public void setSingleSubmitPrice(Double singleSubmitPrice) {
		this.singleSubmitPrice = singleSubmitPrice;
	}

	public Double getSingleConnectFee() {
		return singleConnectFee;
	}

	public void setSingleConnectFee(Double singleConnectFee) {
		this.singleConnectFee = singleConnectFee;
	}

	public Double getSingleCircuitFee() {
		return singleCircuitFee;
	}

	public void setSingleCircuitFee(Double singleCircuitFee) {
		this.singleCircuitFee = singleCircuitFee;
	}

	public Double getSingleSubsidyFee() {
		return singleSubsidyFee;
	}

	public void setSingleSubsidyFee(Double singleSubsidyFee) {
		this.singleSubsidyFee = singleSubsidyFee;
	}

	public Date getChannelShowCreateDate() {
		return channelShowCreateDate;
	}

	public void setChannelShowCreateDate(Date channelShowCreateDate) {
		this.channelShowCreateDate = channelShowCreateDate;
	}

	public Date getChannelShowModifyDate() {
		return channelShowModifyDate;
	}

	public void setChannelShowModifyDate(Date channelShowModifyDate) {
		this.channelShowModifyDate = channelShowModifyDate;
	}

	public TicketOrderType getType() {
		return type;
	}

	public void setType(TicketOrderType type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCinemaOrderCode() {
		return cinemaOrderCode;
	}

	public void setCinemaOrderCode(String cinemaOrderCode) {
		this.cinemaOrderCode = cinemaOrderCode;
	}

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Integer getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
	}

	public Integer getSnackCount() {
		return snackCount;
	}

	public void setSnackCount(Integer snackCount) {
		this.snackCount = snackCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCinemaAmount() {
		return cinemaAmount;
	}

	public void setCinemaAmount(Double cinemaAmount) {
		this.cinemaAmount = cinemaAmount;
	}

	public Double getChannelAmount() {
		return channelAmount;
	}

	public void setChannelAmount(Double channelAmount) {
		this.channelAmount = channelAmount;
	}

	public Double getSubmitAmount() {
		return submitAmount;
	}

	public void setSubmitAmount(Double submitAmount) {
		this.submitAmount = submitAmount;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public Double getCircuitFee() {
		return circuitFee;
	}

	public void setCircuitFee(Double circuitFee) {
		this.circuitFee = circuitFee;
	}

	public Double getSubsidyFee() {
		return subsidyFee;
	}

	public void setSubsidyFee(Double subsidyFee) {
		this.subsidyFee = subsidyFee;
	}

	public Double getChannelFee() {
		return channelFee;
	}

	public void setChannelFee(Double channelFee) {
		this.channelFee = channelFee;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public TicketOrderStatus getStatus() {
		return status;
	}

	public void setStatus(TicketOrderStatus status) {
		this.status = status;
	}

	public Date getRevokeTime() {
		return revokeTime;
	}

	public void setRevokeTime(Date revokeTime) {
		this.revokeTime = revokeTime;
	}

	public TicketVoucher getVoucher() {
		return voucher;
	}

	public void setVoucher(TicketVoucher voucher) {
		this.voucher = voucher;
	}

	public List<TicketOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TicketOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Double getTicketAmount() {
		return ticketAmount;
	}

	public void setTicketAmount(Double ticketAmount) {
		this.ticketAmount = ticketAmount;
	}

	public SnackOrder getSnackOrder() {
		return snackOrder;
	}

	public void setSnackOrder(SnackOrder snackOrder) {
		this.snackOrder = snackOrder;
	}

	public BenefitCardConsumeOrder getBenefitCardConsumeOrder() {
		return benefitCardConsumeOrder;
	}

	public void setBenefitCardConsumeOrder(
			BenefitCardConsumeOrder benefitCardConsumeOrder) {
		this.benefitCardConsumeOrder = benefitCardConsumeOrder;
	}
}