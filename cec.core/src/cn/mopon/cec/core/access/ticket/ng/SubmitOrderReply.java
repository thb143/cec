package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单响应对象。
 */
@XStreamAlias("SubmitSCTSOrderReply")
public class SubmitOrderReply extends NgReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("SubmitSCTSOrderReply", SubmitOrderReply.class);
		xstream.alias("SubmitSCTSOrderReply", NgReply.class,
				SubmitOrderReply.class);
		xstream.aliasField("SubmitSCTSOrderReply", NgServiceReply.class,
				"reply");
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	/**
	 * 构造方法。
	 */
	public SubmitOrderReply() {
		setId("ID_SubmitSCTSOrderReply");
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
