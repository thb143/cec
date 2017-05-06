package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单响应对象。
 */
@XStreamAlias("reply")
public class SubmitOrderReply extends NgcReply {
	private static XStream xstream;
	/** 订单 */
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("reply", NgcReply.class, SubmitOrderReply.class);
		xstream.alias("orderItem", TicketOrderItem.class);
		xstream.alias("voucher", TicketVoucher.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取选座票订单。
	 * 
	 * @param order
	 *            订单
	 * @return 返回选座票订单。
	 */
	public TicketOrder getTicketOrder(TicketOrder order) {
		ticketOrder.setCode(order.getCode());
		ticketOrder.setOrderItems(order.getOrderItems());
		return ticketOrder;
	}

	public void setTicketOrder(TicketOrder ticketOrder) {
		this.ticketOrder = ticketOrder;
	}
}