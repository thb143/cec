package cn.mopon.cec.core.mail;

import cn.mopon.cec.core.entity.User;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 邮件模型抽象类。
 */
public abstract class MailModel {
	/** 模版名称 */
	protected String templateName;
	/** 应用名称 */
	protected String appName;
	/** 标题 */
	protected String subject;
	/** 发送时间 */
	protected Date sendTime = new Date();
	/** 接收邮件列表 */
	private List<String> emails = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param users
	 *            接收用户列表。
	 */
	public MailModel(List<User> users) {
		if (CollectionUtils.isNotEmpty(users)) {
			for (User user : users) {
				if (user.getEnabled() && user.getSettings().getReceiveEmail()
						&& !StringUtils.isEmpty(user.getSettings().getEmail())) {
					emails.add(user.getSettings().getEmail());
				}
			}
		}
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String systemName) {
		this.appName = systemName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public List<String> getEmails() {
		return emails;
	}
}
