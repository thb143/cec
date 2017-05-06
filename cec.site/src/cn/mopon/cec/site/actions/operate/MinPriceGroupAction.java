package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.MinPriceGroup;
import cn.mopon.cec.core.service.CityGroupService;
import cn.mopon.cec.core.service.MinPriceGroupService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 影片最低价管理。
 */
@Controller
@RequestMapping("/operate")
public class MinPriceGroupAction {
	@Resource
	private MinPriceGroupService minPriceGroupService;
	@Resource
	private CityGroupService cityGroupService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 新增影片最低价分类。
	 * 
	 * @param model
	 *            数据模型
	 * @param film
	 *            影片
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("minPriceGroup-add")
	public void add(Model model, Film film) {
		model.addAttribute(film);
		model.addAttribute(new MinPriceGroup());
	}

	/**
	 * 保存影片最低价分类。
	 * 
	 * @param minPriceGroup
	 *            影片最低价分类
	 * @return 返回提示信息。
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("minPriceGroup-save")
	public ModelAndView save(MinPriceGroup minPriceGroup) {
		minPriceGroupService.createMinPriceGroup(minPriceGroup);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("minPriceGroup.save.success"));
	}

	/**
	 * 删除影片最低价分类。
	 * 
	 * @param minPriceGroup
	 *            影片最低价分类
	 * @return 返回提示信息。
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("minPriceGroup-delete")
	public ModelAndView delete(MinPriceGroup minPriceGroup) {
		minPriceGroupService.deleteMinPriceGroup(minPriceGroup);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("minPriceGroup.delete.success"));
	}

	/**
	 * 编辑影片最低价分类。
	 * 
	 * @param model
	 *            数据模型
	 * @param minPriceGroup
	 *            影片最低价分组
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("minPriceGroup-edit")
	public void edit(Model model, MinPriceGroup minPriceGroup) {
		model.addAttribute(minPriceGroup);
		model.addAttribute("cityGroups", cityGroupService.getCityGroups());
	}

	/**
	 * 更新影片最低价分类。
	 * 
	 * @param minPriceGroup
	 *            影片最低价分类
	 * @return 返回提示信息。
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("minPriceGroup-update")
	public ModelAndView update(MinPriceGroup minPriceGroup) {
		minPriceGroupService.updateMinPriceGroup(minPriceGroup);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("minPriceGroup.edit.success"));
	}

	/**
	 * 编辑影片最低价分类。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("FILM_MANAGE")
	@RequestMapping("city-select")
	public void citySelect(Model model) {
		model.addAttribute(new MinPriceGroup());
		model.addAttribute("cityGroups", cityGroupService.getCityGroups());
	}
}