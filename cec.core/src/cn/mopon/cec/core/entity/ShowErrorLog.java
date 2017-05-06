package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.ShowErrorType;
import coo.core.model.UuidEntity;

/**
 * 排期异常日志。
 */
@Entity
@Table(name = "CEC_ShowErrorLog")
public class ShowErrorLog extends UuidEntity {
	/** 关联排期同步日志 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "syncLogId")
	private ShowSyncLog syncLog;
	/** 排期编码 */
	private String showCode;
	/** 影院编码 */
	private String cinemaCode;
	/** 影厅编码 */
	private String hallCode;
	/** 影片编码 */
	private String filmCode;
	/** 放映时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date showTime;
	/** 连场状态 */
	private Boolean through = false;
	/** 最低价 */
	private Double minPrice;
	/** 标准价 */
	private Double stdPrice;
	/** 排期异常类型 */
	@Type(type = "IEnum")
	private ShowErrorType type = ShowErrorType.SHOW;
	/** 异常信息 */
	private String msg;

	public ShowSyncLog getSyncLog() {
		return syncLog;
	}

	public void setSyncLog(ShowSyncLog syncLog) {
		this.syncLog = syncLog;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
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

	public ShowErrorType getType() {
		return type;
	}

	public void setType(ShowErrorType type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}