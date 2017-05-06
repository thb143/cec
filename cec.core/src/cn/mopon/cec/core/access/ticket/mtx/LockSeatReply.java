package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 锁座响应信息。
 */
@XStreamAlias("RealCheckSeatStateResult")
public class LockSeatReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("RealCheckSeatStateResult", LockSeatReply.class);
		xstream.alias("RealCheckSeatStateResult", MtxReply.class,
				LockSeatReply.class);
		xstream.alias("Order", TicketOrder.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public TicketOrder getOrder() {
		return order;
	}
}