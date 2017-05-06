package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 上报类型。
 */
public enum SubmitType implements IEnum {
	CINEMA_PRICE("影院结算价", "1"), CHANNEL_PRICE("渠道结算价", "2"), MIN_PRICE("最低价",
			"3");

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
	private SubmitType(String text, String value) {
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