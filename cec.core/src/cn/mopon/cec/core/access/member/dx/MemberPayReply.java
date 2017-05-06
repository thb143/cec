package cn.mopon.cec.core.access.member.dx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;

/**
 * 会员卡扣款响应对象。
 */
public class MemberPayReply extends DXReply {
	private TicketOrder ticketOrder;

	/**
	 * 构造方法。
	 */
	public MemberPayReply() {
		xstream.alias("data", MemberPayReply.class);
		xstream.alias("data", DXReply.class, MemberPayReply.class);
		xstream.alias("ticketOrder", TicketOrder.class);
		xstream.alias("voucher", TicketVoucher.class);
	}

	public TicketOrder getTicketOrder() {
		return ticketOrder;
	}

	public void setTicketOrder(TicketOrder ticketOrder) {
		this.ticketOrder = ticketOrder;
	}

}