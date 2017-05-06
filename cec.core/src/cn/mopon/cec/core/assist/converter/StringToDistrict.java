package cn.mopon.cec.core.assist.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import cn.mopon.cec.core.assist.district.District;
import cn.mopon.cec.core.assist.district.DistrictHelper;

/**
 * 字符串转换成行政区划转换器。
 */
public class StringToDistrict implements ConverterFactory<String, District> {
	@Override
	public <T extends District> Converter<String, T> getConverter(
			Class<T> targetType) {
		return new StringToDistrictConverter<T>();
	}

	/**
	 * 字符串转换成行政区划转换器。
	 * 
	 * @param <T>
	 *            行政区划类型
	 */
	private class StringToDistrictConverter<T extends District> implements
			Converter<String, T> {
		/**
		 * 将字符串值转换为行政区划对象。
		 * 
		 * @param source
		 *            字符串值
		 * @return 返回行政区划对象。
		 */
		public T convert(String source) {
			if (source.length() == 0) {
				return null;
			}
			return DistrictHelper.getDistrict(source.trim());
		}
	}
}
