package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.BenefitCardStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 权益卡查询模型。
 */
public class BenefitCardSearchModel extends DateRangeSearchModel {
	/** 渠道编号 */
	private String channelId;
	/** 卡类型 */
	private String typeId;
	/** 状态 */
	private BenefitCardStatus status;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public BenefitCardStatus getStatus() {
		return status;
	}

	public void setStatus(BenefitCardStatus status) {
		this.status = status;
	}

}
