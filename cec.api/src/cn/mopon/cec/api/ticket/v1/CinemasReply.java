package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.CinemaVo;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 查询影院列表响应对象。
 */
public class CinemasReply extends ApiReply {
	/** 影院列表 */
	private List<CinemaVo> cinemas = new ArrayList<CinemaVo>();

	/**
	 * 构造方法。
	 * 
	 * @param cinemas
	 *            影院列表
	 * @param url
	 *            服务器图片路径
	 */
	public CinemasReply(List<Cinema> cinemas, String url) {
		for (Cinema cinema : cinemas) {
			this.cinemas.add(new CinemaVo(cinema, url));
		}
	}

	public List<CinemaVo> getCinemas() {
		return cinemas;
	}

	public void setCinemas(List<CinemaVo> cinemas) {
		this.cinemas = cinemas;
	}
}