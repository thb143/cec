package cn.mopon.cec.api;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 请求对象基类。
 */
public abstract class ApiQuery {
	/** 渠道编号 */
	@NotBlank(message = "渠道编号不能为空。")
	protected String channelCode;
	/** 签名 */
	@NotBlank(message = "签名不能为空。")
	protected String sign;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String sellerId) {
		this.channelCode = sellerId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}