package cn.mopon.cec.core.access.member.hln.vo;

/**
 * 选座票折扣。
 */
public class TicketDiscountVo {
	/** 折扣价 */
	private Double ticketPrice;
	/** 座位编码 */
	private String seatNo;

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

}
