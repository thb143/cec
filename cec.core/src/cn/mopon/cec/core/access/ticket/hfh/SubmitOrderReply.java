package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 确认订单响应对象。
 */
@XStreamAlias("data")
public class SubmitOrderReply extends HfhReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", SubmitOrderReply.class);
		xstream.alias("data", HfhReply.class, SubmitOrderReply.class);
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	public TicketOrder getOrder() {
		return order;
	}
}
