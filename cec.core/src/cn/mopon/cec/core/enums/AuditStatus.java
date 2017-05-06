package cn.mopon.cec.core.enums;

import coo.core.model.IEnum;

/**
 * 审核状态。
 */
public enum AuditStatus implements IEnum {
	AUDIT("待审核", "1"), APPROVE("待审批", "2"), APPROVED("已审批", "3"), REFUSED(
			"已退回", "4"), PROCESSED("已处理", "5");

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
	private AuditStatus(String text, String value) {
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