package cn.mopon.cec.core.access.ticket.ngc;

/**
 * 影厅请求对象。
 */
public class HallQuery extends NgcQuery {
	/**
	 * 构造方法。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 */
	public HallQuery(String cinemaCode) {
		setAction("queryHalls.xml");
		addParam("cinemaCode", cinemaCode);
	}
}
