package cn.mopon.cec.core.access.member.hfh;

import java.util.List;

import cn.mopon.cec.core.access.member.hfh.vo.TicketDiscountVo;

/**
 * 会员卡折扣价响应对象。
 */
public class DiscountPriceReply extends HFHReply {
	private List<TicketDiscountVo> ticketDiscounts;

	/**
	 * 构造方法。
	 */
	public DiscountPriceReply() {
		xstream.alias("data", DiscountPriceReply.class);
		xstream.alias("data", HFHReply.class, DiscountPriceReply.class);

		xstream.alias("ticketDiscount", TicketDiscountVo.class);
	}

	public List<TicketDiscountVo> getTicketDiscounts() {
		return ticketDiscounts;
	}
}
