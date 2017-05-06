package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 锁座请求对象。
 */
public class LockSeatQuery extends ApiQuery {
	/** 渠道场次编码 */
	@NotBlank(message = "渠道场次编码不能为空。")
	private String channelShowCode;
	/** 渠道订单号 */
	@NotBlank(message = "渠道订单号不能为空。")
	@Length(max = 30, message = "渠道订单号不能超过30个字符。")
	private String channelOrderCode;
	/** 座位编码 */
	@NotBlank(message = "座位编码不能为空。")
	private String seatCodes;
	/** 票房价 */
	private Double submitPrice;

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String channelShowCode) {
		this.channelShowCode = channelShowCode;
	}

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
	}

	public String getSeatCodes() {
		return seatCodes;
	}

	public void setSeatCodes(String seatCodes) {
		this.seatCodes = seatCodes;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
	}
}