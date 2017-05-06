package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 放映类型。
 */
public enum ShowType implements IEnum {
	NORMAL2D("2D", "1"), NORMAL3D("3D", "2"), MAX2D("MAX2D", "3"), MAX3D(
			"MAX3D", "4"), DMAX("DMAX", "6");

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
	private ShowType(String text, String value) {
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
