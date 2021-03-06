package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单响应对象。
 */
@XStreamAlias("SubmitOrderReply")
public class SubmitOrderReply extends StdReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", StdServiceReply.class);
		xstream.alias("SubmitOrderReply", SubmitOrderReply.class);
		xstream.alias("SubmitOrderReply", StdReply.class,
				SubmitOrderReply.class);
		xstream.aliasField("SubmitOrderReply", StdServiceReply.class, "reply");
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	/**
	 * 构造方法。
	 */
	public SubmitOrderReply() {
		setId("ID_SubmitOrderReply");
	}

	public TicketOrder getOrder() {
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
