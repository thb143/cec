package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import coo.core.model.UuidEntity;

/**
 * 影片错误日志。
 */
@Entity
@Table(name = "CEC_FilmErrorLog")
public class FilmErrorLog extends UuidEntity {
	/** 关联同步日志 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "syncLogId")
	private FilmSyncLog syncLog;
	/** 影片编码 */
	private String code;
	/** 影片名称 */
	private String name;
	/** 错误信息 */
	private String msg;

	public FilmSyncLog getSyncLog() {
		return syncLog;
	}

	public void setSyncLog(FilmSyncLog syncLog) {
		this.syncLog = syncLog;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}