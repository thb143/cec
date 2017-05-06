package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 座位类型。
 */
public enum SeatType implements IEnum {
	NORMAL("普通", "1"), LOVE("情侣", "2");

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
	private SeatType(String text, String value) {
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
