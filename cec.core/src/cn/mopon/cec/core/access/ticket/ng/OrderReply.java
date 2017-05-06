package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.TicketOrderStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.StringUtils;
import coo.core.xstream.GenericXStream;

/**
 * 查询订单响应对象。
 */
@XStreamAlias("QueryOrderReply")
public class OrderReply extends NgReply {
	private static XStream xstream;
	/** 订单 */
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("QueryOrderReply", OrderReply.class);
		xstream.alias("QueryOrderReply", NgReply.class, OrderReply.class);
		xstream.aliasField("QueryOrderReply", NgServiceReply.class, "reply");

		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	/**
	 * 构造方法。
	 */
	public OrderReply() {
		setId("ID_QueryOrderReply");
	}

	/**
	 * 返回订单。
	 * 
	 * @return 返回订单，如果取票号不为空，则为出票成功。
	 */
	public TicketOrder getOrder() {
		if (StringUtils.isNotEmpty(order.getVoucher().getPrintCode())) {
			order.setStatus(TicketOrderStatus.SUCCESS);
		}
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