package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 订单查询对象。
 */
public class OrderQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public OrderQuery(TicketOrder order) {
		setAction("order/ticket-flag");
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("partner_buy_ticket_id", order
				.getCode()));
	}
}
