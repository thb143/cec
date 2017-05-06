package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.TicketOrderStatus;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 订单响应对象。
 */
public class OrderReply extends DxReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", LockSeatReply.class);
		xstream.alias("data", DxReply.class, LockSeatReply.class);
		xstream.registerLocalConverter(TicketOrder.class, "status",
				new IEnumConverter(TicketOrderStatus.class));
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