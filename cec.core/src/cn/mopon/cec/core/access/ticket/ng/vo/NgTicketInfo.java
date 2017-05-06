package cn.mopon.cec.core.access.ticket.ng.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 取票信息vo
 */
@XStreamAlias("TakeTicketInfo")
public class NgTicketInfo {
	/** 简易取票号 */
	@XStreamAsAttribute
	@XStreamAlias("SimpleTakeNo")
	private String simpleTakeNo;
	/** 取票号 */
	@XStreamAlias("PrintNo")
	private String printNo;
	/** 取票验证码 */
	@XStreamAlias("VerifyCode")
	private String verifyCode;
	/** 影院订单号 */
	@XStreamAlias("OrderCode")
	private String cinemaOrderCode;

	public String getSimpleTakeNo() {
		return simpleTakeNo;
	}

	public void setSimpleTakeNo(String simpleTakeNo) {
		this.simpleTakeNo = simpleTakeNo;
	}

	public String getPrintNo() {
		return printNo;
	}

	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getCinemaOrderCode() {
		return cinemaOrderCode;
	}

	public void setCinemaOrderCode(String cinemaOrderCode) {
		this.cinemaOrderCode = cinemaOrderCode;
	}

	@Override
	public String toString() {
		return "NgTicketInfo [simpleTakeNo=" + simpleTakeNo + ", printNo="
				+ printNo + ", verifyCode=" + verifyCode + ", cinemaOrderCode="
				+ cinemaOrderCode + "]";
	}

}
