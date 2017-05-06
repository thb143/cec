package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 取票方式。
 */
public enum PrintMode implements IEnum {
	PRINT("地面取票号", "1"), VOUCHER("平台凭证号", "2"), MIXED("混合方式", "3");

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
	private PrintMode(String text, String value) {
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