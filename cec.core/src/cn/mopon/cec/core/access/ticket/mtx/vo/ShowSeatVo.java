package cn.mopon.cec.core.access.ticket.mtx.vo;

/**
 * 排期座位vo。
 */
public class ShowSeatVo {
	/** 座位编码 */
	private String seatNo;
	/** 座位状态 */
	private String seatState;

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getSeatState() {
		return seatState;
	}

	public void setSeatState(String seatState) {
		this.seatState = seatState;
	}

}
