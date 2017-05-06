package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.FilmVo;
import cn.mopon.cec.core.entity.Film;

/**
 * 查询影片响应对象。
 */
public class FilmsReply extends ApiReply {
	/** 影片列表 */
	private List<FilmVo> films = new ArrayList<FilmVo>();

	/**
	 * 构造方法。
	 * 
	 * @param films
	 *            影片列表
	 */
	public FilmsReply(List<Film> films) {
		for (Film film : films) {
			this.films.add(new FilmVo(film));
		}
	}

	public List<FilmVo> getFilms() {
		return films;
	}

	public void setFilms(List<FilmVo> films) {
		this.films = films;
	}
}