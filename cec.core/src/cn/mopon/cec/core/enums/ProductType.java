package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 产品类型。
 */
public enum ProductType implements IEnum {
	TICKET("选座票", "10"), SNACK("卖品", "20"), BENEFITCARD("权益卡", "30");

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
	private ProductType(String text, String value) {
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
