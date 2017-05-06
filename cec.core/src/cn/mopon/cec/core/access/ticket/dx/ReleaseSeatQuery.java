package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 释放座位请求对象。
 */
public class ReleaseSeatQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public ReleaseSeatQuery(TicketOrder order) {
		setAction("seat/unlock");
		String showCode = order.getShowCode();
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("play_id", this.getPlayId(showCode)));
		params.add(new BasicNameValuePair("seat_id", getOrderSeat(order)));
		params.add(new BasicNameValuePair("lock_flag", order
				.getCinemaOrderCode()));
	}
}
