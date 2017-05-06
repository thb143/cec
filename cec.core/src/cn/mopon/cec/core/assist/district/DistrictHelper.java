package cn.mopon.cec.core.assist.district;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import coo.base.exception.UncheckedException;
import coo.core.xstream.GenericXStream;

/**
 * 行政区划辅助类。
 */
@XStreamAlias("provinces")
public class DistrictHelper {
	private static Logger log = LoggerFactory.getLogger(DistrictHelper.class);
	private static DistrictHelper instance = new DistrictHelper();
	@XStreamImplicit
	private List<Province> provinces = new ArrayList<Province>();

	/**
	 * 私有构造函数。
	 */
	private DistrictHelper() {
		try {
			// 读取行政区划配置文件
			XStream xstream = new GenericXStream();
			xstream.processAnnotations(new Class[] { DistrictHelper.class,
					Province.class, City.class, County.class });
			xstream.fromXML(
					DistrictHelper.class.getResourceAsStream("district.xml"),
					this);
			// 建立行政区划之间的关联关系
			for (Province province : provinces) {
				for (City city : province.getCities()) {
					city.setProvince(province);
					for (County county : city.getCounties()) {
						county.setCity(city);
					}
				}
			}
		} catch (Exception e) {
			log.error("加载行政区划配置文件时发生异常。", e);
		}
	}

	/**
	 * 获取所有省份列表。
	 * 
	 * @return 返回所有省份列表。
	 */
	public static List<Province> getProvinces() {
		return instance.provinces;
	}

	/**
	 * 根据编码获取行政区划。
	 * 
	 * @param <T>
	 *            行政区划类型
	 * @param districtCode
	 *            行政区划编码
	 * @return 返回对应的行政区划。
	 */
	@SuppressWarnings("unchecked")
	public static <T extends District> T getDistrict(String districtCode) {
		if (districtCode.endsWith("0000")) {
			return (T) getProvince(districtCode);
		} else if (districtCode.endsWith("00")) {
			return (T) getCity(districtCode);
		} else {
			return (T) getCounty(districtCode);
		}
	}

	/**
	 * 根据辖区编码列表获取省份列表。
	 * 
	 * @param countyCodes
	 *            辖区编码列表
	 * @return 返回辖区编码所在的省份列表。
	 */
	public static List<Province> getProvinces(List<String> countyCodes) {
		List<Province> provinces = new ArrayList<Province>();
		for (String countyCode : countyCodes) {
			County county = getCounty(countyCode);
			addCountyToProvince(county, provinces);
		}
		return provinces;
	}

	/**
	 * 根据编码获取省份。
	 * 
	 * @param provinceCode
	 *            省份编码
	 * @return 返回对应的省份。
	 */
	private static Province getProvince(String provinceCode) {
		Province province = getProvinceFromList(provinceCode,
				instance.provinces);
		if (province == null) {
			throw new UncheckedException("未找到编码为[" + provinceCode + "]的省份。");
		}
		return province;
	}

	/**
	 * 根据编码获取城市。
	 * 
	 * @param cityCode
	 *            城市编码
	 * @return 返回对应的城市。
	 */
	private static City getCity(String cityCode) {
		Province province = getProvinceFromList(cityCode, instance.provinces);
		City city = province.getCity(cityCode);
		if (city == null) {
			throw new UncheckedException("未找到编码为[" + cityCode + "]的城市。");
		}
		return city;
	}

	/**
	 * 根据编码获取辖区。
	 * 
	 * @param countyCode
	 *            辖区编码
	 * @return 返回对应的辖区。
	 */
	private static County getCounty(String countyCode) {
		Province province = getProvinceFromList(countyCode, instance.provinces);
		for (City city : province.getCities()) {
			County county = city.getCounty(countyCode);
			if (county != null) {
				return county;
			}
		}
		throw new UncheckedException("未找到编码为[" + countyCode + "]的辖区。");
	}

	/**
	 * 将辖区加入到省份列表中，如果省份列表中不存在该辖区所属省份则增加该省份。
	 * 
	 * @param county
	 *            辖区
	 * @param provinces
	 *            省份列表
	 */
	private static void addCountyToProvince(County county,
			List<Province> provinces) {
		Province province = getProvinceFromList(county.getCity().getProvince()
				.getCode(), provinces);
		if (province == null) {
			province = new Province();
			province.setCode(county.getCity().getProvince().getCode());
			province.setName(county.getCity().getProvince().getName());
			provinces.add(province);
			Collections.sort(provinces);
		}
		addCountyToCity(county, province);
	}

	/**
	 * 将辖区加入到省份对应城市的辖区列表中，如果没有找到对应的城市则增加该城市。
	 * 
	 * @param county
	 *            辖区
	 * @param province
	 *            省份
	 */
	private static void addCountyToCity(County county, Province province) {
		City city = province.getCity(county.getCity().getCode());
		if (city == null) {
			city = new City();
			city.setProvince(province);
			city.setCode(county.getCity().getCode());
			city.setName(county.getCity().getName());
			province.getCities().add(city);
			Collections.sort(province.getCities());
		}
		if (!city.getCounties().contains(county)) {
			County newCounty = new County();
			newCounty.setCode(county.getCode());
			newCounty.setName(county.getName());
			city.getCounties().add(newCounty);
			Collections.sort(city.getCounties());
		}
	}

	/**
	 * 根据行政区划编码从省份列表中获取所属的省份。
	 * 
	 * @param districtCode
	 *            行政区划编码
	 * @param provinces
	 *            省份列表
	 * @return 返回所属的省份，如果没找到则返回null。
	 */
	private static Province getProvinceFromList(String districtCode,
			List<Province> provinces) {
		String provinceCode = districtCode.substring(0, 2) + "0000";
		for (Province province : provinces) {
			if (province.getCode().equals(provinceCode)) {
				return province;
			}
		}
		return null;
	}
}
