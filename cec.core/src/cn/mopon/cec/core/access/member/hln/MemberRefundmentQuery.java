package cn.mopon.cec.core.access.member.hln;

import cn.mopon.cec.core.access.member.hln.vo.PayOrderItemVo;
import cn.mopon.cec.core.access.member.hln.vo.PayOrderVo;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员退款查询请求对象
 * 
 */
@XStreamAlias("rollback")
public class MemberRefundmentQuery extends HLNQuery {
	/** 会员支付订单对象 */
	private PayOrderVo payOrder;

	/**
	 * 构造方法
	 * 
	 * @param ticketOrder
	 *            订单对象
	 * @param orderNo
	 *            会员订单号
	 * @param userName
	 *            用户名
	 */
	public MemberRefundmentQuery(TicketOrder ticketOrder, String orderNo,
			String userName) {
		payOrder = new PayOrderVo();
		payOrder.setOrderNo(orderNo);
		payOrder.setOperator(userName);
		payOrder.setPayType(3);
		setItems(ticketOrder);
	}

	/**
	 * 设置会员支付订单项
	 * 
	 * @param ticketOrder
	 *            订单对象
	 */
	private void setItems(TicketOrder ticketOrder) {
		for (TicketOrderItem orderItem : ticketOrder.getOrderItems()) {
			PayOrderItemVo item = new PayOrderItemVo();
			item.setItemId(orderItem.getSeatCode());
			payOrder.getItems().add(item);
		}
	}

	public PayOrderVo getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(PayOrderVo payOrder) {
		this.payOrder = payOrder;
	}

}
