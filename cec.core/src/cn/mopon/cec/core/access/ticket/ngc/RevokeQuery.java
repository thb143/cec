package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 退票请求对象。
 */
public class RevokeQuery extends NgcQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public RevokeQuery(TicketOrder order) {
		setAction("revokeTicket.xml");
		addParam("orderCode", order.getCinemaOrderCode());
	}
}
