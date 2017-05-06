package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import cn.mopon.cec.core.enums.SyncStatus;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 影片同步日志。
 */
@Entity
@Table(name = "CEC_FilmSyncLog")
@Indexed(index = "FilmSyncLog")
public class FilmSyncLog extends UuidEntity {
	/** 同步时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date syncTime = new Date();
	/** 耗时（毫秒） */
	private Long duration = 0L;
	/** 总数量 */
	private Integer processCount = 0;
	/** 新增数量 */
	private Integer createCount = 0;
	/** 更新数量 */
	private Integer updateCount = 0;
	/** 异常数量 */
	private Integer errorCount = 0;
	/** 同步状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private SyncStatus status = SyncStatus.SUCCESS;
	/** 异常信息 */
	private String msg;
	/** 异常影讯记录列表 */
	@OneToMany(mappedBy = "syncLog", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FilmErrorLog> errorLogs = new ArrayList<FilmErrorLog>();

	/**
	 * 增加新增计数。
	 */
	public void addCreateCount() {
		createCount++;
	}

	/**
	 * 增加更新计数。
	 */
	public void addUpdateCount() {
		updateCount++;
	}

	/**
	 * 增加错误计数。
	 */
	public void addErrorCount() {
		errorCount++;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getProcessCount() {
		return processCount;
	}

	public void setProcessCount(Integer processCount) {
		this.processCount = processCount;
	}

	public Integer getCreateCount() {
		return createCount;
	}

	public void setCreateCount(Integer createCount) {
		this.createCount = createCount;
	}

	public Integer getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public SyncStatus getStatus() {
		return status;
	}

	public void setStatus(SyncStatus status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<FilmErrorLog> getErrorLogs() {
		return errorLogs;
	}

	public void setErrorLogs(List<FilmErrorLog> errorLogs) {
		this.errorLogs = errorLogs;
	}
}