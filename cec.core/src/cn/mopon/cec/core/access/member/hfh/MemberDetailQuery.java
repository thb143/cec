package cn.mopon.cec.core.access.member.hfh;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡详细信息请求对象。
 */
public class MemberDetailQuery extends HFHQuery {
	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 */
	public MemberDetailQuery(MemberCard memberCard) {
		super.setMethod("qryMemberCardInfo");
		nvps.add(new BasicNameValuePair("cinemaId", memberCard.getCinemaCode()));
		nvps.add(new BasicNameValuePair("cardFacadeCd", memberCard
				.getCardCode()));
		nvps.add(new BasicNameValuePair("cardPass", memberCard.getPassword()));
		nvps.add(new BasicNameValuePair("randKey", getFixLenthString(8)));
		nvps.add(new BasicNameValuePair("checkValue", getCheckValue(nvps)));
	}
}
