package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 排期异常类型。
 */
public enum ShowErrorType implements IEnum {
	SHOW("排期异常", "1"), PRICE("价格异常", "2"), FILM("影片异常", "3");

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
	private ShowErrorType(String text, String value) {
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