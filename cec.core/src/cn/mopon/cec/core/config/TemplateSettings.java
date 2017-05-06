package cn.mopon.cec.core.config;

import org.springframework.stereotype.Component;

import coo.core.template.AbstractTemplateSettings;

/**
 * 模版设置组件。
 */
@Component("cn.mopon.cec.core.config.TemplateSettings")
public class TemplateSettings extends AbstractTemplateSettings {
	/**
	 * 构造方法。
	 */
	public TemplateSettings() {
		addTemplatePath("classpath:/cn/mopon/cec/core/mail");
		addAutoImport("mail", "mail.ftl");
	}
}