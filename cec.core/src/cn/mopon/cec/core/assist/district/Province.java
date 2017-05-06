package cn.mopon.cec.core.assist.district;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 省份。
 */
@SuppressWarnings("serial")
@XStreamAlias("province")
public class Province extends District {
	/** 关联城市 */
	@XStreamImplicit
	private List<City> cities = new ArrayList<City>();

	/**
	 * 根据城市编码获取城市。
	 * 
	 * @param cityCode
	 *            城市编码
	 * @return 返回对应的城市，如果没找到返回null。
	 */
	public City getCity(String cityCode) {
		for (City city : cities) {
			if (city.getCode().equals(cityCode)) {
				return city;
			}
		}
		return null;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
}
