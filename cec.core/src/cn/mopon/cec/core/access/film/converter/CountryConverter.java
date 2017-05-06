package cn.mopon.cec.core.access.film.converter;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发行国家转换器。
 */
public class CountryConverter {
	private static Logger log = LoggerFactory.getLogger(CountryConverter.class);
	private static Properties countryProperties = new Properties();

	static {
		try {
			countryProperties.load(CountryConverter.class
					.getResourceAsStream("country.properties"));
		} catch (Exception e) {
			log.error("加载发行国家配置文件时发生异常。", e);
		}
	}

	/**
	 * 根据影片编码获取发行国家。
	 * 
	 * @param filmCode
	 *            影片编码
	 * @return 返回发行国家。
	 */
	public static String getCountry(String filmCode) {
		String countryCode = filmCode.substring(0, 3);
		String country = countryProperties.getProperty(countryCode);
		if (country == null) {
			country = "其他";
		}
		return country;
	}
}
