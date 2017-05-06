package cn.mopon.cec.core.config;

import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Component;

import cn.mopon.cec.core.assist.converter.DistrictToString;
import cn.mopon.cec.core.assist.converter.StringToDistrict;
import coo.mvc.config.AbstractConversionConfigurer;

/**
 * 转换器配置组件。
 */
@Component("cn.mopon.cec.core.config.ConversionConfigurer")
public class ConversionConfigurer extends AbstractConversionConfigurer {
	@Override
	public void config(FormattingConversionService conversionService) {
		conversionService.addConverter(new DistrictToString());
		conversionService.addConverterFactory(new StringToDistrict());
	}
}
