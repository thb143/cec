package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 会员接入适配器类型。
 */
public enum MemberAccessAdapter implements IEnum {
	NG("火烈鸟1.5", "020"), DX("鼎新1.0", "040"), HFH("火凤凰特供0.4", "050"), MTX(
			"满天星5.0.1", "060");

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
	private MemberAccessAdapter(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return text;
	}
}
