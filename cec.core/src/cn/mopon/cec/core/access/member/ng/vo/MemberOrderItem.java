package cn.mopon.cec.core.access.member.ng.vo;

/***
 * 订单明细。
 */
public class MemberOrderItem {
	/** 票号 */
	private String ticketCode;
	/** 影票状态（0:已付款 1:未出票 2:已出票 3:已退票） */
	private Integer ticketStatus;
	/** 座位编码 */
	private String seatCode;
	/** 座位分组编码 */
	private String hallGroupCode;
	/** 座位行号 */
	private String seatRowCode;
	/** 座位列号 */
	private String seatColCode;
	/** 价格 */
	private String price;
	/** 服务费 */
	private String fee;

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public Integer getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(Integer ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getSeatCode() {
		return seatCode;
	}

	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}

	public String getHallGroupCode() {
		return hallGroupCode;
	}

	public void setHallGroupCode(String hallGroupCode) {
		this.hallGroupCode = hallGroupCode;
	}

	public String getSeatRowCode() {
		return seatRowCode;
	}

	public void setSeatRowCode(String seatRowCode) {
		this.seatRowCode = seatRowCode;
	}

	public String getSeatColCode() {
		return seatColCode;
	}

	public void setSeatColCode(String seatColCode) {
		this.seatColCode = seatColCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
}