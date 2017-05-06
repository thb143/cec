package cn.mopon.cec.core.access.ticket.dx.enums;

import coo.core.model.IEnum;

/**
 * 鼎新返回消息状态。
 */
public enum DXReplyStatus implements IEnum {
	SUEECSS("成功", "1"), FAIL("失败", "0");

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
	private DXReplyStatus(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}
}
