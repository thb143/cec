package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 通知类型。
 */
public enum NoticeType implements IEnum {
	SHOW("排期预警通知", "1"), PRICE("价格预警通知", "2"), ORDER("订单预警通知", "3"), FILM(
			"影片预警通知", "4"), STAT("统计通知", "5");

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
	private NoticeType(String text, String value) {
		this.text = text;
		this.value = value;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return text;
	}
}