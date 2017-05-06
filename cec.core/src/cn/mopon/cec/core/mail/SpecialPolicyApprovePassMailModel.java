package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 审批特殊定价策略通过通知。
 */
public class SpecialPolicyApprovePassMailModel extends MailModel {
	private String policyName;
	private Date approveTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public SpecialPolicyApprovePassMailModel(SpecialPolicyLog log,
			List<User> users) {
		super(users);
		templateName = "specialPolicy-approve-pass-mail.ftl";
		subject = "审批特殊定价策略通过通知";
		this.policyName = log.getPolicy().getName();
		this.approveTime = log.getApproveTime();
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
}