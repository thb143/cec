package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 锁座响应信息。
 */
@XStreamAlias("LockSeatReply")
public class LockSeatReply extends NgReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("LockSeatReply", LockSeatReply.class);
		xstream.alias("LockSeatReply", NgReply.class, LockSeatReply.class);
		xstream.aliasField("LockSeatReply", NgServiceReply.class, "reply");
		xstream.alias("Order", TicketOrder.class);
	}

	/**
	 * 构造方法。
	 */
	public LockSeatReply() {
		setId("ID_LockSeatReply");
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