package cn.mopon.cec.core.model;

/**
 * 正常订单。
 */
public class NormalOrderStat {
	/** 订单数量 */
	private Integer orderCount = 0;
	/** 选座票数量 */
	private Integer ticketCount = 0;
	/** 影院结算价 */
	private Double cinemaAmount = 0.0d;
	/** 渠道结算价 */
	private Double channelAmount = 0.0d;
	/** 订单金额 */
	private Double amount = 0d;
	/** 服务费 */
	private Double serviceFee = 0.0d;
	/** 渠道费 */
	private Double channelFee = 0.0d;
	/** 手续费 */
	private Double circuitFee = 0.0d;
	/** 补贴金额 */
	private Double subsidyFee = 0.0d;
	/** 票房金额 */
	private Double submitAmount = 0D;
	/** 接入费 */
	private Double connectFee = 0D;

	public Integer getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
	}

	public Double getCinemaAmount() {
		return cinemaAmount;
	}

	public void setCinemaAmount(Double settlePrice) {
		this.cinemaAmount = settlePrice;
	}

	public Double getChannelAmount() {
		return channelAmount;
	}

	public void setChannelAmount(Double salePrice) {
		this.channelAmount = salePrice;
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

	public Double getSubsidyFee() {
		return subsidyFee;
	}

	public void setSubsidyFee(Double subsidyFee) {
		this.subsidyFee = subsidyFee;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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
}
