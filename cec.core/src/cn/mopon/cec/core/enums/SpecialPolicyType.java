package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 特殊定价策略类型。
 */
public enum SpecialPolicyType implements IEnum {
	SELL("促销", "1"), FILM("特殊影片", "2");

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
	private SpecialPolicyType(String text, String value) {
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
