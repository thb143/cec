package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.Hall;

/**
 * 查询影厅座位信息请求对象。
 */
public class SeatQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param hall
	 *            影厅
	 */
	public SeatQuery(Hall hall) {
		setMethod("qrySeat");
		setNeedCinemaParams(true);
		addParam("hallId", hall.getCode());
	}
}