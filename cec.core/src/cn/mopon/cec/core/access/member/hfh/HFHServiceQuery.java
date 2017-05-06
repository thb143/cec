package cn.mopon.cec.core.access.member.hfh;

import org.apache.http.message.BasicNameValuePair;

/**
 * 火凤凰(幸福蓝海)服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class HFHServiceQuery<T extends HFHQuery> {
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public HFHServiceQuery(T query, String username, String password) {
		this.query = query;
		this.query.nvps.add(new BasicNameValuePair("userId", username));
		this.query.nvps.add(new BasicNameValuePair("userPass", password));
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}
}