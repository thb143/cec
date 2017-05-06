package cn.mopon.cec.core.assist.enums;

import coo.core.model.IEnum;

/**
 * 结算规则类型 。
 */
public enum SettleRuleType implements IEnum {
	FIXED("固定价", "1"), FIXED_PLUS("固定加价", "2"), FIXED_MINUS("固定减价", "3"), REBATE(
			"折扣价", "4"), REBATE_PLUS("折扣加价", "5"), REBATE_MINUS("折扣减价", "6"), ROUND(
			"区间定价", "7");

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
	private SettleRuleType(String text, String value) {
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