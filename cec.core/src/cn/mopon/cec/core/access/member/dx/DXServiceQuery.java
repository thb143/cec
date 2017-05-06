package cn.mopon.cec.core.access.member.dx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coo.base.util.CryptoUtils;
import coo.base.util.StringUtils;

/**
 * 鼎新服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class DXServiceQuery<T extends DXQuery> {
	private Logger log = LoggerFactory.getLogger(getClass());
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 * @param password
	 *            授权密钥
	 */
	public DXServiceQuery(T query, String password) {
		this.query = query;
		this.query.params.add(new BasicNameValuePair(query.getSign(),
				genSign(password)));
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
		log.info("参数签名内容[{}]", paramSignStr);
		return CryptoUtils.md5(CryptoUtils.md5(password + paramSignStr)
				+ password);
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}

}
