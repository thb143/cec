package cn.mopon.cec.core.access.member.ng;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 充值请求对象。
 * */
public class CardRechargeQuery extends NgQuery {

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡
	 * @param amount
	 *            金额
	 * @param channelOrderNo
	 *            渠道订单号
	 * @param platformOrderNo
	 *            第三方平台订单号
	 * @param secKey
	 *            渠道密码
	 */
	public CardRechargeQuery(MemberCard memberCard, String amount,
			String channelOrderNo, String platformOrderNo, String secKey) {
		super(memberCard, secKey);
		setAction("cardRecharge.json");
		params.add(new BasicNameValuePair("amount", amount));
		params.add(new BasicNameValuePair("channelOrderNo", channelOrderNo));
		params.add(new BasicNameValuePair("platformOrderNo", platformOrderNo));

	}

}
