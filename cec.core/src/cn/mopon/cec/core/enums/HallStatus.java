package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 影厅状态。
 */
public enum HallStatus implements IEnum {
	DISABLED("停用", "0", StatusColor.GRAY), ENABLED("启用", "1", StatusColor.GREEN), DELETE(
			"删除", "2", StatusColor.ORANGE);

	private String text;
	private String value;
	private String color;

	/**
	 * 构造方法
	 * 
	 * @param text
	 *            文本
	 * @param value
	 *            值
	 * @param color
	 *            颜色
	 */
	private HallStatus(String text, String value, String color) {
		this.text = text;
		this.value = value;
		this.color = color;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return text;
	}
}