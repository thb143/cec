package cn.mopon.cec.core.mail;

import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.BenefitCardTypeLog;
import cn.mopon.cec.core.entity.User;

/**
 * 权益卡审核退回邮件通知。
 */
public class BenefitCardTypeApprovePassMailModel extends MailModel {

	private String typeName;
	private Date approveTime;

	/**
	 * 构造方法。
	 * 
	 * @param log
	 *            权益卡审批记录
	 * @param users
	 *            邮件接收用户
	 */
	public BenefitCardTypeApprovePassMailModel(BenefitCardTypeLog log,
			List<User> users) {
		super(users);
		templateName = "benefitCardType-approve-pass-mail.ftl";
		subject = "审批权益卡类通过通知";
		typeName = log.getType().getName();
		approveTime = log.getApproveTime();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

}
