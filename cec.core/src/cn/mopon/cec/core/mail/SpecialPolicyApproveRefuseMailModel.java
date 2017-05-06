package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 审批特殊定价策略退回通知。
 */
public class SpecialPolicyApproveRefuseMailModel extends MailModel {
	private String policyName;
	private String refuseNote;
	private Date approveTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public SpecialPolicyApproveRefuseMailModel(SpecialPolicyLog log,
			List<User> users) {
		super(users);
		templateName = "specialPolicy-approve-refuse-mail.ftl";
		subject = "审批特殊定价策略退回通知";
		this.policyName = log.getPolicy().getName();
		this.refuseNote = log.getRefuseNote();
		this.approveTime = log.getApproveTime();
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

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
}