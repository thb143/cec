package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询卖品订单请求对象。
 */
public class SnackOrderQuery extends ApiQuery {
	/** 平台订单号 */
	@NotBlank(message = "平台订单号不能为空。")
	private String orderCode;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}