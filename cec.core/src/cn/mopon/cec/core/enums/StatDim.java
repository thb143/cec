package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 统计维度。
 */
public enum StatDim implements IEnum {
	ORDER("订单数", "ORDER"), TICKET("出票数", "TICKET"), AMOUNT("销售金额", "AMOUNT"), CHANNELSUBSIDYORDER(
			"渠道补贴订单数", "CHANNELSUBSIDYORDER"), CHANNELSUBSIDYTICKET("渠道补贴票数",
			"CHANNELSUBSIDYTICKET"), CHANNELSUBSIDYAMOUNT("渠道补贴金额",
			"CHANNELSUBSIDYAMOUNT");
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
	private StatDim(String text, String value) {
		this.text = text;
		this.value = value;
	}

	@Override
	public String toString() {
		return text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getValue() {
		return value;
	}
}
