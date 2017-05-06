package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.access.ticket.ng.vo.NgOrder;
import cn.mopon.cec.core.access.ticket.ng.vo.NgSeat;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 锁座请求对象。
 */
@XStreamAlias("LockSeat")
public class LockSeatQuery extends NgQuery {
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
		xstream.alias("LockSeat", LockSeatQuery.class);
		xstream.alias("LockSeat", NgQuery.class, LockSeatQuery.class);
		xstream.alias("Seat", NgSeat.class);
		xstream.aliasField("LockSeat", NgServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 */
	public LockSeatQuery(TicketOrder ticketOrder) {
		setId("ID_LockSeat");
		cinemaCode = ticketOrder.getCinema().getCode();
		order.setCount(ticketOrder.getTicketCount());
		order.setSessionCode(ticketOrder.getShowCode());
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			NgSeat ngSeat = new NgSeat();
			ngSeat.setSeatCode(orderItem.getSeatCode());
			order.getSeats().add(ngSeat);
		}
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
