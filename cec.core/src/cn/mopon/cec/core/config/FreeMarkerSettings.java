package cn.mopon.cec.core.config;

import org.springframework.stereotype.Component;

import cn.mopon.cec.core.assist.district.DistrictHelper;
import coo.mvc.config.AbstractFreeMarkerSettings;

/**
 * FreeMarker设置组件。
 */
@Component("cn.mopon.cec.core.config.FreeMarkerSettings")
public class FreeMarkerSettings extends AbstractFreeMarkerSettings {
	/**
	 * 构造方法。
	 */
	public FreeMarkerSettings() {
		setOrder(100);
		addEnumPackage("cn.mopon.cec.core.enums");
		addEnumPackage("cn.mopon.cec.core.assist.enums");
		addStaticClass(DistrictHelper.class);
		addGlobalBean("circuit", "circuit");
	}
}