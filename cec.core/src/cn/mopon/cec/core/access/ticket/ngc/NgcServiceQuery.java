package cn.mopon.cec.core.access.ticket.ngc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import coo.base.constants.Encoding;
import coo.base.exception.UncheckedException;
import coo.base.util.CryptoUtils;
import coo.base.util.StringUtils;

/**
 * NGC服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class NgcServiceQuery<T extends NgcQuery> {
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            服务请求对象
	 * @param channelCode
	 *            渠道编号
	 * @param key
	 *            通讯密钥
	 */
	public NgcServiceQuery(T query, String channelCode, String key) {
		this.query = query;
		this.query.params
				.add(new BasicNameValuePair("channelCode", channelCode));
		this.query.params.add(new BasicNameValuePair("sign", genSign(key)));
	}

	/**
	 * 生成签名。
	 * 
	 * @param key
	 *            通讯密钥
	 * @return 返回签名。
	 */
	private String genSign(String key) {
		Map<String, Object> paramsMap = new TreeMap<String, Object>();
		List<NameValuePair> values = query.getParams();
		for (NameValuePair valuePair : values) {
			paramsMap.put(valuePair.getName(), valuePair.getValue());
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
		return CryptoUtils.md5(key + paramSignStr + key);
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}
}