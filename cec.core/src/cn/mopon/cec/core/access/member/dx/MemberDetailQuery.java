package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡详细信息请求对象。
 */
public class MemberDetailQuery extends DXQuery {

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param userName
	 *            用户名
	 * @param accessToken
	 *            auth2.0 token
	 */
	public MemberDetailQuery(MemberCard memberCard, String userName,
			String accessToken) {
		super.setAction("card/detail");
		params.add(new BasicNameValuePair("cid", memberCard.getCinemaCode()));
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("pid", userName));
	}

}
