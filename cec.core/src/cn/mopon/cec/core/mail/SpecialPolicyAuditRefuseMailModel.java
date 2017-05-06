package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 审核特殊定价策略退回通知。
 */
public class SpecialPolicyAuditRefuseMailModel extends MailModel {
	private String policyName;
	private String refuseNote;
	private Date auditTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public SpecialPolicyAuditRefuseMailModel(SpecialPolicyLog log,
			List<User> users) {
		super(users);
		templateName = "specialPolicy-audit-refuse-mail.ftl";
		subject = "审核特殊定价策略退回通知";
		this.policyName = log.getPolicy().getName();
		this.refuseNote = log.getRefuseNote();
		this.auditTime = log.getAuditTime();
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getRefuseNote() {
		return refuseNote;
	}

	public void setRefuseNote(String refuseNote) {
		this.refuseNote = refuseNote;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
}