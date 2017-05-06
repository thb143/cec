package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 锁座请求对象。
 */
public class LockSeatQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public LockSeatQuery(TicketOrder order) {
		setAction("seat/lock");
		String showCode = order.getShowCode();
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("play_id", this.getPlayId(showCode)));
		params.add(new BasicNameValuePair("play_update_time", this
				.getUpdateTime(showCode)));
		params.add(new BasicNameValuePair("seat_id", getOrderSeat(order)));
	}
}
