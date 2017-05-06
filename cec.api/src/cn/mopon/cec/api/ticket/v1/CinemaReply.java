package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.CinemaVo;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 查询影院响应对象。
 */
public class CinemaReply extends ApiReply {
	private CinemaVo cinema;

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param url
	 *            服务器图片路径
	 */
	public CinemaReply(Cinema cinema, String url) {
		this.cinema = new CinemaVo(cinema, url);
	}

	public CinemaVo getCinema() {
		return cinema;
	}

	public void setCinema(CinemaVo cinema) {
		this.cinema = cinema;
	}
}