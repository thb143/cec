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

import cn.mopon.cec.core.enums.PrintStatus;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import coo.base.util.NumberUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 卖品订单。
 */
@Entity
@Table(name = "CEC_SnackOrder")
@Indexed(index = "SnackOrder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnackOrder extends UuidEntity {
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Channel channel;
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Cinema cinema;
	/** 关联选座票 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticketOrderId")
	@IndexedEmbedded(includePaths = { "code" })
	private TicketOrder ticketOrder;
	/** 订单号 */
	@Field(analyze = Analyze.NO)
	private String code;
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
	/** 订单金额 */
	private Double amount = 0D;
	/** 选座票卖品数量 */
	private Integer snackCount = 0;
	/** 标准价 */
	private Double stdAmount = 0D;
	/** 影院结算金额 */
	private Double cinemaAmount = 0D;
	/** 渠道结算金额 */
	private Double channelAmount = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 渠道费 */
	private Double channelFee = 0D;
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
	@OneToOne(mappedBy = "snackOrder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@IndexedEmbedded(includePaths = { "code" })
	private SnackVoucher voucher;
	/** 关联权益卡消费订单 */
	@OneToOne(mappedBy = "snackOrder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ContainedIn
	private BenefitCardConsumeSnackOrder benefitCardConsumeSnackOrder;
	/** 关联卖品明细 */
	@OneToMany(mappedBy = "snackOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SnackOrderItem> orderItems = new ArrayList<SnackOrderItem>();

	/**
	 * 创建卖品订单。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回卖品订单。
	 */
	public static SnackOrder create(TicketOrder ticketOrder) {
		SnackOrder order = new SnackOrder();
		order.setChannel(ticketOrder.getChannel());
		order.setChannelOrderCode(ticketOrder.getChannelOrderCode());
		order.setCinema(ticketOrder.getCinema());
		order.setCode(ticketOrder.getCode());
		order.setStatus(ticketOrder.getStatus());
		order.setTicketOrder(ticketOrder);
		order.setCreateTime(new Date());
		return order;
	}

	/**
	 * 添加卖品明细。
	 * 
	 * @param orderItem
	 *            卖品明细
	 */
	public void addOrderItem(SnackOrderItem orderItem) {
		snackCount = snackCount + orderItem.getCount();
		amount = NumberUtils.add(amount,
				orderItem.getSalePrice() * orderItem.getCount());
		cinemaAmount = NumberUtils.add(cinemaAmount, orderItem.getCinemaPrice()
				* orderItem.getCount());
		channelAmount = NumberUtils.add(channelAmount,
				orderItem.getChannelPrice() * orderItem.getCount());
		stdAmount = NumberUtils.add(stdAmount, orderItem.getStdPrice()
				* orderItem.getCount());
		channelFee = NumberUtils.add(channelFee, orderItem.getChannelFee()
				* orderItem.getCount());
		connectFee = NumberUtils.add(connectFee, orderItem.getConnectFee()
				* orderItem.getCount());
		orderItem.setSnackOrder(this);
		orderItems.add(orderItem);
	}

	/**
	 * 判断卖品结算价是否正确。
	 * 
	 * @param snacks
	 *            卖品信息
	 * @return 返回卖品结算价是否匹配。
	 */
	public Boolean isSnackSettlePriceMatched(String snacks) {
		String[] infos = snacks.split(",");
		for (String snackInfo : infos) {
			String snackCode = snackInfo.split(":")[0];
			Double settlePrice = NumberUtils.halfUp(Double
					.parseDouble(snackInfo.split(":")[3]));
			if (!checkOrderItemSettlePrice(snackCode, settlePrice)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 校验订单明细中的结算价。
	 * 
	 * @param snackCode
	 *            卖品编码
	 * @param settlePrice
	 *            结算价
	 * @return 返回订单明细中的结算价是否正确。
	 */
	private Boolean checkOrderItemSettlePrice(String snackCode,
			Double settlePrice) {
		for (SnackOrderItem item : getOrderItems()) {
			if (item.getSnack().getCode().equals(snackCode)
					&& item.getChannelPrice().doubleValue() != settlePrice
							.doubleValue()) {
				return false;
			}
		}
		return true;
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

	// /**
	// * 判断订单是否超过退票时间。
	// *
	// * @return 返回订单是否可以退票。
	// */
	// public Boolean isOverRevokeTime() {
	// return getVoucher().getExpireTime().after(new Date());
	// }

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public TicketOrder getTicketOrder() {
		return ticketOrder;
	}

	public void setTicketOrder(TicketOrder ticketOrder) {
		this.ticketOrder = ticketOrder;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getSnackCount() {
		return snackCount;
	}

	public void setSnackCount(Integer snackCount) {
		this.snackCount = snackCount;
	}

	public Double getStdAmount() {
		return stdAmount;
	}

	public void setStdAmount(Double stdAmount) {
		this.stdAmount = stdAmount;
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

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public Double getChannelFee() {
		return channelFee;
	}

	public void setChannelFee(Double channelFee) {
		this.channelFee = channelFee;
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

	public SnackVoucher getVoucher() {
		return voucher;
	}

	public void setVoucher(SnackVoucher voucher) {
		this.voucher = voucher;
	}

	public BenefitCardConsumeSnackOrder getBenefitCardConsumeSnackOrder() {
		return benefitCardConsumeSnackOrder;
	}

	public void setBenefitCardConsumeSnackOrder(
			BenefitCardConsumeSnackOrder benefitCardConsumeSnackOrder) {
		this.benefitCardConsumeSnackOrder = benefitCardConsumeSnackOrder;
	}

	public List<SnackOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<SnackOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}