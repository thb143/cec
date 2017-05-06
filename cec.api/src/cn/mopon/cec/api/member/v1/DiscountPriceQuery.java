package cn.mopon.cec.api.member.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 会员卡折扣请求对象。
 */
public class DiscountPriceQuery extends ApiQuery {
	/** 外部订单号 */
	@NotBlank(message = "订单号不能为空。")
	private String orderCode;
	/** 会员卡号 */
	@NotBlank(message = "会员卡号不能为空。")
	private String cardCode;
	/** 会员卡密码 */
	@NotBlank(message = "会员卡密码不能为空。")
	private String password;

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

}
