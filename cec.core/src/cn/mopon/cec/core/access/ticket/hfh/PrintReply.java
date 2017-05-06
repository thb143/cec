package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.PrintStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询订单响应对象。
 */
@XStreamAlias("data")
public class PrintReply extends HfhReply {
	private static XStream xstream;
	/** 订单 */
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", PrintReply.class);
		xstream.alias("data", HfhReply.class, PrintReply.class);
		xstream.alias("Order", TicketOrder.class);
		xstream.registerLocalConverter(TicketOrderItem.class, "printStatus",
				new IEnumConverter(PrintStatus.class));
		xstream.alias("orderItem", TicketOrderItem.class);
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