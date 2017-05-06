package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.TicketOrderKind;
import cn.mopon.cec.core.enums.TicketOrderType;
import coo.base.util.DateUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 选座票订单统计类。
 */
@Entity
@Table(name = "CEC_TicketOrderStat")
@Indexed(index = "TicketOrderStat")
public class TicketOrderStat extends UuidEntity {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "name" })
	private Cinema cinema;
	/** 关联影厅 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallId")
	@IndexedEmbedded(includePaths = "id")
	private Hall hall;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "name" })
	private Channel channel;
	/** 关联影片 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filmId")
	@IndexedEmbedded(includePaths = "id")
	private Film film;
	/** 关联特殊定价策略 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialPolicyId")
	@IndexedEmbedded(includePaths = "id")
	private SpecialPolicy specialPolicy;
	/** 接入商类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private Provider provider = Provider.NG;
	/** 订单号 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 影院订单号 */
	@Field(analyze = Analyze.NO)
	private String cinemaOrderCode;
	/** 渠道订单号 */
	private String channelOrderCode;
	/** 订单类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private TicketOrderType type = TicketOrderType.NORMAL;
	/** 订单类别 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private TicketOrderKind kind = TicketOrderKind.NORMAL;
	/** 放映类型 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private ShowType showType = ShowType.NORMAL2D;
	/** 手机号码 */
	private String mobile;
	/** 确认时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date confirmTime;
	/** 选座票数量 */
	private Integer ticketCount = 0;
	/** 订单金额 */
	private Double amount = 0D;
	/** 影院结算金额 */
	private Double cinemaAmount = 0D;
	/** 渠道结算金额 */
	private Double channelAmount = 0D;
	/** 票房金额 */
	private Double submitAmount = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 服务费 */
	private Double serviceFee = 0D;
	/** 渠道费 */
	private Double channelFee = 0D;
	/** 手续费 */
	private Double circuitFee = 0D;
	/** 补贴费 */
	private Double subsidyFee = 0D;
	/** 退票时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date revokeTime;
	/** 统计日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	protected Date statDate;

	/**
	 * 构造方法。
	 */
	public TicketOrderStat() {
	}

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            订单
	 * @param kind
	 *            订单类别
	 * @param date
	 *            统计日期
	 */
	public TicketOrderStat(TicketOrder order, TicketOrderKind kind, Date date) {
		setCinema(order.getCinema());
		setHall(order.getHall());
		setChannel(order.getChannel());
		setFilm(order.getFilm());
		if (order.getSpecialChannel() != null) {
			setSpecialPolicy(order.getSpecialChannel().getRule().getPolicy());
		}
		setProvider(order.getProvider());
		setCode(order.getCode());
		setCinemaOrderCode(order.getCinemaOrderCode());
		setChannelOrderCode(order.getChannelOrderCode());
		setType(order.getType());
		setKind(kind);
		setShowType(order.getShowType());
		setMobile(order.getMobile());
		setConfirmTime(order.getConfirmTime());
		setStatDate(date);
		if (kind == TicketOrderKind.REVOKE) {
			setRevoke(order);
		} else {
			setNormal(order);
		}
	}

	/**
	 * 正常设置。
	 * 
	 * @param order
	 *            订单
	 */
	private void setNormal(TicketOrder order) {
		setChannelAmount(order.getChannelAmount());
		setCinemaAmount(order.getCinemaAmount());
		setTicketCount(order.getTicketCount());
		setAmount(order.getAmount());
		setServiceFee(order.getServiceFee());
		setChannelFee(order.getChannelFee());
		setCircuitFee(order.getCircuitFee());
		setSubsidyFee(order.getSubsidyFee());
		setSubmitAmount(order.getSubmitAmount());
		setConnectFee(order.getConnectFee());
	}

	/**
	 * 退票设置。
	 * 
	 * @param order
	 *            订单
	 */
	private void setRevoke(TicketOrder order) {
		setRevokeTime(order.getRevokeTime());
		setChannelAmount(-order.getChannelAmount());
		setCinemaAmount(-order.getCinemaAmount());
		setTicketCount(-order.getTicketCount());
		setAmount(-order.getAmount());
		setServiceFee(-order.getServiceFee());
		setChannelFee(-order.getChannelFee());
		setCircuitFee(-order.getCircuitFee());
		setSubsidyFee(-order.getSubsidyFee());
		setSubmitAmount(-order.getSubmitAmount());
		setConnectFee(-order.getConnectFee());
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public SpecialPolicy getSpecialPolicy() {
		return specialPolicy;
	}

	public void setSpecialPolicy(SpecialPolicy specialPolicy) {
		this.specialPolicy = specialPolicy;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(Double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public Double getChannelFee() {
		return channelFee;
	}

	public void setChannelFee(Double channelFee) {
		this.channelFee = channelFee;
	}

	public Double getCircuitFee() {
		return circuitFee;
	}

	public void setCircuitFee(Double circuitFee) {
		this.circuitFee = circuitFee;
	}

	/**
	 * 获取string 格式的 出票时间。
	 * 
	 * @return 时间。
	 */
	public String getConfirmTimeStr() {
		return DateUtils.format(confirmTime, DateUtils.SECOND);
	}

	/**
	 * 获取string 格式的 退票时间。
	 * 
	 * @return 时间。
	 */
	public String getRevokeTimeStr() {
		if (revokeTime != null) {
			return DateUtils.format(revokeTime, DateUtils.SECOND);
		}
		return null;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
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

	public TicketOrderType getType() {
		return type;
	}

	public void setType(TicketOrderType type) {
		this.type = type;
	}

	public TicketOrderKind getKind() {
		return kind;
	}

	public void setKind(TicketOrderKind kind) {
		this.kind = kind;
	}

	public Integer getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
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

	public Double getSubsidyFee() {
		return subsidyFee;
	}

	public void setSubsidyFee(Double subsidyFee) {
		this.subsidyFee = subsidyFee;
	}

	public Date getRevokeTime() {
		return revokeTime;
	}

	public void setRevokeTime(Date revokeTime) {
		this.revokeTime = revokeTime;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
}
