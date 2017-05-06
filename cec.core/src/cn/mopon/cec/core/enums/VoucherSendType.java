package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 凭证下发方式。
 */
public enum VoucherSendType implements IEnum {
	SMS("短信", "1"), MMS("彩信", "2"), SMMS("短信和彩信", "3");

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
	private VoucherSendType(String text, String value) {
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