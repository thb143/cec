package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.ShowStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 排期查询条件。
 */
public class ShowSearchModel extends DateRangeSearchModel {
	/** 影院ID */
	private String cinemaId;
	/** 状态 */
	private ShowStatus status;

	public String getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	public ShowStatus getStatus() {
		return status;
	}

	public void setStatus(ShowStatus status) {
		this.status = status;
	}
}