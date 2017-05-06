package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.TicketOrderStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询订单响应对象。
 */
@XStreamAlias("GetOrderStateResult")
public class OrderReply extends MtxReply {
	private static XStream xstream;
	/** 订单 */
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("GetOrderStateResult", OrderReply.class);
		xstream.alias("GetOrderStateResult", MtxReply.class, OrderReply.class);
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("orderItem", TicketOrderItem.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.registerLocalConverter(TicketOrder.class, "status",
				new IEnumConverter(TicketOrderStatus.class));
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