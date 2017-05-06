package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 会员卡折扣价请求对象。
 */
public class DiscountPriceQuery extends DXQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param userName
	 *            用户名
	 * @param accessToken
	 *            auth2.0 token
	 */
	public DiscountPriceQuery(TicketOrder order, String userName,
			String accessToken) {
		super.setAction("seat/price");
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("lock_flag", order
				.getCinemaOrderCode()));
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("pid", userName));
	}
}
