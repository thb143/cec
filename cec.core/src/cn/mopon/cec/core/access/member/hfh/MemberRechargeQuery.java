package cn.mopon.cec.core.access.member.hfh;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡充值请求对象。
 */
public class MemberRechargeQuery extends HFHQuery {
	/**
	 * 构造方法。
	 * 
	 * @param token
	 *            令牌
	 * @param memberCard
	 *            会员卡信息
	 * @param money
	 *            充值金额
	 */
	public MemberRechargeQuery(String token, MemberCard memberCard, String money) {
		super.setMethod("modifyMemberCardBalance");
		nvps.add(new BasicNameValuePair("cardFacadeCd", memberCard
				.getCardCode()));
		nvps.add(new BasicNameValuePair("cardPass", memberCard.getPassword()));
		nvps.add(new BasicNameValuePair("token", token));
		nvps.add(new BasicNameValuePair("balance", money));
		nvps.add(new BasicNameValuePair("payment", ""));
		nvps.add(new BasicNameValuePair("aliOrderNo", ""));
		nvps.add(new BasicNameValuePair("randKey", getFixLenthString(8)));
		nvps.add(new BasicNameValuePair("checkValue", getCheckValue(nvps)));
	}
}
