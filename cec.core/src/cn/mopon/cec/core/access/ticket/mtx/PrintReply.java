package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.PrintStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.BeanUtils;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询打票状态响应对象。
 */
@XStreamAlias("AppPrintTicketResult")
public class PrintReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("AppPrintTicketResult", PrintReply.class);
		xstream.alias("AppPrintTicketResult", MtxReply.class, PrintReply.class);
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("orderItem", TicketOrderItem.class);
		xstream.registerLocalConverter(TicketOrderItem.class, "printStatus",
				new IEnumConverter(PrintStatus.class));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取订单。
	 * 
	 * @param localOrder
	 *            本地订单
	 * @return 返回订单信息。
	 */
	public TicketOrder getOrder(TicketOrder localOrder) {
		BeanUtils.copyFields(localOrder, order, "orderItems");
		for (TicketOrderItem remoteItem : order.getOrderItems()) {
			for (TicketOrderItem localItem : localOrder.getOrderItems()) {
				if (remoteItem.getSeatRow().equals(localItem.getSeatRow())
						&& remoteItem.getSeatCol().equals(
								localItem.getSeatCol())) {
					localItem.setBarCode(remoteItem.getBarCode());
					BeanUtils.copyFields(localItem, remoteItem,
							"barCode,printStatus");
				}
			}
		}
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

}