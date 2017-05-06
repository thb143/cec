package cn.mopon.cec.core.access.member.hln;

import cn.mopon.cec.core.access.member.hln.vo.PayOrderItemVo;
import cn.mopon.cec.core.access.member.hln.vo.PayOrderVo;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员卡折扣价请求对象。
 * 
 */
@XStreamAlias("queryPayDetails")
public class DiscountPriceQuery extends HLNQuery {
	private PayOrderVo payOrder;

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡
	 * @param ticketOrder
	 *            订单
	 */
	public DiscountPriceQuery(MemberCard memberCard, TicketOrder ticketOrder) {
		payOrder = new PayOrderVo();
		payOrder.setAccount(memberCard.getCardCode());
		payOrder.setPayType(2);
		for (TicketOrderItem item : ticketOrder.getOrderItems()) {
			PayOrderItemVo items = new PayOrderItemVo();
			items.setSeatId(item.getSeatCode());
			items.setNumber(1);
			items.setOriginalPrice(ticketOrder.getStdPrice());
			items.setItemType("2");
			items.setPlanId(ticketOrder.getShowCode());
			payOrder.getItems().add(items);
		}
	}

	public PayOrderVo getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(PayOrderVo payOrder) {
		this.payOrder = payOrder;
	}
}
