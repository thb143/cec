package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 确认订单请求对象。
 */
public class SubmitOrderQuery extends ApiQuery {
	/** 平台订单号 */
	@NotBlank(message = "平台订单号不能为空。")
	private String orderCode;
	/** 座位信息 */
	@NotBlank(message = "座位信息不能为空。")
	private String orderSeats;
	/** 卖品信息 */
	private String snacks;
	/** 权益卡信息 */
	private String benefit;
	/** 手机号码 */
	@NotBlank(message = "手机号码不能为空。")
	private String mobile;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderSeats() {
		return orderSeats;
	}

	public void setOrderSeats(String seatInfo) {
		this.orderSeats = seatInfo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
}