package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 有效月份。
 */
public enum ValidMonth implements IEnum {
	ONE("1个月", "1"), THREE("3个月", "3"), SIX("6个月", "6"), ONE_YEAR("12个月", "12"), TWO_YEAR(
			"24个月", "24"), THREE_YEAR("36个月", "36");

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
	private ValidMonth(String text, String value) {
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
