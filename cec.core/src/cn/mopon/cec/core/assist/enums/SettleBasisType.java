package cn.mopon.cec.core.assist.enums;

import coo.core.model.IEnum;

/**
 * 结算规则基准类型。
 */
public enum SettleBasisType implements IEnum {
	STD_PRICE("标准价", "1"), MIN_PRICE("最低价", "2"), CINEMA_SETTLE("影院结算价", "3");

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
	private SettleBasisType(String text, String value) {
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
