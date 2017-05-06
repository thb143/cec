package cn.mopon.cec.core.access.sms.enums;

/**
 * 短信类型。
 */
public enum SmsType {
	SMS("短信", "1"), MMS("彩信", "2");

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
	private SmsType(String text, String value) {
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
