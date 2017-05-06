package cn.mopon.cec.core.access.film.csp;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Film;
import coo.base.util.DateUtils;

/**
 * 影片信息。
 */
public class FilmVo {
	/** 编码 */
	private String code;
	/** 名称 */
	private String name;
	/** 产地 */
	private String locale;
	/** 首映日期 */
	private String publishDate;
	/** 时长 */
	private String duration;
	/** 英文名称 */
	private String englishName;
	/** 语言 */
	private String language;
	/** 影片类型 */
	private String filmTypes;
	/** 放映类型 */
	private String showTypes;
	/** 评分 */
	private String score;
	/** 导演 */
	private String director;
	/** 演员 */
	private String cast;
	/** 精彩看点 */
	private String point;
	/** 发行商 */
	private String publisher;
	/** 简介 */
	private String intro;
	/** 海报 */
	private String poster;
	/** 剧照 */
	private List<StillVo> stills = new ArrayList<>();
	/** 预告片 */
	private List<TrailerVo> trailers = new ArrayList<>();

	/**
	 * 获取影片。
	 * 
	 * @return 返回影片。
	 */
	public Film getFilm() {
		Film film = new Film();
		film.setCode(getCode());
		film.setName(getName());
		film.setDuration(Integer.valueOf(getDuration()));
		film.setPublishDate(DateUtils.parse(getPublishDate(), DateUtils.DAY));
		film.setPublisher(getPublisher());
		film.setDirector(getDirector());
		film.setCast(getCast());
		film.setIntro(getIntro());
		film.setShowTypes(getShowTypes());
		film.setCountry(getLocale());
		film.setLanguage(getLanguage());
		film.setType(getFilmTypes());
		film.setStills(getStillList());
		film.setTrailers(getTrailerList());
		film.setPoster(getPoster());
		film.setHighlight(getPoint());
		film.autoFillIn();
		return film;
	}

	/**
	 * 获取剧照信息。
	 * 
	 * @return 返回剧照。
	 */
	private String getStillList() {
		StringBuilder sb = new StringBuilder();
		for (StillVo still : getStills()) {
			if (sb.length() != 0) {
				sb.append(",");
			}
			sb.append(still.getImageUrl());
		}
		return sb.toString();
	}

	/**
	 * 获取预告片信息。
	 * 
	 * @return 返回预告片。
	 */
	private String getTrailerList() {
		StringBuilder sb = new StringBuilder();
		for (TrailerVo trailer : getTrailers()) {
			if (sb.length() != 0) {
				sb.append(",");
			}
			sb.append(trailer.getVideoUrl());
		}
		return sb.toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFilmTypes() {
		return filmTypes;
	}

	public void setFilmTypes(String filmTypes) {
		this.filmTypes = filmTypes;
	}

	public String getShowTypes() {
		return showTypes;
	}

	public void setShowTypes(String showTypes) {
		this.showTypes = showTypes;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public List<StillVo> getStills() {
		return stills;
	}

	public void setStills(List<StillVo> stills) {
		this.stills = stills;
	}

	public List<TrailerVo> getTrailers() {
		return trailers;
	}

	public void setTrailers(List<TrailerVo> trailers) {
		this.trailers = trailers;
	}
}
