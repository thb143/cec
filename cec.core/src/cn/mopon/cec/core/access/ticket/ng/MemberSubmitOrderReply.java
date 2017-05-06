package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 
 * 会员确认订单响应对象
 * 
 */
@XStreamAlias("DMemberSubmitOrderReply")
public class MemberSubmitOrderReply extends NgReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("DMemberSubmitOrderReply", MemberSubmitOrderReply.class);
		xstream.alias("DMemberSubmitOrderReply", NgReply.class,
				MemberSubmitOrderReply.class);
		xstream.aliasField("DMemberSubmitOrderReply", NgServiceReply.class,
				"reply");
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	/**
	 * 构造方法
	 */
	public MemberSubmitOrderReply() {
		this.setId("ID_DMemberSubmitOrderReply");
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
