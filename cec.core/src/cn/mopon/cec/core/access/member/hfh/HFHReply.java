package cn.mopon.cec.core.access.member.hfh;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 响应对象基类。
 */
public abstract class HFHReply {
	@XStreamOmitField
	protected XStream xstream = new GenericXStream();
	@XStreamAsAttribute
	@XStreamAlias("version")
	private String version = "1.0";
	@XStreamAsAttribute
	@XStreamAlias("message")
	private String message;
	@XStreamAsAttribute
	@XStreamAlias("result")
	private String result;
	@XStreamAlias("Messages")
	private String messages;

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

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

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}