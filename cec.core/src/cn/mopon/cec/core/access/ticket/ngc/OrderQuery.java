package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 查询订单请求对象。
 */
public class OrderQuery extends NgcQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public OrderQuery(TicketOrder order) {
		setAction("queryOrder.xml");
		addParam("orderCode", order.getCinemaOrderCode());
	}
}
