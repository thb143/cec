package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 选座票订单类型。
 */
public enum TicketOrderType implements IEnum {
	NORMAL("普通订单", "1"), SPECIAL("特价订单", "2");

	private String text;
	private String value;

	/**
	 * 构造方法
	 * 
	 * @param text
	 *            文本
	 * @param value
	 *            值
	 */
	private TicketOrderType(String text, String value) {
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