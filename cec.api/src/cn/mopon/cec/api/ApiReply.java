package cn.mopon.cec.api;

import com.thoughtworks.xstream.annotations.XStreamAliasType;

/**
 * 响应对象基类。
 */
@XStreamAliasType("reply")
public class ApiReply {
	/** 编码 */
	private String code;
	/** 信息 */
	private String msg;

	/**
	 * 构造方法。
	 */
	public ApiReply() {
		this(ApiReplyCode.SUCCESS);
	}

	/**
	 * 构造方法。
	 * 
	 * @param replyCode
	 *            响应编码
	 */
	public ApiReply(IReplyCode replyCode) {
		code = replyCode.getCode();
		msg = replyCode.getMsg();
	}

	/**
	 * 设置响应。
	 * 
	 * @param replyCode
	 *            响应编码
	 */
	public void setReplyCode(IReplyCode replyCode) {
		code = replyCode.getCode();
		msg = replyCode.getMsg();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}