package cn.mopon.cec.core.access.ticket.hfh;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 响应对象基类。
 */
public abstract class HfhReply {
	@XStreamAsAttribute
	@XStreamAlias("version")
	private String version = "1.0";
	@XStreamAsAttribute
	@XStreamAlias("message")
	private String message;
	@XStreamAsAttribute
	@XStreamAlias("result")
	private String result;

	/**
	 * 获取xstream对象。
	 * 
	 * @return xstream对象。
	 */
	abstract XStream getXstream();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
