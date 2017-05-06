package cn.mopon.cec.core.access.ticket.ng;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.PrintStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.base.util.BeanUtils;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询打票状态响应对象。
 * 
 */
@XStreamAlias("DQueryTakeTicketInfoReply")
public class PrintStatusReply extends NgReply {
	@XStreamOmitField
	private static XStream xstream;
	/** 订单对象 */
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("OnlineTicketingServiceReply", NgServiceReply.class);
		xstream.alias("DQueryTakeTicketInfoReply", PrintStatusReply.class);
		xstream.alias("DQueryTakeTicketInfoReply", NgReply.class,
				PrintStatusReply.class);
		xstream.aliasField("DQueryTakeTicketInfoReply", NgServiceReply.class,
				"reply");

		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
		xstream.registerLocalConverter(TicketVoucher.class, "status",
				new IEnumConverter(PrintStatus.class));
	}

	/**
	 * 构造方法。
	 */
	public PrintStatusReply() {
		setId("DQueryTakeTicketInfoReply");
	}

	/**
	 * 重构订单对象。
	 * 
	 * @param ticketOrder
	 *            订单对象
	 * @return 订单对象。
	 */
	public TicketOrder getOrder(TicketOrder ticketOrder) {
		BeanUtils.copyFields(ticketOrder, order, "orderItems");
		for (TicketOrderItem localItem : ticketOrder.getOrderItems()) {
			for (TicketOrderItem remoteItem : order.getOrderItems()) {
				if (localItem.getSeatCode().equals(remoteItem.getSeatCode())) {
					localItem.setBarCode(remoteItem.getBarCode());
					BeanUtils.copyFields(localItem, remoteItem);
					remoteItem.setPrintStatus(order.getVoucher().getStatus());
				}
			}
		}
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	@Override
	XStream getXstream() {
		return xstream;
	}

}
