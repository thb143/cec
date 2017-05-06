package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询场次请求对象。
 */
public class ShowQuery extends ApiQuery {
	/** 渠道场次编码 */
	@NotBlank(message = "渠道场次编码不能为空。")
	private String channelShowCode;

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String channelShowCode) {
		this.channelShowCode = channelShowCode;
	}
}