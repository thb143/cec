package cn.mopon.cec.core.access.member.ng;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 退票请求对象。
 */
public class RefundTicketQuery extends NgQuery {

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
	public RefundTicketQuery(TicketOrder order, MemberCard memberCard,
			String secKey) {
		super(memberCard, secKey);
		setAction("orderRefund.json");
		params.add(new BasicNameValuePair("orderNo", order.getCinemaOrderCode()));
	}

}
