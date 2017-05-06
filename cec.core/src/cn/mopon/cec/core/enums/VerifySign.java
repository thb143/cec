package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 签名验证。
 */
public enum VerifySign implements IEnum {
	VERIFY("验证", "1"), NOTVERIFY("不验证", "0");

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
	private VerifySign(String text, String value) {
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