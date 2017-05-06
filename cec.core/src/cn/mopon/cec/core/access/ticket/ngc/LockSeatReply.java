package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.TicketOrder;
import com.thoughtworks.xstream.XStream;
import coo.core.xstream.GenericXStream;

/**
 * 锁座响应信息。
 */
public class LockSeatReply extends NgcReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", LockSeatReply.class);
		xstream.alias("data", NgcReply.class, LockSeatReply.class);
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
