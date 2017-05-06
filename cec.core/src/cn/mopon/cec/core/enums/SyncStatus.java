package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 同步状态。
 */
public enum SyncStatus implements IEnum {
	SUCCESS("同步成功", "1", StatusColor.GREEN), FAILED("同步失败", "2",
			StatusColor.RED), ERROR("数据异常", "3", StatusColor.ORANGE);

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
	private SyncStatus(String text, String value, String color) {
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