package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 取票号和验证码查询打票请求对象。
 */
public class PrintByVerifyCodeQuery extends ApiQuery {
	/** 订单编码 */
	@NotBlank(message = "订单编码不能为空。")
	private String orderCode;
	/** 取票号 */
	@NotBlank(message = "取票号不能为空。")
	private String printCode;
	/** 取票验证码 */
	@NotBlank(message = "取票验证码不能为空。")
	private String verifyCode;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String cinemaCode) {
		this.orderCode = cinemaCode;
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
}