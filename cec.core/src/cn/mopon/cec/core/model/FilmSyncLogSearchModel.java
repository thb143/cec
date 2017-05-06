package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.SyncStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 影片同步日志查询条件。
 */
public class FilmSyncLogSearchModel extends DateRangeSearchModel {
	/** 影片同步状态 */
	private SyncStatus status;

	public SyncStatus getStatus() {
		return status;
	}

	public void setStatus(SyncStatus status) {
		this.status = status;
	}

}