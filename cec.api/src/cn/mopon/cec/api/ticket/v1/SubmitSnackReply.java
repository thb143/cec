package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 确认卖品响应对象。
 */
@JsonInclude(Include.NON_NULL)
public class SubmitSnackReply extends ApiReply {
	/** 凭证编码 */
	private String voucherCode;
	/** 订单号 */
	private String orderCode;

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}