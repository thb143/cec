package cn.mopon.cec.core.access.ticket.dx;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.util.StringUtils;

/**
 * 确认订单请求对象。
 */
public class SubmitOrderQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public SubmitOrderQuery(TicketOrder order) {
		setAction("seat/lock-buy");
		String showCode = order.getShowCode();
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("play_id", getPlayId(showCode)));
		params.add(new BasicNameValuePair("seat", genOrderSeatAndPrice(order)));
		params.add(new BasicNameValuePair("lock_flag", order
				.getCinemaOrderCode()));
		params.add(new BasicNameValuePair("play_update_time",
				getUpdateTime(showCode)));
		params.add(new BasicNameValuePair("partner_buy_ticket_id", order
				.getCode()));
	}

	/**
	 * 生成订单座位和价格。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回订单座位。
	 */
	private String genOrderSeatAndPrice(TicketOrder order) {
		List<String> strs = new ArrayList<>();
		for (TicketOrderItem item : order.getOrderItems()) {
			strs.add(item.getSeatCode() + "-" + item.getServiceFee() + "-"
					+ item.getSubmitPrice());
		}
		return StringUtils.join(strs, ",");
	}
}
