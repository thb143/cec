package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 接入商类型。
 */
public enum Provider implements IEnum {
	NG("火烈鸟", "1"), DX("鼎新", "2"), HFH("火凤凰", "3"), MTX("满天星", "4"), DD("大地",
			"5"), ZL("洲立", "6"), JY("金逸", "7"), WY("微影", "8");

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
	private Provider(String text, String value) {
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
