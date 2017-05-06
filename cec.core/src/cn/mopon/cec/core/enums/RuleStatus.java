package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 规则状态。
 */
public enum RuleStatus implements IEnum {
	UNAUDIT("未标记", "1", StatusColor.BLACK), AUDITPASS("审核标记通过", "2",
			StatusColor.GREEN), APPROVEPASS("审批标记通过", "3", StatusColor.GREEN), REFUSE(
			"标记退回", "4", StatusColor.RED);

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
	private RuleStatus(String text, String value, String color) {
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