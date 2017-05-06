package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡充值请求对象。
 */
public class MemberRechargeQuery extends DXQuery {
	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param money
	 *            充值金额（100元起冲）
	 * @param orderNo
	 *            SCEC订单号
	 * @param userName
	 *            用户名
	 * @param accessToken
	 *            auth2.0 token
	 */
	public MemberRechargeQuery(MemberCard memberCard, String money,
			String orderNo, String userName, String accessToken) {
		super.setAction("card/recharge");
		params.add(new BasicNameValuePair("cid", memberCard.getCinemaCode()));
		params.add(new BasicNameValuePair("money", money));
		params.add(new BasicNameValuePair("partner_deposit_id", orderNo));
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("pid", userName));
	}
}
