package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 释放座位响应对象。
 */
@XStreamAlias("ReleaseSeatReply")
public class ReleaseSeatReply extends StdReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", StdServiceReply.class);
		xstream.alias("ReleaseSeatReply", ReleaseSeatReply.class);
		xstream.alias("ReleaseSeatReply", StdReply.class,
				ReleaseSeatReply.class);
		xstream.aliasField("ReleaseSeatReply", StdServiceReply.class, "reply");
		xstream.alias("Order", TicketOrder.class);
	}

	/**
	 * 构造方法。
	 */
	public ReleaseSeatReply() {
		setId("ID_ReleaseSeatReply");
	}

	public TicketOrder getOrder() {
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
