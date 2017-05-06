package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.assist.district.District;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 影院按省份分组模型。
 */
@SuppressWarnings("serial")
public class ProvinceCinemaModel extends District {
	private List<CityCinemaModel> cities = new ArrayList<>();

	public List<CityCinemaModel> getCities() {
		return cities;
	}

	public void setCities(List<CityCinemaModel> cities) {
		this.cities = cities;
	}

	/**
	 * 获取影院按城市分组模型。
	 * 
	 * @param cinema
	 *            影院
	 */
	public void addCityModel(Cinema cinema) {
		City city = cinema.getCounty().getCity();
		for (CityCinemaModel cityCinemaModel : cities) {
			if (cityCinemaModel.getCity().getCode().equals(city.getCode())) {
				cityCinemaModel.getCinemas().add(cinema);
				return;
			}
		}
		CityCinemaModel cityCinemaModel = new CityCinemaModel();
		cityCinemaModel.setCity(city);
		cityCinemaModel.getCinemas().add(cinema);
		cities.add(cityCinemaModel);
		Collections.sort(cities);
	}
}