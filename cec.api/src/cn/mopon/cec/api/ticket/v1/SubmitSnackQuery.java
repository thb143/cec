package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 确认卖品请求对象。
 */
public class SubmitSnackQuery extends ApiQuery {
	/** 渠道订单号 */
	@NotBlank(message = "渠道订单号不能为空。")
	@Length(max = 30, message = "渠道订单号不能超过30个字符。")
	private String channelOrderCode;
	/** 卖品信息 */
	@NotBlank(message = "卖品信息不能为空。")
	private String snacks;
	/** 权益卡信息 */
	private String benefit;
	/** 手机号码 */
	@NotBlank(message = "手机号码不能为空。")
	private String mobile;

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}

	public String getSnacks() {
		return snacks;
	}

	public void setSnacks(String snacks) {
		this.snacks = snacks;
	}

	public String getBenefit() {
		return benefit;
	}

	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}