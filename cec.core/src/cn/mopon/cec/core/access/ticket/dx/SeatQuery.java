package cn.mopon.cec.core.access.ticket.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.Hall;

/**
 * 影厅座位查询对象。
 */
public class SeatQuery extends DxQuery {
	/**
	 * 构造方法。
	 * 
	 * @param hall
	 *            影厅
	 */
	public SeatQuery(Hall hall) {
		setAction("/cinema/hall-seats");
		params.add(new BasicNameValuePair("cid", hall.getCinema().getCode()));
		params.add(new BasicNameValuePair("hall_id", hall.getCode()));
	}
}
