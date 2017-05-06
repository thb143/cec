package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 影院按城市分组列表模型。
 */
public class CityCinemaListModel {
	private List<CityCinemaModel> items = new ArrayList<CityCinemaModel>();

	/**
	 * 增加影院。
	 * 
	 * @param cinema
	 *            影院
	 */
	public void addCinema(Cinema cinema) {
		CityCinemaModel cityCinemaModel = getCityCinemaModel(cinema.getCounty()
				.getCity());
		cityCinemaModel.addCinema(cinema);
	}

	/**
	 * 获取影院按城市分组模型。
	 * 
	 * @param city
	 *            城市
	 * @return 返回影院按城市分组模型。
	 */
	private CityCinemaModel getCityCinemaModel(City city) {
		for (CityCinemaModel cityCinemaModel : items) {
			if (cityCinemaModel.getCity().equals(city)) {
				return cityCinemaModel;
			}
		}
		CityCinemaModel cityCinemaModel = new CityCinemaModel();
		cityCinemaModel.setCity(city);
		items.add(cityCinemaModel);
		Collections.sort(items);
		return cityCinemaModel;
	}

	public List<CityCinemaModel> getItems() {
		return items;
	}

	public void setItems(List<CityCinemaModel> items) {
		this.items = items;
	}
}