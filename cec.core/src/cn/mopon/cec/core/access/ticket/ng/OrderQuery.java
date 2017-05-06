package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询订单请求对象。
 */
@XStreamAlias("QueryOrder")
public class OrderQuery extends NgQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	@XStreamAlias("OrderCode")
	private String cinemaOrderCode;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", NgServiceQuery.class);
		xstream.alias("QueryOrder", OrderQuery.class);
		xstream.alias("QueryOrder", NgQuery.class, OrderQuery.class);
		xstream.aliasField("QueryOrder", NgServiceQuery.class, "query");

	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 */
	public OrderQuery(TicketOrder ticketOrder) {
		setId("ID_QueryOrder");
		cinemaCode = ticketOrder.getCinema().getCode();
		cinemaOrderCode = ticketOrder.getCinemaOrderCode();
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCinemaOrderCode() {
		return cinemaOrderCode;
	}

	public void setCinemaOrderCode(String cinemaOrderCode) {
		this.cinemaOrderCode = cinemaOrderCode;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}
}