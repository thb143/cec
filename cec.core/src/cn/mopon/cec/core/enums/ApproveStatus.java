package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 审批状态。
 */
public enum ApproveStatus implements IEnum {
	APPROVE("待审批", "1"), APPROVED("已审批", "2"), REFUSED("已退回", "3"), PROCESSED(
			"已处理", "4");

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
	private ApproveStatus(String text, String value) {
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
