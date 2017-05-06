package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 选座票接入适配器类型。
 */
public enum TicketAccessAdapter implements IEnum {
	TEST("测试", "000"), STD("国标2013", "010"), HLN("火烈鸟1.5", "020"), NGC(
			"火烈鸟5.0", "030"), DX("鼎新1.0", "040"), HFH("火凤凰特供0.6", "050"), MTX(
			"满天星5.0.1", "060"), DD("大地1.0", "070"), ZL("洲立1.71", "080"), JY(
			"金逸1.02", "090"), WY("微影V5", "100");

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
	private TicketAccessAdapter(String text, String value) {
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