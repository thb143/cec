package cn.mopon.cec.core.access.ticket.ng;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 退票响应对象。
 */
@XStreamAlias("RefundTicketReply")
public class RefundTicketReply extends NgReply {
	private static XStream xstream;
	private Boolean refunded;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("RefundTicketReply", RefundTicketReply.class);
		xstream.alias("RefundTicketReply", NgReply.class,
				RefundTicketReply.class);
		xstream.aliasField("RefundTicketReply", NgServiceReply.class, "reply");
	}

	/**
	 * 构造方法。
	 */
	public RefundTicketReply() {
		setId("ID_RefundTicketReply");
	}

	public Boolean getRefunded() {
		return refunded;
	}

	public void setRefunded(Boolean status) {
		this.refunded = status;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
