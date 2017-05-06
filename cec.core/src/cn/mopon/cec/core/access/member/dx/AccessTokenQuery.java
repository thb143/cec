package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

/**
 * Auth2.0 AccessToken请求对象。
 */
public class AccessTokenQuery extends DXQuery {

	/**
	 * 构造方法。
	 * 
	 * @param authCode
	 *            Auth2.0 authCode
	 * @param userName
	 *            授权账号
	 */
	public AccessTokenQuery(String authCode, String userName) {
		super.setAction("oauth2/access-token");
		super.setSign("client_secret");
		params.add(new BasicNameValuePair("client_id", userName));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", authCode));
	}

}
