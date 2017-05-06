package cn.mopon.cec.core.mail;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import coo.base.util.CollectionUtils;
import coo.core.mail.MailSender;
import coo.core.mail.TemplateMail;

/**
 * 邮件服务组件。
 */
@Service
public class MailService {
	@Resource
	private MailSender mailSender;
	@Value("${app.mail}")
	private String mail;
	@Value("${app.name}")
	private String appName;

	/**
	 * 异步发送邮件。
	 * 
	 * @param model
	 *            邮件模型
	 */
	@Async
	public void send(MailModel model) {
		sendMail(model);
	}

	/**
	 * 同步发送邮件。
	 * 
	 * @param model
	 *            邮件模型
	 */
	public void syncSend(MailModel model) {
		sendMail(model);
	}

	/**
	 * 发送邮件。
	 * 
	 * @param model
	 *            邮件模型
	 */
	private void sendMail(MailModel model) {
		if (CollectionUtils.isEmpty(model.getEmails())) {
			return;
		}
		TemplateMail templateMail = new TemplateMail();
		templateMail.setTemplateName(model.getTemplateName());
		for (String email : model.getEmails()) {
			templateMail.addTo(email);
		}
		templateMail.setFrom(mail);
		templateMail.setSubject(appName + "-" + model.getSubject());

		model.setAppName(appName);
		templateMail.setModel(model);
		mailSender.send(templateMail);
	}
}
