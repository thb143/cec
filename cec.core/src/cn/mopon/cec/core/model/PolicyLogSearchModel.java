package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.AuditStatus;
import cn.mopon.cec.core.enums.RuleStatus;
import coo.core.model.SearchModel;

/**
 * 策略审批记录搜索模型。
 */
public class PolicyLogSearchModel extends SearchModel {
	private AuditStatus status;

	private RuleStatus cinemaRuleStatus;

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}

	public RuleStatus getCinemaRuleStatus() {
		return cinemaRuleStatus;
	}

	public void setCinemaRuleStatus(RuleStatus cinemaRuleStatus) {
		this.cinemaRuleStatus = cinemaRuleStatus;
	}
}