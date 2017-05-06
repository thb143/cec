package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.access.ticket.ng.vo.MemberOrder;
import cn.mopon.cec.core.access.ticket.ng.vo.NgSeat;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 会员订单确认请求对象。
 * 
 */
@XStreamAlias("DMemberSubmitOrder")
public class MemberSubmitOrderQuery extends NgQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	@XStreamAlias("Order")
	private MemberOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", NgServiceQuery.class);
		xstream.alias("DMemberSubmitOrder", MemberSubmitOrderQuery.class);
		xstream.alias("DMemberSubmitOrder", NgQuery.class,
				MemberSubmitOrderQuery.class);
		xstream.alias("Order", MemberOrder.class);
		xstream.alias("Seat", NgSeat.class);
		xstream.aliasField("DMemberSubmitOrder", NgServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单对象
	 * @param memberPayOrderCode
	 *            会员支付订单号
	 */
	public MemberSubmitOrderQuery(TicketOrder ticketOrder,
			String memberPayOrderCode) {
		this.setId("ID_DMemberSubmitOrder");
		this.cinemaCode = ticketOrder.getCinema().getCode();
		order = new MemberOrder();
		order.setCount(ticketOrder.getTicketCount());
		order.setOrderCode(ticketOrder.getCinemaOrderCode());
		order.setMemberPayOrderCode(memberPayOrderCode);
		order.setSessionCode(ticketOrder.getShowCode());
		this.setSeats(ticketOrder);
	}

	/**
	 * 设置订单座位信息
	 * 
	 * @param ticketOrder
	 *            订单对象
	 */
	public void setSeats(TicketOrder ticketOrder) {
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			NgSeat ngSeat = new NgSeat();
			ngSeat.setSeatCode(orderItem.getSeatCode());
			ngSeat.setPrice(orderItem.getSubmitPrice());
			order.getSeats().add(ngSeat);
		}
	}

	public MemberOrder getOrder() {
		return order;
	}

	public void setOrder(MemberOrder order) {
		this.order = order;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
