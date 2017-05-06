package cn.mopon.cec.core.access.member.dx.vo;

/**
 * 选座票订单VO。
 */
public class TicketOrderVo {
	/** 取票号（序列号） */
	private String printCode;
	/** 取票号（验证码） */
	private String verifyCode;

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

}
