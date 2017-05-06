package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.AuditStatus;
import coo.core.model.SearchModel;

/**
 * 权益卡审批记录搜索模型。
 */
public class CardTypeLogSearchModel extends SearchModel {
	private AuditStatus status;

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}

}
