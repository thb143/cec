package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询开卡订单请求对象。
 */
public class OpenBenefitCardOrderQuery extends ApiQuery {

	/** 渠道开卡订单号 */
	@NotBlank(message = "渠道开卡订单号不能为空。")
	private String channelOrderCode;

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}
}