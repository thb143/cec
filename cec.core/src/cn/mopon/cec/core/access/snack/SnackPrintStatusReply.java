package cn.mopon.cec.core.access.snack;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 查看卖品打印状态响应对象。
 */
public class SnackPrintStatusReply {
	private SnackPrintStatusReplyHead head;
	private SnackPrintStatusReplyBody body;
	protected XStream xstream = new GenericXStream();

	/**
	 * 构造方法。
	 */
	public SnackPrintStatusReply() {
		xstream.alias("mopon", SnackPrintStatusReply.class);
	}

	public SnackPrintStatusReplyHead getHead() {
		return head;
	}

	public SnackPrintStatusReplyBody getBody() {
		return body;
	}

	public void setBody(SnackPrintStatusReplyBody body) {
		this.body = body;
	}

	public void setHead(SnackPrintStatusReplyHead head) {
		this.head = head;
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}
}