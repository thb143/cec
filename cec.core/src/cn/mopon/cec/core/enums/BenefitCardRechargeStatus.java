package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;
import coo.mvc.constants.StatusColor;

/**
 * 权益卡续费状态。
 */
public enum BenefitCardRechargeStatus implements IEnum {
	PAID("已支付", "1", StatusColor.ORANGE), SUCCESS("成功", "2", StatusColor.GREEN), FAIL(
			"失败", "3", StatusColor.RED);

	private String text;
	private String value;
	private String color;

	/**
	 * 构造方法。
	 * 
	 * @param text
	 *            文本
	 * @param value
	 *            值
	 * @param color
	 *            颜色
	 */
	private BenefitCardRechargeStatus(String text, String value, String color) {
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