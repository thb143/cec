package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 接入模式。
 */
public enum AccessModel implements IEnum {
	CENTER("中心接入", "1"), SINGLE("单家接入", "2");

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
	private AccessModel(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return text;
	}
}
