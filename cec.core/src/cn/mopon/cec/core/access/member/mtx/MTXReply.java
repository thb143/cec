package cn.mopon.cec.core.access.member.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 响应对象基类。
 */
public abstract class MTXReply {
	@XStreamOmitField
	protected XStream xstream = new GenericXStream();
	private String code;
	private String msg;

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
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
