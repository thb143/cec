package cn.mopon.cec.core.access.member.hfh;

import org.apache.http.message.BasicNameValuePair;

/**
 * 会员卡令牌请求对象。
 */
public class TokenQuery extends HFHQuery {

	/**
	 * 构造方法。
	 */
	public TokenQuery() {
		super.setMethod("getToken");
		nvps.add(new BasicNameValuePair("checkValue", getCheckValue(nvps)));
	}
}
