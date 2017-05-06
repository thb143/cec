package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 卖品状态。
 */
public enum SnackStatus implements IEnum {
	OFF("下架", "0", StatusColor.GRAY), ON("上架", "1", StatusColor.GREEN), UNVALID(
			"未设价", "2", StatusColor.RED);

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
	private SnackStatus(String text, String value, String color) {
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