package cn.mopon.cec.core.access.member.ng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.MemberSettings;
import coo.base.constants.Encoding;
import coo.base.exception.UncheckedException;
import coo.base.util.CryptoUtils;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;

/**
 * NG服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class NgServiceQuery<T extends NgQuery> {
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 * @param settings
	 *            会员设置
	 */
	public NgServiceQuery(T query, MemberSettings settings) {
		this.query = query;
		this.query.params.add(new BasicNameValuePair("channelCode", settings
				.getUsername()));
		this.query.params.add(new BasicNameValuePair("requestDate", DateUtils
				.format(new Date(), DateUtils.SECOND)));
		this.query.params
				.add(new BasicNameValuePair("sign", genSign(CryptoUtils.md5(
						settings.getPassword()).toUpperCase())));
	}

	/**
	 * 生成签名。
	 * 
	 * @param password
	 *            授权密钥。
	 * @return 返回签名。
	 */
	private String genSign(String password) {
		Map<String, Object> paramsMap = new TreeMap<String, Object>();
		for (NameValuePair nvp : query.getParams()) {
			paramsMap.put(nvp.getName(), nvp.getValue());
		}
		List<String> paramPairs = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			paramPairs.add(entry.getKey() + "=" + entry.getValue());
		}
		String paramSignStr = StringUtils.join(paramPairs, "&");
		try {
			paramSignStr = URLEncoder.encode(paramSignStr, Encoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new UncheckedException(e);
		}
		return CryptoUtils.md5(password + paramSignStr + password);
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}
}