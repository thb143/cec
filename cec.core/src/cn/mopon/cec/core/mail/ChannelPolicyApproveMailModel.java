package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelPolicyLog;
import cn.mopon.cec.core.entity.User;

/**
 * 待审批渠道结算策略通知。
 */
public class ChannelPolicyApproveMailModel extends MailModel {
	private String policyName;
	private Date submitTime;

	/**
	 * 构造方法。
	 * 
	 * @param policyLog
	 *            渠道结算策略审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public ChannelPolicyApproveMailModel(ChannelPolicyLog policyLog,
			List<User> users) {
		super(users);
		templateName = "channelPolicy-approve-mail.ftl";
		subject = "待审批渠道结算策略通知";
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
