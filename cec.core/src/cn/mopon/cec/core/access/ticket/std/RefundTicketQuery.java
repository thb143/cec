package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.access.ticket.std.vo.StdOrder;
import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 退票请求对象。
 */
@XStreamAlias("RefundTicket")
public class RefundTicketQuery extends StdQuery {
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

		xstream.alias("RefundTicket", RefundTicketQuery.class);
		xstream.alias("RefundTicket", StdQuery.class, RefundTicketQuery.class);
		xstream.aliasField("RefundTicket", StdServiceQuery.class, "query");
	}

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 */
	public RefundTicketQuery(TicketOrder ticketOrder) {
		setId("ID_RefundTicket");
		cinemaCode = ticketOrder.getCinema().getCode();
		order.setPrintNo(ticketOrder.getVoucher().getPrintCode());
		order.setVerifyCode(ticketOrder.getVoucher().getVerifyCode());
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
