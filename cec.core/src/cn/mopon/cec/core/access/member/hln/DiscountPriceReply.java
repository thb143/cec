package cn.mopon.cec.core.access.member.hln;

import java.util.List;

import cn.mopon.cec.core.access.member.hln.vo.TicketDiscountVo;

/**
 * 会员卡折扣价响应对象。
 */
public class DiscountPriceReply extends HLNReply {
	private String cinemaOrderCode;
	private List<TicketDiscountVo> ticketDiscounts;

	/**
	 * 构造方法。
	 */
	public DiscountPriceReply() {
		xstream.alias("data", DiscountPriceReply.class);
		xstream.alias("data", HLNReply.class, DiscountPriceReply.class);
		xstream.alias("ticketDiscount", TicketDiscountVo.class);
	}

	public String getCinemaOrderCode() {
		return cinemaOrderCode;
	}

	public void setCinemaOrderCode(String cinemaOrderCode) {
		this.cinemaOrderCode = cinemaOrderCode;
	}

	public List<TicketDiscountVo> getTicketDiscounts() {
		return ticketDiscounts;
	}

	public void setTicketDiscounts(List<TicketDiscountVo> ticketDiscounts) {
		this.ticketDiscounts = ticketDiscounts;
	}

}
