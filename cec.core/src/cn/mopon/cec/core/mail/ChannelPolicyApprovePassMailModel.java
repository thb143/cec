package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 审批渠道结算策略通过通知。
 */
public class ChannelPolicyApprovePassMailModel extends MailModel {
	private String policyName;
	private Date approveTime;

	/**
	 * 构造方法。
	 * 
	 * @param policyLog
	 *            渠道结算策略审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public ChannelPolicyApprovePassMailModel(ChannelPolicyLog policyLog,
			List<User> users) {
		super(users);
		templateName = "channelPolicy-approve-pass-mail.ftl";
		subject = "审批渠道结算策略通过通知";
		this.policyName = policyLog.getPolicy().getName();
		this.approveTime = policyLog.getApproveTime();
	}

	public String getPolicyName() {
		return policyName;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

}