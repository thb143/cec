package cn.mopon.cec.api.config;

import org.springframework.stereotype.Component;

import coo.mvc.config.AbstractFreeMarkerSettings;

/**
 * FreeMarker设置组件。
 */
@Component("cn.mopon.cec.api.config.FreeMarkerSettings")
public class FreeMarkerSettings extends AbstractFreeMarkerSettings {
	/**
	 * 构造方法。
	 */
	public FreeMarkerSettings() {
		setOrder(100);
		addTemplatePath("classpath:/cn/mopon/cec/api/actions/");
	}
}