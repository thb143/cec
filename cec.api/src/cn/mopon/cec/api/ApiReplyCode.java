package cn.mopon.cec.api;

/**
 * 
 * API响应。
 * 
 */
public enum ApiReplyCode implements IReplyCode {
	SUCCESS("001", "操作成功。"), CHANNEL_NOT_EXIST("900", "渠道未找到。"), CHANNEL_CLOSED(
			"901", "渠道已关闭。"), CHANNEL_UNSALABLE("902", "渠道已停售。"), ACCESS_DENIDE(
			"996", "访问被禁止。"), PARAMS_VALIDATE_FAILED("997", "参数验证失败。"), SIGN_VERIFY_FAILED(
			"998", "签名验证失败。"), FAILED("999", "发生未知异常。");

	private String code;
	private String msg;

	/**
	 * 构造方法。
	 * 
	 * @param code
	 *            编码
	 * @param msg
	 *            信息
	 */
	private ApiReplyCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}