package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardTypeLog;
import cn.mopon.cec.core.entity.User;

/**
 * 权益卡审核退回邮件通知。
 */
public class BenefitCardTypeAuditRefuseMailModel extends MailModel {

	private String typeName;
	private Date auditTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            权益卡审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public BenefitCardTypeAuditRefuseMailModel(BenefitCardTypeLog log,
			List<User> users) {
		super(users);
		templateName = "benefitCardType-audit-refuse-mail.ftl";
		subject = "审核权益卡类退回通知";
		typeName = log.getType().getName();
		auditTime = log.getAuditTime();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
}
