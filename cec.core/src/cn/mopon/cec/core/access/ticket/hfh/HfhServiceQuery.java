package cn.mopon.cec.core.access.ticket.hfh;

/**
 * 火凤凰(幸福蓝海)服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class HfhServiceQuery<T extends HfhQuery> {
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 */
	public HfhServiceQuery(T query) {
		this.query = query;
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}
}