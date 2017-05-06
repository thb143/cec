package cn.mopon.cec.api;

/**
 * API响应接口。
 * 
 */
public interface IReplyCode {
	/**
	 * 获取响应编码。
	 * 
	 * @return 返回响应编码。
	 */
	String getCode();

	/**
	 * 获取响应信息。
	 * 
	 * @return 返回响应信息。
	 */
	String getMsg();
}
