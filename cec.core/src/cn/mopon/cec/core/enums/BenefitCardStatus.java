package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 权益卡状态。
 */
public enum BenefitCardStatus implements IEnum {

	NORMAL("正常", "1", StatusColor.GREEN), DISABLE("冻结", "2", StatusColor.RED), EXPIRE(
			"过期", "3", StatusColor.GRAY);

	private String text;
	private String value;
	private String color;

	/**
	 * 构造方法。
	 * 
	 * @param text
	 *            名称
	 * @param value
	 *            值
	 */
	BenefitCardStatus(String text, String value, String color) {
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
