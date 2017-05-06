package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

/**
 * Cookie请求对象。
 */
public class CookieQuery extends DXQuery {

	/**
	 * 构造方法。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @param userName
	 *            用户名
	 * @param cardCode
	 *            会员卡号
	 * @param cardPass
	 *            会员卡密码
	 */
	public CookieQuery(String cinemaCode, String userName, String cardCode,
			String cardPass) {
		super.setAction("oauth2/verify-login");
		params.add(new BasicNameValuePair("jsoncallback", "?"));
		params.add(new BasicNameValuePair("cinema_id", cinemaCode));
		params.add(new BasicNameValuePair("client_id", userName));
		params.add(new BasicNameValuePair("card", cardCode));
		params.add(new BasicNameValuePair("password", cardPass));
	}
}
