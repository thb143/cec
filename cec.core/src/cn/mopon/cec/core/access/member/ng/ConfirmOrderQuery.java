package cn.mopon.cec.core.access.member.ng;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;

/**
 * 确认订单请求对象。
 */
public class ConfirmOrderQuery extends NgQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param memberCard
	 *            会员卡
	 * @param secKey
	 *            渠道密码
	 */
	public ConfirmOrderQuery(TicketOrder order, MemberCard memberCard,
			String secKey) {
		super(memberCard, secKey);
		setAction("orderConfirm.json");
		params.add(new BasicNameValuePair("orderNo", order.getCinemaOrderCode()));
		params.add(new BasicNameValuePair("orders", getSeatInfo(order)));
	}

	/**
	 * 获取订单座位信息。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回座位信息。
	 */
	private String getSeatInfo(TicketOrder order) {
		StringBuilder sb = new StringBuilder();
		for (TicketOrderItem item : order.getOrderItems()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(order.getShowCode()).append(":");
			sb.append(item.getSeatCode()).append(":");
			sb.append(item.getSubmitPrice()).append(":");
			sb.append(item.getServiceFee());
		}
		return sb.toString();
	}
}
