package cn.mopon.cec.core.access.film.csp;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Film;

/**
 * 影片接口 响应报文体。
 */
public class FilmReply extends CspReply {
	/** 影片VO列表 */
	private List<FilmVo> films = new ArrayList<FilmVo>();

	/**
	 * 获取影片列表。
	 * 
	 * @return 返回影片列表。
	 */
	public List<Film> getFilmList() {
		List<Film> films = new ArrayList<Film>();
		for (FilmVo filmVo : getFilms()) {
			films.add(filmVo.getFilm());
		}
		return films;
	}

	public List<FilmVo> getFilms() {
		return films;
	}

	public void setFilms(List<FilmVo> filmList) {
		this.films = filmList;
	}
}