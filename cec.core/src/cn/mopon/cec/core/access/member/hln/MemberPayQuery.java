package cn.mopon.cec.core.access.member.hln;

import cn.mopon.cec.core.access.member.hln.vo.PayOrderItemVo;
import cn.mopon.cec.core.access.member.hln.vo.PayOrderVo;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.NumberUtils;

/**
 * 会员卡消费扣款请求对象。
 */
@XStreamAlias("pay")
public class MemberPayQuery extends HLNQuery {
	private PayOrderVo payOrder;

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param ticketOrder
	 *            选座票订单
	 * @param orderNo
	 *            会员订单查询时返回的订单号
	 * @param userName
	 *            用户名
	 */
	public MemberPayQuery(MemberCard memberCard, TicketOrder ticketOrder,
			String orderNo, String userName) {
		payOrder = new PayOrderVo();
		payOrder.setAccount(memberCard.getCardCode());
		payOrder.setPassword(memberCard.getPassword());
		payOrder.setOrderNo(orderNo);
		payOrder.setOperator(userName);
		payOrder.setPayType(2);
		for (TicketOrderItem order : ticketOrder.getOrderItems()) {
			PayOrderItemVo item = new PayOrderItemVo();
			item.setItemType("2");
			item.setItemId(order.getSeatCode());
			item.setOriginalPrice(ticketOrder.getStdPrice());
			item.setDiscountPrice(order.getSubmitPrice());
			item.setNumber(1);
			item.setSeatId(order.getSeatCode());
			item.setPlanId(ticketOrder.getShowCode());
			// 销售价 - 票房价 = 会员手续费
			item.setServiceFee(NumberUtils.sub(order.getSalePrice(),
					order.getSubmitPrice()));
			payOrder.getItems().add(item);
		}
	}

}
