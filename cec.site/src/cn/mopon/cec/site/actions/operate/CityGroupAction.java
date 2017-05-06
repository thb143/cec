package cn.mopon.cec.site.actions.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.CityGroup;
import cn.mopon.cec.core.service.CityGroupService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 城市分组管理。
 */
@Controller
@RequestMapping("/operate")
public class CityGroupAction {
	@Resource
	private CityGroupService cityGroupService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看城市分组列表。
	 * 
	 * @param selectedCityGroupId
	 *            选择的接入类型
	 * @param model
	 *            数据模型
	 */
	@Auth("FILM_VIEW")
	@RequestMapping("cityGroup-list")
	public void cityGroupList(String selectedCityGroupId, Model model) {
		List<CityGroup> cityGroups = cityGroupService.getCityGroups();
		if (selectedCityGroupId == null && !cityGroups.isEmpty()) {
			selectedCityGroupId = cityGroups.get(0).getId();
		}
		model.addAttribute("selectedCityGroupId", selectedCityGroupId);
		model.addAttribute("cityGroups", cityGroups);
	}

	/**
	 * 新增城市分组。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("cityGroup-add")
	public void add(Model model) {
		model.addAttribute(new CityGroup());
		model.addAttribute("citySelectModel",
				cityGroupService.getSelectModel(new CityGroup()));
	}

	/**
	 * 保存城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 * @return 返回提示信息。
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("cityGroup-save")
	public ModelAndView save(CityGroup cityGroup) {
		cityGroupService.createCityGroup(cityGroup);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("cityGroup.add.success"),
				"/operate/cityGroup-list?selectedCityGroupId="
						+ cityGroup.getId());
	}

	/**
	 * 编辑城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 * @param model
	 *            数据模型
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("cityGroup-edit")
	public void edit(CityGroup cityGroup, Model model) {
		model.addAttribute("cityGroup", cityGroup);
		model.addAttribute("citySelectModel",
				cityGroupService.getSelectModel(cityGroup));
	}

	/**
	 * 更新城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 * @return 返回提示信息。
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("cityGroup-update")
	public ModelAndView update(CityGroup cityGroup) {
		cityGroupService.updateCityGroup(cityGroup);
		return NavTabResultUtils.forward(
				messageSource.get("cityGroup.edit.success"),
				"/operate/cityGroup-list?selectedCityGroupId="
						+ cityGroup.getId());
	}

	/**
	 * 删除城市分组。
	 * 
	 * @param cityGroup
	 *            城市分组
	 * @return 返回提示信息。
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("cityGroup-delete")
	public ModelAndView delete(CityGroup cityGroup) {
		cityGroupService.deleteCityGroup(cityGroup);
		return NavTabResultUtils.forward(
				messageSource.get("cityGroup.delete.success"),
				"/operate/cityGroup-list");
	}
}