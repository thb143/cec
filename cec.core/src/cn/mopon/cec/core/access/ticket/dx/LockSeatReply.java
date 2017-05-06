package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 锁座响应对象。
 */
public class LockSeatReply extends DxReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", LockSeatReply.class);
		xstream.alias("data", DxReply.class, LockSeatReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public TicketOrder getTicketOrder() {
		return ticketOrder;
	}

	public void setTicketOrder(TicketOrder ticketOrder) {
		this.ticketOrder = ticketOrder;
	}
}
