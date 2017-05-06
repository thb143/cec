package cn.mopon.cec.core.access.ticket.hfh;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.util.StringUtils;

/**
 * 确认订单请求对象。
 */
public class SubmitOrderQuery extends HfhQuery {
	private static final String TICKETTYPE = "成人票";

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public SubmitOrderQuery(TicketOrder order) {
		setMethod("fixCashOrder");
		setNeedCinemaParams(true);
		setNeedRandKey(true);
		setNeedCheckValue(true);
		addParam("orderNo", order.getCinemaOrderCode());
		addParam("ticketCount", order.getTicketCount().toString());
		addParam("hallId", order.getHall().getCode());
		addParam("filmId", order.getFilmCode());
		genShowParams(order.getShowCode(), order.getShowTime());
		addParam("ticketTypeList", getTicketType(order.getTicketCount()));
		addParam("priceList", getPrice(order));
		addParam("feeList", getFee(order));
		addParam("phoneNo", order.getMobile());
	}

	/**
	 * 影票名称。
	 * 
	 * @param ticketCount
	 *            订票数量
	 * @return 影票名称
	 */
	private String getTicketType(int ticketCount) {
		List<String> ticketTypes = new ArrayList<>();
		for (int i = 0; i < ticketCount; i++) {
			ticketTypes.add(TICKETTYPE);
		}
		return StringUtils.join(ticketTypes, "|");
	}

	/**
	 * 影票价格。
	 * 
	 * @param order
	 *            订单
	 * @return 影票价格串
	 */
	private String getPrice(TicketOrder order) {
		List<String> submitPrices = new ArrayList<>();
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			submitPrices.add(orderItem.getSubmitPrice().toString());
		}
		return StringUtils.join(submitPrices, "|");
	}

	/**
	 * 影票手续费。
	 * 
	 * @param order
	 *            订单
	 * @return 影票手续费串
	 */
	private String getFee(TicketOrder order) {
		List<String> serviceFees = new ArrayList<>();
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			serviceFees.add(orderItem.getServiceFee().toString());
		}
		return StringUtils.join(serviceFees, "|");
	}
}