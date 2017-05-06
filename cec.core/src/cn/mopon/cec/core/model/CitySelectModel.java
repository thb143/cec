package cn.mopon.cec.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.assist.district.DistrictHelper;
import cn.mopon.cec.core.assist.district.Province;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import coo.base.util.CollectionUtils;

/**
 * 城市选择模型。
 */
@SuppressWarnings("serial")
public class CitySelectModel implements Serializable {
	private List<CityGroupModel> cityGroupModels = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param countyCodes
	 *            区域列表。
	 */
	public CitySelectModel(List<String> countyCodes) {
		for (Province province : DistrictHelper.getProvinces(countyCodes)) {
			List<City> cities = province.getCities();
			for (City city : cities) {
				addCity(city);
			}
		}
		Collections.sort(cityGroupModels);
	}

	/**
	 * 生成城市分组模型。
	 * 
	 * @param selectedCities
	 *            已选城市
	 */
	public void genSelectedModel(List<City> selectedCities) {
		CityGroupModel cityGroupModel = new CityGroupModel("已选城市");
		for (City city : selectedCities) {
			removeCity(city);
			cityGroupModel.addCity(city);
		}
		cityGroupModels.add(0, cityGroupModel);
		List<CityGroupModel> emptyCityGroupModels = new ArrayList<>();
		for (CityGroupModel model : cityGroupModels) {
			if (CollectionUtils.isEmpty(model.getCities())) {
				emptyCityGroupModels.add(model);
			}
		}
		if (CollectionUtils.isNotEmpty(emptyCityGroupModels)) {
			cityGroupModels.removeAll(emptyCityGroupModels);
		}
	}

	/**
	 * 添加城市
	 * 
	 * @param city
	 *            城市
	 */
	private void addCity(City city) {
		String alpha = PinyinHelper
				.convertToPinyinString(city.getName(), "",
						PinyinFormat.WITHOUT_TONE).toUpperCase()
				.substring(0, 1);

		getCityGroupModel(alpha).addCity(city);
	}

	/**
	 * 移除城市。
	 * 
	 * @param city
	 *            城市
	 */
	private void removeCity(City city) {
		for (CityGroupModel model : cityGroupModels) {
			if (model.getCities().contains(city)) {
				model.getCities().remove(city);
				break;
			}
		}
	}

	/**
	 * 获取城市分组模型。
	 * 
	 * @param alpha
	 *            首字母
	 * @return 返回城市分组模型。
	 */
	private CityGroupModel getCityGroupModel(String alpha) {
		for (CityGroupModel groupModel : cityGroupModels) {
			if (groupModel.getAlpha().equals(alpha)) {
				return groupModel;
			}
		}
		CityGroupModel groupModel = new CityGroupModel(alpha);
		cityGroupModels.add(groupModel);
		return groupModel;
	}

	public List<CityGroupModel> getCityGroupModels() {
		return cityGroupModels;
	}

	public void setCityGroupModels(List<CityGroupModel> cityGroupModels) {
		this.cityGroupModels = cityGroupModels;
	}
}