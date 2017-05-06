package cn.mopon.cec.core.access.ticket.mtx.vo;

import java.util.Date;

import org.joda.time.DateTime;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.enums.ShowType;
import coo.base.util.DateUtils;

/**
 * 排期。
 */
public class MTXShow {
	/** 关联影院 */
	private Cinema cinema;
	/** 关联影厅 */
	private Hall hall;
	/** 关联影片 */
	private Film film;
	/** 编码 */
	private String code;
	/** 影片编码 */
	private String filmCode;
	/** 影片语言 */
	private String language;
	/** 放映类型 */
	private ShowType showType;
	/** 放映时间 */
	private Date showTime;
	/** 过期时间 */
	private Date expireTime;
	/** 连场状态 */
	private Boolean through = false;
	/** 最低价 */
	private Double minPrice = 0D;
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 放映开始时间 */
	private Date startTime;
	/** 放映结束时间 */
	private Date endTime;
	/** 排期状态 */
	private String status;

	/**
	 * 获取放映时长 。
	 * 
	 * @return 放映时长。
	 */
	public Integer getDuration() {
		if (endTime.getTime() < startTime.getTime()) {
			DateTime dateTime = new DateTime(endTime.getTime());
			endTime = dateTime.plusDays(1).toDate();
		}
		return (int) DateUtils.getDuration(startTime, endTime)
				.getStandardMinutes();
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Boolean getThrough() {
		return through;
	}

	public void setThrough(Boolean through) {
		this.through = through;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}