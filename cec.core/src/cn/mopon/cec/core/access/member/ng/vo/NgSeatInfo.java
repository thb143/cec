package cn.mopon.cec.core.access.member.ng.vo;

public class NgSeatInfo {
	/** 排期编码 */
	private String sessionCode;
	/** 座位编码 */
	private String seatCode;
	/** 座位价格 */
	private Double memberPrice;
	/** 服务费 */
	private Double fee;

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public String getSeatCode() {
		return seatCode;
	}

	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}

	public Double getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}
}
