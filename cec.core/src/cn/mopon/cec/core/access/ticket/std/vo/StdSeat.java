package cn.mopon.cec.core.access.ticket.std.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 座位VO。
 */
@XStreamAlias("Seat")
public class StdSeat {
	@XStreamAsAttribute
	@XStreamAlias("SeatCode")
	private String seatCode;
	@XStreamAsAttribute
	@XStreamAlias("Price")
	private Double price;
	private String filmTicketCode;

	public String getSeatCode() {
		return seatCode;
	}

	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getFilmTicketCode() {
		return filmTicketCode;
	}

	public void setFilmTicketCode(String filmTicketCode) {
		this.filmTicketCode = filmTicketCode;
	}

}
