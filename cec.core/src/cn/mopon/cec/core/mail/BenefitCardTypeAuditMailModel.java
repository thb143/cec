package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardTypeLog;
import cn.mopon.cec.core.entity.User;

/**
 * 权益卡审核邮件通知。
 */
public class BenefitCardTypeAuditMailModel extends MailModel {

	private String typeName;
	private Date submitTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            权益卡审核记录
	 * @param users
	 *            邮件接收用户
	 */
	public BenefitCardTypeAuditMailModel(BenefitCardTypeLog log,
			List<User> users) {
		super(users);
		templateName = "benefitCardType-audit-mail.ftl";
		subject = "待审核权益卡类通知";
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
