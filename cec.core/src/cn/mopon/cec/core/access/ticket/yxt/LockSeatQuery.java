package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.constants.Chars;

/**
 * 锁座请求对象。
 */
public class LockSeatQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public LockSeatQuery(TicketOrder order) {
		setAction("lockSeats.xml");
		addParam("cinemaCode", order.getCinema().getCode());
		addParam("channelShowCode", order.getShowCode());
		addParam("seatCodes", getSeats(order));
		addParam("mobile", "13888888888");
		addParam("lockPrice", String.valueOf(order.getSingleCinemaPrice()));
		addParam("channelOrderCode", order.getChannelOrderCode());
	}

	/**
	 * 获取座位。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回座位。
	 */
	private String getSeats(TicketOrder ticketOrder) {
		StringBuilder sb = new StringBuilder();
		for (TicketOrderItem item : ticketOrder.getOrderItems()) {
			if (sb.length() > 0) {
				sb.append(Chars.COMMA);
			}
			sb.append(item.getSeatCode());
		}
		return sb.toString();
	}
}
