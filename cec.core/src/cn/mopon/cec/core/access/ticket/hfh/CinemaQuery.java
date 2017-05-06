package cn.mopon.cec.core.access.ticket.hfh;

/**
 * 查询影院基础信息请求对象。
 */
public class CinemaQuery extends HfhQuery {
	/**
	 * 构造方法。
	 */
	public CinemaQuery() {
		setMethod("qryCinema");
	}
}