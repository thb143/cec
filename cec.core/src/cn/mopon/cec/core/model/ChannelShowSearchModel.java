package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.ShelveStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 渠道排期查询条件。
 */
public class ChannelShowSearchModel extends DateRangeSearchModel {
	/** 渠道ID */
	private String channelId;
	/** 状态 */
	private ShelveStatus status = ShelveStatus.ON;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public ShelveStatus getStatus() {
		return status;
	}

	public void setStatus(ShelveStatus status) {
		this.status = status;
	}
}