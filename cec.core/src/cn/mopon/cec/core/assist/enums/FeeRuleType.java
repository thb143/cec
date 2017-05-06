package cn.mopon.cec.core.assist.enums;

import coo.core.model.IEnum;

/**
 * 费用规则类型。
 */
public enum FeeRuleType implements IEnum {
	NONE("无费用", "1"), FIXED("固定费用", "2"), PERCENT("百分比费用", "3");

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
	private FeeRuleType(String text, String value) {
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