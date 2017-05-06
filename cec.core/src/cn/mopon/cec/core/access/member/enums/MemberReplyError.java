package cn.mopon.cec.core.access.member.enums;

import coo.core.model.IEnum;

/**
 * 会员接口响应码。
 */
public enum MemberReplyError implements IEnum {
	MEMBER_LACK_BALANCE("100", "会员卡余额不足或扣款金额错误。"), ORDER_SUBMIT_FAILED("102",
			"影院地面系统确认订单失败"), MEMBER_NOT_EXIST("103", "会员卡或密码错误"), MEMBERCHIP_NOT_EXIST(
			"104", "会员卡芯片号不存在"), MEMBERCHIP_SEAT_PAY("105", "同一座位，不允许多人同时购买");
	private String text;
	private String value;

	/**
	 * 构造方法。
	 * 
	 * @param value
	 *            编码
	 * @param text
	 *            信息
	 */
	private MemberReplyError(String value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getValue() {
		return value;
	}
}
