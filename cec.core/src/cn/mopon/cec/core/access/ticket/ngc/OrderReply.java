package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import com.thoughtworks.xstream.XStream;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询订单响应对象。
 */
public class OrderReply extends NgcReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", OrderReply.class);
		xstream.alias("data", NgcReply.class, OrderReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.registerLocalConverter(TicketOrder.class, "status",
				new IEnumConverter(TicketOrderStatus.class));
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
