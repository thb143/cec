package cn.mopon.cec.core.config;

import org.springframework.stereotype.Component;

import coo.core.hibernate.AbstractLocalSessionSettings;

/**
 * SessionFactory设置。
 */
@Component("cn.mopon.cec.core.config.LocalSessionSettings")
public class LocalSessionSettings extends AbstractLocalSessionSettings {
	/**
	 * 构造方法。
	 */
	public LocalSessionSettings() {
		addPackageToScan("cn.mopon.cec.core.entity");
		addAnnotatedPackage("cn.mopon.cec.core.assist.usertype");
	}
}