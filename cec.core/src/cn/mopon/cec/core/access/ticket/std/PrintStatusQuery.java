package cn.mopon.cec.core.access.ticket.std;

import cn.mopon.cec.core.access.ticket.std.vo.StdOrder;
import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 
 * 查询订单打印状态请求对象。
 */
@XStreamAlias("QueryPrint")
public class PrintStatusQuery extends StdQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 影院代码 */
	@XStreamAsAttribute
	@XStreamAlias("CinemaCode")
	private String cinemaCode;
	/** 订单查询请求对象 */
	@XStreamAlias("Order")
	private StdOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceQuery", StdServiceQuery.class);
		xstream.alias("QueryPrint", PrintStatusQuery.class);
		xstream.alias("QueryPrint", StdQuery.class, PrintStatusQuery.class);
		xstream.aliasField("QueryPrint", StdServiceQuery.class, "query");
	}

	/**
	 * 构造方法
	 * 
	 * @param ticketOrder
	 *            订单对象
	 */
	public PrintStatusQuery(TicketOrder ticketOrder) {
		setId("ID_QueryPrint");
		order = new StdOrder();
		this.cinemaCode = ticketOrder.getCinema().getCode();
		this.order.setPrintNo(ticketOrder.getVoucher().getPrintCode());
		this.order.setVerifyCode(ticketOrder.getVoucher().getVerifyCode());
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
