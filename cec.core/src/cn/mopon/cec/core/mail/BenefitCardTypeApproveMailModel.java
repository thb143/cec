package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardTypeLog;
import cn.mopon.cec.core.entity.User;

/**
 * 权益卡审批邮件通知。
 */
public class BenefitCardTypeApproveMailModel extends MailModel {
	private String typeName;
	private Date submitTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            权益卡审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public BenefitCardTypeApproveMailModel(BenefitCardTypeLog log,
			List<User> users) {
		super(users);
		templateName = "benefitCardType-approve-mail.ftl";
		subject = "待审批权益卡类通知";
		typeName = log.getType().getName();
		submitTime = log.getSubmitTime();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

}
