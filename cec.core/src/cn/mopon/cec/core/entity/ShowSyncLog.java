package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.ShowErrorType;
import cn.mopon.cec.core.enums.SyncStatus;
import coo.base.util.BeanUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 排期同步日志。
 */
@Entity
@Table(name = "CEC_ShowSyncLog")
@Indexed(index = "ShowSyncLog")
public class ShowSyncLog extends UuidEntity {
	/** 关联影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	@IndexedEmbedded(includePaths = { "id", "code" })
	private Cinema cinema;
	/** 同步时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date syncTime = new Date();
	/** 耗时（毫秒） */
	private Long duration = 0L;
	/** 处理数量 */
	private Integer processCount = 0;
	/** 新增数量 */
	private Integer createCount = 0;
	/** 更新数量 */
	private Integer updateCount = 0;
	/** 删除数量 */
	private Integer deleteCount = 0;
	/** 异常数量 */
	private Integer errorCount = 0;
	/** 同步状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private SyncStatus status = SyncStatus.SUCCESS;
	/** 异常信息 */
	private String msg;
	/** 异常排期记录列表 */
	@OneToMany(mappedBy = "syncLog", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ShowErrorLog> errorLogs = new ArrayList<>();

	/**
	 * 获取指定类型的异常排期记录列表。
	 * 
	 * @param showErrorType
	 *            异常排期记录类型
	 * @return 返回指定类型的异常排期记录列表。
	 */
	public List<ShowErrorLog> getErrorLogs(ShowErrorType showErrorType) {
		List<ShowErrorLog> showErrorLogs = new ArrayList<>();
		for (ShowErrorLog showErrorLog : getErrorLogs()) {
			if (showErrorLog.getType() == showErrorType) {
				showErrorLogs.add(showErrorLog);
			}
		}
		return showErrorLogs;
	}

	/**
	 * 增加错误记录。
	 * 
	 * @param show
	 *            同步的排期
	 * @param type
	 *            排期异常类型
	 * @param errorMsg
	 *            错误信息
	 */
	public void addErrorLog(Show show, ShowErrorType type, String errorMsg) {
		ShowErrorLog showErrorLog = new ShowErrorLog();
		showErrorLog.setSyncLog(this);
		showErrorLog.setShowCode(show.getCode());
		showErrorLog.setCinemaCode(show.getCinema().getCode());
		showErrorLog.setHallCode(show.getHall().getCode());
		showErrorLog.setFilmCode(show.getFilmCode());
		showErrorLog.setType(type);
		showErrorLog.setMsg(errorMsg);
		BeanUtils.copyFields(show, showErrorLog);
		errorLogs.add(showErrorLog);
		addErrorCount();
		status = SyncStatus.ERROR;
	}

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
	 * 增加删除计数。
	 */
	public void addDeleteCount() {
		deleteCount++;
	}

	/**
	 * 增加错误计数。
	 */
	public void addErrorCount() {
		errorCount++;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
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

	public Integer getDeleteCount() {
		return deleteCount;
	}

	public void setDeleteCount(Integer deleteCount) {
		this.deleteCount = deleteCount;
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

	public List<ShowErrorLog> getErrorLogs() {
		return errorLogs;
	}

	public void setErrorLogs(List<ShowErrorLog> errorLogs) {
		this.errorLogs = errorLogs;
	}
}