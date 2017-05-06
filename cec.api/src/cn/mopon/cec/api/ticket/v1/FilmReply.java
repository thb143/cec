package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.FilmVo;
import cn.mopon.cec.core.entity.Film;

/**
 * 查询影片响应对象。
 */
public class FilmReply extends ApiReply {
	private FilmVo film;

	/**
	 * 构造方法。
	 * 
	 * @param film
	 *            影片
	 */
	public FilmReply(Film film) {
		this.film = new FilmVo(film);
	}

	public FilmVo getFilm() {
		return film;
	}

	public void setFilm(FilmVo film) {
		this.film = film;
	}
}