package cn.mopon.cec.core.access.ticket.mtx;

import java.util.Random;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 确认订单响应对象。
 */
@XStreamAlias("SellTicketResult")
public class SubmitOrderReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("Order")
	private TicketOrder order;

	static {
		xstream = new GenericXStream();
		xstream.alias("SellTicketResult", SubmitOrderReply.class);
		xstream.alias("SellTicketResult", MtxReply.class,
				SubmitOrderReply.class);
		xstream.alias("Order", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取订单。
	 * 
	 * @param orders
	 *            订单
	 * @return 返回地面订单。
	 */
	public TicketOrder getOrder(TicketOrder orders) {
		for (TicketOrderItem orderItems : orders.getOrderItems()) {
			orderItems.setTicketCode(order.getOrderItems().get(0)
					.getTicketCode());
		}
		// 赋值给订单编码
		order.setCode(orders.getCode());
		order.setOrderItems(orders.getOrderItems());
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
	}

	/**
	 * 获取指定长度的随便数。
	 * 
	 * @param strLength
	 *            长度
	 * @return 返回随机数。
	 */
	public String getFixLenthString(int strLength) {
		Random rm = new Random();
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		String fixLenthString = String.valueOf(pross);
		return fixLenthString.substring(2, strLength + 1);
	}
}
