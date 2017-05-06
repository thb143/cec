package cn.mopon.cec.core.access.member.vo;

import cn.mopon.cec.core.access.member.ng.vo.NgSeatInfo;

/**
 * 座位折扣价信息。
 */
public class SeatInfo {
	/** 影厅座位号 */
	private String seatNo;
	/** 折扣价 */
	private Double ticketPrice;

	public SeatInfo() {

	}

	public SeatInfo(NgSeatInfo ngSeatInfo) {
		this.seatNo = ngSeatInfo.getSeatCode();
		this.ticketPrice = ngSeatInfo.getMemberPrice();
	}

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
}
