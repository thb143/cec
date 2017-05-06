package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.CinemaPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 审批影院结算策略退回通知。
 */
public class CinemaPolicyApproveRefuseMailModel extends MailModel {
	private String policyName;
	private Date approveTime;

	/**
	 * 构造方法。
	 * 
	 * @param policyLog
	 *            影院结算策略审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public CinemaPolicyApproveRefuseMailModel(CinemaPolicyLog policyLog,
			List<User> users) {
		super(users);
		templateName = "cinemaPolicy-approve-refuse-mail.ftl";
		subject = "审批影院结算策略退回通知";
		this.policyName = policyLog.getPolicy().getName();
		this.approveTime = policyLog.getApproveTime();
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