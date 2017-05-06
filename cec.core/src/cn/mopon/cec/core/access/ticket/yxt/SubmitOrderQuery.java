package cn.mopon.cec.core.access.ticket.yxt;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.util.StringUtils;

/**
 * 确认订单请求对象。
 */
public class SubmitOrderQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public SubmitOrderQuery(TicketOrder order) {
		setAction("submitOrder.xml");
		addParam("cinemaCode", order.getCinema().getCode());
		addParam("orderCode", order.getCinemaOrderCode());
		addParam("mobile", order.getMobile());
		addParam("orderSeats", getSeats(order));
	}

	/**
	 * 获取座位。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回座位。
	 */
	private String getSeats(TicketOrder ticketOrder) {
		List<String> strs = new ArrayList<>();
		for (TicketOrderItem item : ticketOrder.getOrderItems()) {
			strs.add(item.getSeatCode() + ":" + item.getSalePrice() + ":"
					+ item.getSubmitPrice());
		}
		return StringUtils.join(strs, ",");
	}
}