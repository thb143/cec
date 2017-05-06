package cn.mopon.cec.core.access.ticket.dx;

/**
 * 影院列表查询对象。
 */
public class CinemasQuery extends DxQuery {
	/**
	 * 构造方法。
	 */
	public CinemasQuery() {
		super.setAction("partner/cinemas");
	}
}
