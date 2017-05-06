package cn.mopon.cec.core.access.ticket.ngc;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * NGC响应对象基类。
 */
public abstract class NgcReply {
	@XStreamAlias("code")
	private String code;
	@XStreamAlias("msg")
	private String msg;

	/**
	 * 获取xstream对象。
	 * 
	 * @return xstream对象。
	 */
	abstract XStream getXstream();

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
