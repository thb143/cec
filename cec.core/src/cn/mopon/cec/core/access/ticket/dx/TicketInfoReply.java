package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.XStream;

import coo.core.xstream.GenericXStream;

/**
 * 订单信息响应对象。
 */
public class TicketInfoReply extends DxReply {
	private static XStream xstream;
	private TicketOrder ticketOrder;

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", TicketInfoReply.class);
		xstream.alias("data", DxReply.class, TicketInfoReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("ticketOrderItem", TicketOrderItem.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取带有票号选座票订单。
	 * 
	 * @param localOrder
	 *            选座票订单
	 * @return 返回带有影票编号的订单。
	 */
	public TicketOrder getTicketOrder(TicketOrder localOrder) {
		for (TicketOrderItem orderItem : localOrder.getOrderItems()) {
			for (TicketOrderItem remoteItem : ticketOrder.getOrderItems()) {
				if (orderItem.getSeatCol().equals(remoteItem.getSeatCol())
						&& orderItem.getSeatRow().equals(
								remoteItem.getSeatRow())) {
					orderItem.setTicketCode(remoteItem.getTicketCode());
				}
			}
		}
		return localOrder;
	}
}
