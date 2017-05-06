package cn.mopon.cec.core.model;

import coo.core.model.DateRangeSearchModel;

/**
 * 权益卡用户查询对象。
 */
public class BenefitCardUserSearchModel extends DateRangeSearchModel {

	private String channelId;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
