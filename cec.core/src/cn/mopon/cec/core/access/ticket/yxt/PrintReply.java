package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.PrintStatus;
import com.thoughtworks.xstream.XStream;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询订单响应对象。
 */
public class PrintReply extends YxtReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", PrintReply.class);
		xstream.alias("reply", YxtReply.class, PrintReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("orderItem", TicketOrderItem.class);
		xstream.registerLocalConverter(TicketOrderItem.class, "printStatus",
				new IEnumConverter(PrintStatus.class));
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
