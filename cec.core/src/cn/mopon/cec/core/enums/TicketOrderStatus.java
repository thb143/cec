package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 选座票订单状态。
 */
public enum TicketOrderStatus implements IEnum {
	UNPAID("未支付", "1"), CANCELED("已取消", "2"), PAID("已支付", "3"), SUCCESS("出票成功",
			"4"), FAILED("出票失败", "5"), REVOKED("已退票", "6");

	private String text;
	private String value;

	/**
	 * 构造方法。
	 * 
	 * @param text
	 *            文本
	 * @param value
	 *            值
	 */
	private TicketOrderStatus(String text, String value) {
		this.text = text;
		this.value = value;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return text;
	}
}
