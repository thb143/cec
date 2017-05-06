package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.AuditStatus;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 特殊定价策略审批记录。
 */
@Entity
@Table(name = "CEC_SpecialPolicyLog")
@Indexed(index = "SpecialPolicyLog")
public class SpecialPolicyLog extends UuidEntity {
	/** 关联策略 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyId")
	@IndexedEmbedded(includePaths = "name")
	private SpecialPolicy policy;
	/** 提交人 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submitterId")
	private User submitter;
	/** 提交时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date submitTime;
	/** 提交说明 */
	private String submitRemark;
	/** 审核人 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auditorId")
	private User auditor;
	/** 审核时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTime;
	/** 审批人 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approverId")
	private User approver;
	/** 审批时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date approveTime;
	/** 退回意见 */
	private String refuseNote;
	/** 审批状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private AuditStatus status = AuditStatus.AUDIT;

	public SpecialPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(SpecialPolicy policy) {
		this.policy = policy;
	}

	public User getSubmitter() {
		return submitter;
	}

	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getSubmitRemark() {
		return submitRemark;
	}

	public void setSubmitRemark(String submitRemark) {
		this.submitRemark = submitRemark;
	}

	public User getAuditor() {
		return auditor;
	}

	public void setAuditor(User auditor) {
		this.auditor = auditor;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public User getApprover() {
		return approver;
	}

	public void setApprover(User approver) {
		this.approver = approver;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public String getRefuseNote() {
		return refuseNote;
	}

	public void setRefuseNote(String refuseNote) {
		this.refuseNote = refuseNote;
	}

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}
}
