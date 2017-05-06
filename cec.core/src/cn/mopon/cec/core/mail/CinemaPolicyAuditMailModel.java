package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 待审核影院结算策略通知。
 */
public class CinemaPolicyAuditMailModel extends MailModel {
	private String policyName;
	private Date submitTime;

	/**
	 * 构造方法。
	 * 
	 * @param policyLog
	 *            影院结算策略审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public CinemaPolicyAuditMailModel(CinemaPolicyLog policyLog,
			List<User> users) {
		super(users);
		templateName = "cinemaPolicy-audit-mail.ftl";
		subject = "待审核影院结算策略通知";
		policyName = policyLog.getPolicy().getName();
		submitTime = policyLog.getSubmitTime();
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