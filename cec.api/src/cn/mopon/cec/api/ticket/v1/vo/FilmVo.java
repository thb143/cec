package cn.mopon.cec.api.ticket.v1.vo;

import java.util.Date;

import cn.mopon.cec.core.entity.Film;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;

/**
 * 影片。
 */
@XStreamAlias("film")
public class FilmVo {
	/** 编码 */
	private String code = "";
	/** 名称 */
	private String name = "";
	/** 时长（分钟） */
	private Integer duration = 0;
	/** 影片公映日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date publishDate;
	/** 发行商 */
	private String publisher = "";
	/** 导演 */
	private String director = "";
	/** 演员 */
	private String cast = "";
	/** 简介 */
	private String intro = "";
	/** 放映类型 */
	private String showTypes = "";
	/** 发行国家 */
	private String country = "";
	/** 影片类型 */
	private String type = "";
	/** 影片语言 */
	private String language = "";
	/** 海报 */
	private String poster = "";
	/** 剧照 */
	private String stills = "";
	/** 精彩看点 */
	private String highlight = "";

	/**
	 * 构造方法。
	 * 
	 * @param film
	 *            影片
	 */
	public FilmVo(Film film) {
		BeanUtils.copyFields(film, this, "shows");
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getShowTypes() {
		return showTypes;
	}

	public void setShowTypes(String showType) {
		this.showTypes = showType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getStills() {
		return stills;
	}

	public void setStills(String stills) {
		this.stills = stills;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
}