package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 可售状态。
 */
public enum SellStatus implements IEnum {
	DISABLED("不可售", "0"), ENABLED("可售", "1");

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
	private SellStatus(String text, String value) {
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