package cn.mopon.cec.api.member.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 会员卡支付请求对象。
 */
public class CardPayQuery extends ApiQuery {
	/** 订单号 */
	@NotBlank(message = "订单号不能为空。")
	private String orderCode;
	/** 会员卡号 */
	@NotBlank(message = "会员卡号不能为空。")
	private String cardCode;
	/** 会员卡密码 */
	@NotBlank(message = "会员卡密码不能为空。")
	private String password;
	/** 手机号 */
	@NotBlank(message = "手机号不能为空。")
	private String mobile;
	/** 座位信息 */
	@NotBlank(message = "座位信息不能为空。")
	private String seatInfos;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSeatInfos() {
		return seatInfos;
	}

	public void setSeatInfos(String seatInfos) {
		this.seatInfos = seatInfos;
	}

}
