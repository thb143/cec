package cn.mopon.cec.core.access.member.ng;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 价格查询请求对象。
 */
public class PriceQuery extends NgQuery {
	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡
	 * @param orderNo
	 *            订单编号
	 * @param secKey
	 *            渠道密码
	 */
	public PriceQuery(MemberCard memberCard, String orderNo, String secKey) {
		super(memberCard, secKey);
		setAction("queryPriceByOderNo.json");
		params.add(new BasicNameValuePair("orderNo", orderNo));
	}
}
