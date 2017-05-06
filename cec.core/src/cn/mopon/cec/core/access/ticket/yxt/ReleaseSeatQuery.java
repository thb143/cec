package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 释放座位请求对象。
 */
public class ReleaseSeatQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public ReleaseSeatQuery(TicketOrder order) {
		setAction("releaseSeats.xml");
		addParam("cinemaCode", order.getCinema().getCode());
		addParam("orderCode", order.getCinemaOrderCode());
	}
}
