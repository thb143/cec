package cn.mopon.cec.core.access.member.ng;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 订单列表请求对象。
 */
public class OrdersQuery extends NgQuery {
	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            截止日期
	 * @param secKey
	 *            渠道密码
	 */
	public OrdersQuery(MemberCard memberCard, String startDate, String endDate,
			String secKey) {
		super(memberCard, secKey);
		setAction("queryOrders.json");
		params.add(new BasicNameValuePair("startDate", startDate));
		params.add(new BasicNameValuePair("endDate", endDate));
	}

}
