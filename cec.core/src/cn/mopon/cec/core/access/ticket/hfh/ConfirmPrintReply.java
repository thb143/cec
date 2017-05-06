package cn.mopon.cec.core.access.ticket.hfh;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 确认打票响应对象
 * 
 * 
 */
@XStreamAlias("data")
public class ConfirmPrintReply extends HfhReply {
	private static XStream xstream;
	/**
	 * 返回消息。
	 */
	private String message;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", ConfirmPrintReply.class);
		xstream.alias("data", HfhReply.class, ConfirmPrintReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
