package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 工作台统计类型。
 */
public enum BenchType implements IEnum {

	CINEMA("影院", "1"), CHANNEL("渠道", "2"), DAY("天", "3"), FILM("影片", "4"), SPECIAL(
			"特殊定价", "5");

	private String text;
	private String value;

	/**
	 * 构造方法。
	 * 
	 * @param text
	 *            名称
	 * @param value
	 *            值
	 */
	BenchType(String text, String value) {
		this.text = text;
		this.value = value;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public String getValue() {
		return this.value;
	}
}
