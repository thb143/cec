package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.SpecialPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 待审核特殊定价策略通知。
 */
public class SpecialPolicyAuditMailModel extends MailModel {
	private String policyName;
	private Date submitTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public SpecialPolicyAuditMailModel(SpecialPolicyLog log, List<User> users) {
		super(users);
		templateName = "specialPolicy-audit-mail.ftl";
		subject = "待审核特殊定价策略通知";
		this.policyName = log.getPolicy().getName();
		this.submitTime = log.getSubmitTime();
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
}