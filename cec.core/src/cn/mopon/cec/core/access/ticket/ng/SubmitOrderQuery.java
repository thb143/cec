package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.access.ticket.ng.vo.NgOrder;
import cn.mopon.cec.core.access.ticket.ng.vo.NgSeat;
import cn.mopon.cec.core.access.ticket.std.vo.StdSeat;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单请求对象。
 */
@XStreamAlias("SubmitSCTSOrder")
public class SubmitOrderQuery extends NgQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	@XStreamAlias("Order")
	private NgOrder order = new NgOrder();

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", NgServiceQuery.class);
		xstream.alias("SubmitSCTSOrder", SubmitOrderQuery.class);
		xstream.alias("SubmitSCTSOrder", NgQuery.class, SubmitOrderQuery.class);
		xstream.alias("Seat", StdSeat.class);
		xstream.aliasField("SubmitSCTSOrder", NgServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 */
	public SubmitOrderQuery(TicketOrder ticketOrder) {
		setId("ID_SubmitSCTSOrder");
		cinemaCode = ticketOrder.getCinema().getCode();
		order.setSessionCode(ticketOrder.getShowCode());
		order.setCinemaOrderCode(ticketOrder.getCinemaOrderCode());
		order.setCount(ticketOrder.getTicketCount());
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			NgSeat ngSeat = new NgSeat();
			ngSeat.setSeatCode(orderItem.getSeatCode());
			ngSeat.setPrice(orderItem.getSalePrice());
			ngSeat.setPayPrice(orderItem.getSubmitPrice());
			order.getSeats().add(ngSeat);
		}
		order.setChannelId(ticketOrder.getChannel().getCode());
		order.setChannelName(ticketOrder.getChannel().getName());
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public NgOrder getOrder() {
		return order;
	}

	public void setOrder(NgOrder order) {
		this.order = order;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}
