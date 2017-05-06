package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 释放座位响应对象。
 */
@XStreamAlias("data")
public class ReleaseSeatReply extends HfhReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", ReleaseSeatReply.class);
		xstream.alias("data", HfhReply.class, ReleaseSeatReply.class);
		xstream.alias("Order", TicketOrder.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public TicketOrder getOrder() {
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}
}
