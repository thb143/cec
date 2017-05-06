package cn.mopon.cec.core.access.ticket.ng.vo;

/**
 * 
 * 简易取票号vo
 * 
 */
public class NgSimpleTakeNo {
	/** 影院订单号 */
	private String cinemaOrderCode;
	/** 简易订单号 */
	private String simpleTakeNo;

	public String getCinemaOrderCode() {
		return cinemaOrderCode;
	}

	public void setCinemaOrderCode(String cinemaOrderCode) {
		this.cinemaOrderCode = cinemaOrderCode;
	}

	public String getSimpleTakeNo() {
		return simpleTakeNo;
	}

	public void setSimpleTakeNo(String simpleTakeNo) {
		this.simpleTakeNo = simpleTakeNo;
	}

}
