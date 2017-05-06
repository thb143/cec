package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.SyncStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 排期同步日志查询条件。
 */
public class ShowSyncLogSearchModel extends DateRangeSearchModel {
	/** 影院ID */
	private String cinemaId;
	/** 排期同步状态 */
	private SyncStatus status;

	public String getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	public SyncStatus getStatus() {
		return status;
	}

	public void setStatus(SyncStatus status) {
		this.status = status;
	}
}