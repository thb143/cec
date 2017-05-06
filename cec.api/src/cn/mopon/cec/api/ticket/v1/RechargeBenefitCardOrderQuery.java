package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询续费订单请求对象。
 */
public class RechargeBenefitCardOrderQuery extends ApiQuery {
	/** 渠道续费订单号 */
	@NotBlank(message = "渠道续费订单号不能为空。")
	private String channelOrderCode;

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}
}