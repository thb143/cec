package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 策略状态。
 */
public enum PolicyStatus implements IEnum {
	SUBMIT("待提交", "1", StatusColor.RED), AUDIT("待审核", "2", StatusColor.ORANGE), APPROVE(
			"待审批", "3", StatusColor.ORANGE), APPROVED("已审批", "4",
			StatusColor.GREEN), BACKED("已退回", "5", StatusColor.RED);

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
	private PolicyStatus(String text, String value, String color) {
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