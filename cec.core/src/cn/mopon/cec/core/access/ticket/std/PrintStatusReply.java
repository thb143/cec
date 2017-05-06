package cn.mopon.cec.core.access.ticket.std;

import java.util.ArrayList;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.PrintStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 
 * 查询订单打印状态响应对象
 * 
 */
@XStreamAlias("QueryPrintReply")
public class PrintStatusReply extends StdReply {
	private static XStream xstream;
	/** 订单对象 */
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", StdServiceReply.class);
		xstream.alias("QueryPrintReply", PrintStatusReply.class);
		xstream.alias("QueryPrintReply", StdReply.class, PrintStatusReply.class);
		xstream.aliasField("QueryPrintReply", StdServiceReply.class, "reply");

		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
		xstream.registerLocalConverter(TicketVoucher.class, "status",
				new IEnumConverter(PrintStatus.class));
		xstream.registerConverter(new DateConverter(DateUtils.SECOND));
	}

	/**
	 * 构造方法。
	 */
	public PrintStatusReply() {
		setId("ID_QueryPrintReply");
		setId("ID_QueryPrintReply");
	}

	/**
	 * 重构订单对象
	 * 
	 * @param ticketOrder
	 *            订单对象
	 * @return 订单对象
	 */
	public TicketOrder getOrder(TicketOrder ticketOrder) {
		BeanUtils.copyFields(ticketOrder, this.order, "orderItems");
		this.order.setOrderItems(new ArrayList<TicketOrderItem>());
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			orderItem.setPrintStatus(this.order.getVoucher().getStatus());
			this.order.getOrderItems().add(orderItem);
		}
		return this.order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
