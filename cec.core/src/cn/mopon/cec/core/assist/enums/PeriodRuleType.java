package cn.mopon.cec.core.assist.enums;

import coo.core.model.IEnum;

/**
 * 时段规则类型。
 */
public enum PeriodRuleType implements IEnum {
	HOLIDAY("特殊日期", "10"), ROUNDWEEK("区间星期", "20"), ROUND("日期区间", "30"), WEEK(
			"特殊星期", "40"), DAY("常规日期", "50");

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
	private PeriodRuleType(String text, String value) {
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