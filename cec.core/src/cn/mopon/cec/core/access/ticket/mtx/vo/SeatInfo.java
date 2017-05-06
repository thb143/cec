package cn.mopon.cec.core.access.ticket.mtx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 座位信息。
 */
@XStreamAlias("SeatInfo")
public class SeatInfo {
	/** 影厅座位号 */
	@XStreamAlias("SeatNo")
	private String seatNo;
	/** 显示票价 */
	@XStreamAlias("TicketPrice")
	private Double ticketPrice;
	/** 手续费 */
	@XStreamAlias("Handlingfee")
	private Double handlingfee;

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public Double getHandlingfee() {
		return handlingfee;
	}

	public void setHandlingfee(Double handlingfee) {
		this.handlingfee = handlingfee;
	}
}
