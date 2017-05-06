package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 打票状态。
 */
public enum PrintStatus implements IEnum {
	NO("未打票", "0"), YES("已打票", "1");

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
	private PrintStatus(String text, String value) {
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
