package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 释放座位请求对象。
 */
public class ReleaseSeatQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public ReleaseSeatQuery(TicketOrder order) {
		setMethod("webUnlockSeat");
		setNeedCinemaParams(true);
		setNeedRandKey(true);
		setNeedCheckValue(true);
		addParam("orderNo", order.getCinemaOrderCode());
		addParam("ticketCount", order.getTicketCount().toString());
	}
}