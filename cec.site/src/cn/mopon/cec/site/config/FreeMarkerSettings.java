package cn.mopon.cec.site.config;

import org.springframework.stereotype.Component;

import coo.mvc.config.AbstractFreeMarkerSettings;

/**
 * FreeMarker设置组件。
 */
@Component("cn.mopon.cec.site.config.FreeMarkerSettings")
public class FreeMarkerSettings extends AbstractFreeMarkerSettings {
	/**
	 * 构造方法。
	 */
	public FreeMarkerSettings() {
		setOrder(100);
		addTemplatePath("classpath:/cn/mopon/cec/site/macros/");
		addTemplatePath("classpath:/cn/mopon/cec/site/actions/");
		addAutoImport("ope", "ope.ftl");
		addAutoImport("ass", "ass.ftl");
		addAutoImport("sys", "sys.ftl");
		addAutoImport("pro", "pro.ftl");
		addAutoImport("ord", "ord.ftl");
		addAutoImport("pri", "pri.ftl");
	}
}