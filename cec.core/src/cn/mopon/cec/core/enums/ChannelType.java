package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 渠道类型。
 */
public enum ChannelType implements IEnum {
	OTHER("分销", "1"), OWN("自有", "2");

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
	private ChannelType(String text, String value) {
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
