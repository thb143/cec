package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.PrintStatus;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 请求出票响应对象。
 */
public class PrintReply extends DxReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.registerLocalConverter(TicketOrderItem.class, "printStatus",
				new IEnumConverter(PrintStatus.class));
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("orderItem", TicketOrderItem.class);
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
