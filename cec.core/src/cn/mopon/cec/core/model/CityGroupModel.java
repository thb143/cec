package cn.mopon.cec.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.assist.district.City;

/**
 * 城市分组模型。
 */
@SuppressWarnings("serial")
public class CityGroupModel implements Serializable, Comparable<CityGroupModel> {
	private String alpha;
	private List<City> cities = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param alpha
	 *            首字母
	 */
	public CityGroupModel(String alpha) {
		this.alpha = alpha;
	}

	@Override
	public int compareTo(CityGroupModel o) {
		return alpha.compareTo(o.getAlpha());
	}

	/**
	 * 列表中增加城市。
	 * 
	 * @param city
	 *            城市
	 */
	public void addCity(City city) {
		cities.add(city);
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
}