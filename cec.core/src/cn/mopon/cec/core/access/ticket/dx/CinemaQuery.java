package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

/**
 * 影院查询对象。
 */
public class CinemaQuery extends DxQuery {
	/**
	 * 构造方法
	 * 
	 * @param cinemaCode
	 *            影院编码
	 */
	public CinemaQuery(String cinemaCode) {
		setAction("cinema/halls");
		params.add(new BasicNameValuePair("cid", cinemaCode));
	}
}
