package cn.mopon.cec.core.access.member.ng.vo;

import java.util.List;

/**
 * 卡订单明细。
 */
public class MemberOrder {
	/** 地面订单编码 */
	private String orderNo;
	/** 销售时间 */
	private String saleTime;
	/** 订单总额 */
	private String totalSales;
	/** 影院编码 */
	private String cinemaCode;
	/** 影院名称 */
	private String cinemaName;
	/** 影厅编码 */
	private String screenCode;
	/** 影厅名称 */
	private String screenName;
	/** 影片编码 */
	private String filmCode;
	/** 影片名称 */
	private String filmName;
	/** 排期编码 */
	private String sessionCode;
	/** 地面订单编码 */
	private String sessionDateTime;
	/** 影票数量 */
	private String ticketCount;
	/** 取票号 */
	private String printCode;
	/** 取票验证码 */
	private String verifyCode;
	/** 订单明细 */
	private List<MemberOrderItem> data;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public String getScreenCode() {
		return screenCode;
	}

	public void setScreenCode(String screenCode) {
		this.screenCode = screenCode;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public String getSessionDateTime() {
		return sessionDateTime;
	}

	public void setSessionDateTime(String sessionDateTime) {
		this.sessionDateTime = sessionDateTime;
	}

	public String getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(String ticketCount) {
		this.ticketCount = ticketCount;
	}

	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public List<MemberOrderItem> getData() {
		return data;
	}

	public void setData(List<MemberOrderItem> data) {
		this.data = data;
	}
}