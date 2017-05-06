package cn.mopon.cec.core.access.ticket.std.enums;

import coo.core.model.IEnum;

/**
 * 放映计划座位状态。
 */
public enum SessionSeatStatus implements IEnum {
	ALL("所有", "All"), AVAILABLE("可出售", "Available"), LOCKED("已锁定", "Locked"), SOLD(
			"已售出", "Sold"), BOOKED("已预定", "Booked"), UNAVAILABLE("不可用",
			"Unavailable");

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
	private SessionSeatStatus(String text, String value) {
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
