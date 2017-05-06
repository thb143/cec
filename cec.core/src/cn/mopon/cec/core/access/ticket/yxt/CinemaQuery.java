package cn.mopon.cec.core.access.ticket.yxt;

/**
 * 查询影院基础信息请求对象。
 */
public class CinemaQuery extends YxtQuery {

	/**
	 * 构造方法。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 */
	public CinemaQuery(String cinemaCode) {
		setAction("queryCinema.xml");
		addParam("cinemaCode", cinemaCode);
	}
}