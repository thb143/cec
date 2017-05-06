package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 生效状态。
 */
public enum ValidStatus implements IEnum {
	UNVALID("未生效", "0", StatusColor.BLACK), VALID("已生效", "1", StatusColor.GREEN), INVALID(
			"已失效", "2", StatusColor.GRAY);

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
	private ValidStatus(String text, String value, String color) {
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