package cn.mopon.cec.core.access.ticket.yxt;

import java.util.Date;

import coo.base.util.DateUtils;

/**
 * 影片排期查询对象。
 */
public class SessionQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param startDate
	 *            排期日期
	 * @param cinemaCode
	 *            影院编码
	 */
	public SessionQuery(Date startDate, String cinemaCode) {
		setAction("queryShows.xml");
		if (startDate != null) {
			addParam("startDate", DateUtils.format(startDate));
		}
		addParam("cinemaCode", cinemaCode);
		addParam("status", "1");
	}
}
