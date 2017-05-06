package cn.mopon.cec.core.assist.enums;

import coo.core.model.IEnum;

/**
 * 周枚举类。
 */
public enum Week implements IEnum {
	MON("周一", "1"), TUE("周二", "2"), WED("周三", "3"), THU("周四", "4"), FRI("周五",
			"5"), SAT("周六", "6"), SUN("周日", "7");

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
	private Week(String text, String value) {
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
