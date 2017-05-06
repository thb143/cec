package cn.mopon.cec.core.access.film.csp;

/**
 * csp 返回结果基类。
 */
public class CspReply {
	/** 异常码 */
	private String code;
	/** 返回信息 */
	private String msg;

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