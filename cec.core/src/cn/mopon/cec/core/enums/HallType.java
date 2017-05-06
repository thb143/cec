package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 影厅类型。
 */
public enum HallType implements IEnum {
	NORMAL2D("普通影厅", "1"), NORMAL3D("3D影厅", "2"), MAX2D("巨幕影厅", "3"), MAX3D(
			"3D巨幕影厅", "4");

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
	private HallType(String text, String value) {
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
