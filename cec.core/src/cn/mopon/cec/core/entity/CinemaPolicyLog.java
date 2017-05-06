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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
 * 影院结算定价策略审批记录。
 */
@Entity
@Table(name = "CEC_CinemaPolicyLog")
@Indexed(index = "CinemaPolicyLog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CinemaPolicyLog extends UuidEntity {
	/** 关联影院结算策略 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyId")
	@IndexedEmbedded(includePaths = { "name", "cinema.name",
			"cinema.county.city.name", "cinema.county.city.code",
			"cinema.county.code" })
	private CinemaPolicy policy;
	/** 提交人 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submitterId")
	private User submitter;
	/** 提交时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date submitTime;
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
	/** 审批状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private AuditStatus status = AuditStatus.AUDIT;

	public CinemaPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(CinemaPolicy policy) {
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

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}
}
