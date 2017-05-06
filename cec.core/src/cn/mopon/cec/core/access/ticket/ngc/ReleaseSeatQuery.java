package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 释放座位请求对象。
 */
public class ReleaseSeatQuery extends NgcQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public ReleaseSeatQuery(TicketOrder order) {
		setAction("releaseSeats.xml");
		addParam("orderCode", order.getCinemaOrderCode());
	}
}
