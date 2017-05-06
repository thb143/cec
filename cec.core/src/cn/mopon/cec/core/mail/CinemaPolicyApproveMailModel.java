package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 待审批影院结算策略通知。
 */
public class CinemaPolicyApproveMailModel extends MailModel {
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
	public CinemaPolicyApproveMailModel(CinemaPolicyLog policyLog,
			List<User> users) {
		super(users);
		templateName = "cinemaPolicy-approve-mail.ftl";
		subject = "待审批影院结算策略通知";
		this.policyName = policyLog.getPolicy().getName();
		this.submitTime = policyLog.getSubmitTime();
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