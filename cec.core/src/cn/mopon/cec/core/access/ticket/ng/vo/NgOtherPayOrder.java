package cn.mopon.cec.core.access.ticket.ng.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 第三方支付定单vo
 */
public class NgOtherPayOrder {
	/** 选座订单号 */
	@XStreamAsAttribute
	@XStreamAlias("TicketOrderNo")
	private String ticketOrderNo;
	/** 终端Mac地址 */
	@XStreamAlias("TerminalMacAddr")
	private String terminalMacAddr;
	/** 支付结果 */
	@XStreamAlias("PayResult")
	private Integer payResult;
	/** 支付价格 */
	@XStreamAlias("PayPrice")
	private Double payPrice;
	/** 支付类型 */
	@XStreamAlias("PayType")
	private Integer payType;
	/** 支付详单号 */
	@XStreamAlias("PayDetailNo")
	private String payDetailNo;
	/** 第三方订单号 */
	@XStreamAlias("OtherOrderNo")
	private String otherOrderNo;

	public String getTicketOrderNo() {
		return ticketOrderNo;

	}

	public void setTicketOrderNo(String ticketOrderNo) {
		this.ticketOrderNo = ticketOrderNo;
	}

	public String getTerminalMacAddr() {
		return terminalMacAddr;
	}

	public void setTerminalMacAddr(String terminalMacAddr) {
		this.terminalMacAddr = terminalMacAddr;
	}

	public Integer getPayResult() {
		return payResult;
	}

	public void setPayResult(Integer payResult) {
		this.payResult = payResult;
	}

	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getPayDetailNo() {
		return payDetailNo;
	}

	public void setPayDetailNo(String payDetailNo) {
		this.payDetailNo = payDetailNo;
	}

	public String getOtherOrderNo() {
		return otherOrderNo;
	}

	public void setOtherOrderNo(String otherOrderNo) {
		this.otherOrderNo = otherOrderNo;
	}

}
