package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单响应对象。
 */
public class SubmitOrderReply extends DxReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", SubmitOrderReply.class);
		xstream.alias("data", DxReply.class, SubmitOrderReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
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
