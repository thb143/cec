package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.OrderVo;
import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 查询订单响应对象。
 */
public class OrderReply extends ApiReply {
	/** 订单 */
	private OrderVo order;

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	public OrderReply(TicketOrder ticketOrder) {
		order = new OrderVo(ticketOrder);
	}

	public OrderVo getOrder() {
		return order;
	}

	public void setOrder(OrderVo order) {
		this.order = order;
	}
}