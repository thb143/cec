package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 确认打票请求对象。
 */
public class ConfirmPrintQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            订单对象。
	 */
	public ConfirmPrintQuery(TicketOrder order) {
		setMethod("fixTicketPrint");
		setNeedCinemaParams(true);
		setNeedRandKey(true);
		setNeedCheckValue(true);
		addParam("bookingId", order.getCinemaOrderCode());
		addParam("setPrinted", "Y");
	}
}