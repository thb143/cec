package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 退票请求对象。
 */
public class RefundTicketQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public RefundTicketQuery(TicketOrder order) {
		setAction("ticket/refund");
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("ticket_flag1", order.getVoucher()
				.getPrintCode()));
		params.add(new BasicNameValuePair("ticket_flag2", order.getVoucher()
				.getVerifyCode()));
		params.add(new BasicNameValuePair("partner_refund_ticket_id", order
				.getCinemaOrderCode()));
	}
}
