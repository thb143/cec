package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 审核影院结算策略退回通知。
 */
public class CinemaPolicyAuditRefuseMailModel extends MailModel {
	private String policyName;
	private Date auditTime;

	/**
	 * 构造方法。
	 * 
	 * @param policyLog
	 *            影院结算策略审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public CinemaPolicyAuditRefuseMailModel(CinemaPolicyLog policyLog,
			List<User> users) {
		super(users);
		templateName = "cinemaPolicy-audit-refuse-mail.ftl";
		subject = "审核影院结算策略退回通知";
		this.policyName = policyLog.getPolicy().getName();
		this.auditTime = policyLog.getAuditTime();
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

}