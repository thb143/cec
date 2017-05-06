package cn.mopon.cec.core.access.member.hfh;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketVoucher;

/**
 * 会员卡消费扣款响应对象。
 */
public class MemberPayReply extends HFHReply {
	private TicketOrder ticketOrder;

	/**
	 * 构造方法。
	 */
	public MemberPayReply() {
		xstream.alias("data", MemberPayReply.class);
		xstream.alias("data", MemberPayReply.class, HFHReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
		xstream.alias("orderItem", TicketOrderItem.class);
	}

	public TicketOrder getTicketOrder() {
		return ticketOrder;
	}

	public void setTicketOrder(TicketOrder ticketOrder) {
		this.ticketOrder = ticketOrder;
	}

}
