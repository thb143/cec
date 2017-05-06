package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 退票请求对象。
 */
public class RevokeQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public RevokeQuery(TicketOrder order) {
		setAction("revokeTicket.xml");
		addParam("cinemaCode", order.getCinema().getCode());
		addParam("orderCode", order.getCinemaOrderCode());
	}
}
