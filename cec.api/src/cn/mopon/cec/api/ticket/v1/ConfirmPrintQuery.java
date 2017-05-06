package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 确认打票请求对象。
 */
public class ConfirmPrintQuery extends ApiQuery {
	/** 订单编码 */
	@NotBlank(message = "订单编码不能为空。")
	private String orderCode;
	/** 凭证编号 */
	@NotBlank(message = "凭证编号不能为空。")
	private String voucherCode;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String ticketCode) {
		this.orderCode = ticketCode;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}
}