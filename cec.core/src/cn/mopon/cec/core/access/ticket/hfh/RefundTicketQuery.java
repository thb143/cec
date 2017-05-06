package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 退票请求对象。
 */
public class RefundTicketQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public RefundTicketQuery(TicketOrder order) {
		setMethod("cancelOrder");
		setNeedCinemaParams(true);
		setNeedRandKey(true);
		setNeedCheckValue(true);
		addParam("bookingId", order.getCinemaOrderCode());
		addParam("ticketCount", order.getTicketCount().toString());
		addParam("hallId", order.getHall().getCode());
		addParam("filmId", order.getFilmCode());
		genShowParams(order.getShowCode(), order.getShowTime());
		addParam("sectionId", order.getOrderItems().get(0).getSeatGroupCode());
	}
}