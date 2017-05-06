package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import cn.mopon.cec.core.enums.ChannelType;
import cn.mopon.cec.core.enums.PrintStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.util.BeanUtils;
import coo.base.util.NumberUtils;
import coo.core.model.UuidEntity;

/**
 * 选座票订单明细。
 */
@Entity
@Table(name = "CEC_TicketOrderItem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TicketOrderItem extends UuidEntity {
	/** 关联订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId")
	private TicketOrder order;
	/** 影票编码 */
	private String ticketCode;
	/** 座位编号 */
	private String seatCode;
	/** 座位分组编码 */
	private String seatGroupCode;
	/** 座位行号 */
	private String seatRow;
	/** 座位列号 */
	private String seatCol;
	/** 销售价 */
	private Double salePrice = 0D;
	/** 影院结算价 */
	private Double cinemaPrice = 0D;
	/** 渠道结算价 */
	private Double channelPrice = 0D;
	/** 票房价 */
	private Double submitPrice = 0D;
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
	private Double discountPrice = 0D;
	/** 二维码 */
	private String barCode;
	@Transient
	private PrintStatus printStatus;

	/**
	 * 创建选座票订单明细。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param showSeat
	 *            排期座位
	 * @return 返回选座票订单明细。
	 */
	public static TicketOrderItem create(ChannelShow channelShow,
			ShowSeat showSeat) {
		TicketOrderItem orderItem = new TicketOrderItem();
		orderItem.setSeatCode(showSeat.getCode());
		orderItem.setSeatGroupCode(showSeat.getGroupCode());
		orderItem.setSeatCol(showSeat.getColNum());
		orderItem.setSeatRow(showSeat.getRowNum());
		BeanUtils.copyFields(channelShow, orderItem, "id");
		return orderItem;
	}

	/**
	 * 设置销售价。
	 * 
	 * @param orderSeat
	 *            座位信息
	 */
	public void setSalePrice(String orderSeat) {
		String[] seatItems = orderSeat.split(":");
		salePrice = NumberUtils.halfUp(Double.parseDouble(seatItems[1]));
		// 如果是自有渠道并且座位信息包含票房价，重新设置票房价
		if (getOrder().getChannel().getType() == ChannelType.OWN
				&& seatItems.length > 2) {
			submitPrice = NumberUtils.halfUp(Double.parseDouble(seatItems[2]));
			// 如果票房价小于最低价，则设置最低价为票房价
			Double minPrice = getOrder().getMinPrice();
			if (submitPrice < minPrice) {
				submitPrice = minPrice;
			}
		}
		// 设置渠道费。
		Double channelSettlePrice = NumberUtils.add(channelPrice, connectFee);
		channelFee = NumberUtils.sub(salePrice, channelSettlePrice);
		// 设置服务费。如果销售价大于票房价，则服务费=销售价-票房价，否则服务费为0。
		if (salePrice > submitPrice) {
			serviceFee = NumberUtils.sub(salePrice, submitPrice);
		}
	}

	public TicketOrder getOrder() {
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getSeatCode() {
		return seatCode;
	}

	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}

	public String getSeatGroupCode() {
		return seatGroupCode;
	}

	public void setSeatGroupCode(String seatGroupCode) {
		this.seatGroupCode = seatGroupCode;
	}

	public String getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public String getSeatCol() {
		return seatCol;
	}

	public void setSeatCol(String seatCol) {
		this.seatCol = seatCol;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getCinemaPrice() {
		return cinemaPrice;
	}

	public void setCinemaPrice(Double cinemaPrice) {
		this.cinemaPrice = cinemaPrice;
	}

	public Double getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(Double channelPrice) {
		this.channelPrice = channelPrice;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
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

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public PrintStatus getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(PrintStatus printStatus) {
		this.printStatus = printStatus;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
}