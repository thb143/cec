package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.EnabledStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 权益卡类查询模型。
 */
public class BenefitCardTypeSearchModel extends DateRangeSearchModel {
	private EnabledStatus status;

	public EnabledStatus getStatus() {
		return status;
	}

	public void setStatus(EnabledStatus status) {
		this.status = status;
	}

}
