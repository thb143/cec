package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 查询出票信息请求对象。
 */
public class PrintQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public PrintQuery(TicketOrder order) {
		setAction("ticket/info");
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("ticket_flag1", order.getVoucher()
				.getPrintCode()));
		params.add(new BasicNameValuePair("ticket_flag2", order.getVoucher()
				.getVerifyCode()));
	}
}
