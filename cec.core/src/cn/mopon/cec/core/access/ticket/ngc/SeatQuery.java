package cn.mopon.cec.core.access.ticket.ngc;

import cn.mopon.cec.core.entity.Hall;

/**
 * 影厅座位查询对象。
 */
public class SeatQuery extends NgcQuery {

	/**
	 * 构造方法。
	 * 
	 * @param hall
	 *            影厅
	 */
	public SeatQuery(Hall hall) {
		setAction("querySeats.xml");
		super.addParam("cinemaCode", hall.getCinema().getCode());
		super.addParam("hallCode", hall.getCode());
	}
}
