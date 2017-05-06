package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.access.ticket.std.vo.StdOrder;
import cn.mopon.cec.core.access.ticket.std.vo.StdSeat;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 释放座位请求对象。
 */
@XStreamAlias("ReleaseSeat")
public class ReleaseSeatQuery extends StdQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	@XStreamAlias("Order")
	private StdOrder order = new StdOrder();

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", StdServiceQuery.class);

		xstream.alias("ReleaseSeat", LockSeatQuery.class);
		xstream.alias("ReleaseSeat", StdQuery.class, LockSeatQuery.class);
		xstream.aliasField("ReleaseSeat", StdServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 */
	public ReleaseSeatQuery(TicketOrder ticketOrder) {
		setId("ID_ReleaseSeat");
		cinemaCode = ticketOrder.getCinema().getCode();
		order.setSessionCode(ticketOrder.getShowCode());
		order.setCinemaOrderCode(ticketOrder.getCinemaOrderCode());
		order.setCount(ticketOrder.getTicketCount());
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			StdSeat stdSeat = new StdSeat();
			stdSeat.setSeatCode(orderItem.getSeatCode());
			order.getSeats().add(stdSeat);
		}
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public StdOrder getOrder() {
		return order;
	}

	public void setOrder(StdOrder order) {
		this.order = order;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
