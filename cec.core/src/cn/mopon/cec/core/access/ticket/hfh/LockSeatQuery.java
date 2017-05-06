package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.constants.Chars;

/**
 * 锁座请求对象。
 */
public class LockSeatQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public LockSeatQuery(TicketOrder order) {
		setMethod("webLockSeat");
		setNeedCinemaParams(true);
		setNeedRandKey(true);
		setNeedCheckValue(true);
		String cinemaOrderCode = order.getCode().substring(4);
		order.setCinemaOrderCode(cinemaOrderCode);
		addParam("orderNo", order.getCinemaOrderCode());
		addParam("ticketCount", order.getTicketCount().toString());
		addParam("hallId", order.getHall().getCode());
		addParam("sectionId", order.getOrderItems().get(0).getSeatGroupCode());
		addParam("filmId", order.getFilmCode());
		genShowParams(order.getShowCode(), order.getShowTime());
		addParam("seatId", getSeatCode(order));
		addParam("lockMinuteTime", "15");
	}

	/**
	 * 获取座位编码。
	 * 
	 * @param order
	 *            订单
	 * @return 座位编码
	 */
	private String getSeatCode(TicketOrder order) {
		StringBuilder sb = new StringBuilder();
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			if (sb.length() > 0) {
				sb.append(Chars.BAR)
						.append(orderItem.getSeatCode().split(Chars.UNDERLINE)[0])
						.append(Chars.COLON)
						.append(orderItem.getSeatCode().split(Chars.UNDERLINE)[1]);
			} else {
				sb.append(orderItem.getSeatCode().split(Chars.UNDERLINE)[0])
						.append(Chars.COLON)
						.append(orderItem.getSeatCode().split(Chars.UNDERLINE)[1]);
			}
		}
		return sb.toString();
	}
}
