package cn.mopon.cec.api.member.v1.vo;

import cn.mopon.cec.core.access.member.vo.SeatInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 座位折扣价信息。
 */
@XStreamAlias("seatInfo")
@JsonInclude(Include.NON_NULL)
public class SeatInfoVo {
	/** 影厅座位号 */
	private String seatNo;
	/** 折扣价 */
	private Double ticketPrice;

	/**
	 * 构造方法。
	 * 
	 * @param seatInfo
	 *            座位折扣信息
	 */
	public SeatInfoVo(SeatInfo seatInfo) {
		this.seatNo = seatInfo.getSeatNo();
		this.ticketPrice = seatInfo.getTicketPrice();
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
