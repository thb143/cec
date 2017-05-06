package cn.mopon.cec.site.actions.assist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.assist.district.City;
import cn.mopon.cec.core.assist.district.County;
import cn.mopon.cec.core.assist.district.DistrictHelper;
import cn.mopon.cec.core.assist.district.Province;
import cn.mopon.cec.core.entity.Cinema;
import coo.base.util.StringUtils;

/**
 * 行政区划辅助。
 */
@Controller
@RequestMapping("/assist")
public class DistrictAction {
	/**
	 * 根据省份编码获取城市列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param provinceCode
	 *            省份编码
	 */
	@RequestMapping("city-list")
	public void listCity(Model model, String provinceCode) {
		if (StringUtils.isNotBlank(provinceCode)) {
			Province province = DistrictHelper.getDistrict(provinceCode);
			model.addAttribute("cities", province.getCities());
		}
	}

	/**
	 * 根据城市编码获取辖区列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param cityCode
	 *            城市编码
	 */
	@RequestMapping("county-list")
	public void listCounty(Model model, String cityCode) {
		if (StringUtils.isNotBlank(cityCode)) {
			City city = DistrictHelper.getDistrict(cityCode);
			model.addAttribute("counties", city.getCounties());
		}
	}

	/**
	 * 行政区划测试。
	 * 
	 * @param model
	 *            数据模型
	 * @param county
	 *            辖区编码
	 */
	@RequestMapping("district-test")
	public void test(Model model, String county) {
		Cinema cinema = new Cinema();
		if (county != null) {
			County cinemaCounty = DistrictHelper.getDistrict(county);
			cinema.setCounty(cinemaCounty);
		}
		model.addAttribute("cinema", cinema);
	}
}
