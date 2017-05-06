package cn.mopon.cec.api;

/**
 * API异常。
 */
@SuppressWarnings("serial")
public class ApiException extends RuntimeException {
	private final IReplyCode replyCode;

	/**
	 * 构造方法。
	 * 
	 * @param replyCode
	 *            错误编码
	 */
	public ApiException(IReplyCode replyCode) {
		super(replyCode.getMsg());
		this.replyCode = replyCode;
	}

	/**
	 * 抛出API异常。
	 * 
	 * @param replyCode
	 *            异常编码
	 */
	public static void thrown(IReplyCode replyCode) {
		throw new ApiException(replyCode);
	}

	public IReplyCode getReplyCode() {
		return replyCode;
	}
}