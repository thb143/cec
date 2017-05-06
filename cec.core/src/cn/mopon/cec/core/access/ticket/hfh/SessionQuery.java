package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.access.ticket.hfh.constants.HFHConstants;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 查询电影院放映计划信息请求对象。
 */
public class SessionQuery extends HfhQuery {
	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param beginDate
	 *            开始日期时间
	 * @param endDdate
	 *            结束日期时间
	 */
	public SessionQuery(Cinema cinema, String beginDate, String endDdate) {
		setMethod("qrySearchShow");
		addParam("searchKey", "C;BD;ED");
		String cinemaLinkId = cinema
				.getTicketSettingsParams(HFHConstants.CINEMA_LINKID);
		addParam("searchValue", cinemaLinkId + ";" + beginDate + ";" + endDdate);
	}
}