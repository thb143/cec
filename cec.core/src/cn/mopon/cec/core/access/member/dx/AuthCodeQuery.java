package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

/**
 * Auth2.0 Code请求对象。
 */
public class AuthCodeQuery extends DXQuery {
	/**
	 * 构造方法
	 * 
	 * @param cardCode
	 *            会员卡号
	 * @param cardPass
	 *            会员卡密码
	 * @param userName
	 *            授权账号
	 * @param cinemaCode
	 *            影院编码
	 */
	public AuthCodeQuery(String cardCode, String cardPass, String userName,
			String cinemaCode) {
		super.setAction("oauth2/authorize");
		params.add(new BasicNameValuePair("card", cardCode));
		params.add(new BasicNameValuePair("password", cardPass));
		params.add(new BasicNameValuePair("client_id", userName));
		params.add(new BasicNameValuePair("response_type", "code"));
		params.add(new BasicNameValuePair("redirect_uri", "callback"));
		params.add(new BasicNameValuePair("state", "xyz"));
		params.add(new BasicNameValuePair("scope", ""));
		params.add(new BasicNameValuePair("cinema_id", cinemaCode));
	}
}
