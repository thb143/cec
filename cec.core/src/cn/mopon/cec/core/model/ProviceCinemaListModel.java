package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.assist.district.Province;
import cn.mopon.cec.core.entity.Cinema;

/**
 * 影院按省份分组列表模型。
 */
public class ProviceCinemaListModel {
	private List<ProvinceCinemaModel> items = new ArrayList<>();

	/**
	 * 获取影院按省份分组模型。
	 * 
	 * @param cinema
	 *            影院
	 */
	public void addProvinceCinemaModel(Cinema cinema) {
		Province province = cinema.getCounty().getCity().getProvince();
		for (ProvinceCinemaModel provinceCinemaModel : items) {
			if (provinceCinemaModel.getCode().equals(province.getCode())) {
				provinceCinemaModel.addCityModel(cinema);
				return;
			}
		}
		ProvinceCinemaModel provinceCinemaModel = new ProvinceCinemaModel();
		provinceCinemaModel.setCode(province.getCode());
		provinceCinemaModel.setName(province.getName());

		provinceCinemaModel.addCityModel(cinema);

		items.add(provinceCinemaModel);
	}

	public List<ProvinceCinemaModel> getItems() {
		return items;
	}

	public void setItems(List<ProvinceCinemaModel> items) {
		this.items = items;
	}
}