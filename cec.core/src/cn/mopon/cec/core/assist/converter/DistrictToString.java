package cn.mopon.cec.core.assist.converter;

import org.springframework.core.convert.converter.Converter;

import cn.mopon.cec.core.assist.district.District;

/**
 * 行政区划转换成字符串转换器。
 */
public class DistrictToString implements Converter<District, String> {
	@Override
	public String convert(District district) {
		return district.getCode();
	}
}
