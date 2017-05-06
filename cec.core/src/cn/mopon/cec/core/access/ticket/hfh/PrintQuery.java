package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 查询订单请求对象。
 */
public class PrintQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public PrintQuery(TicketOrder order) {
		setMethod("qryOrder");
		setNeedCinemaParams(true);
		setNeedRandKey(true);
		setNeedCheckValue(true);
		addParam("orderNo", order.getCinemaOrderCode());
		addParam("ticketCount", order.getTicketCount().toString());
	}
}