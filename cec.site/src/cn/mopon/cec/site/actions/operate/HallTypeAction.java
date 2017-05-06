package cn.mopon.cec.site.actions.operate;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.HallType;
import cn.mopon.cec.core.service.HallTypeService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 影厅类型管理
 */
@Controller
@RequestMapping("/operate")
public class HallTypeAction {
	@Resource
	private HallTypeService hallTypeService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看影厅类型列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param selectedId
	 *            选中的机构ID
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hallType-list")
	public void list(Model model, String selectedId) {
		List<HallType> hallTypeList = hallTypeService.getHallTypeList();
		model.addAttribute("selectedId", selectedId);
		model.addAttribute("hallTypeList", hallTypeList);
	}

	/**
	 * 新增影厅类型。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hallType-add")
	public void add(Model model) {
		HallType hallType = new HallType();
		model.addAttribute(hallType);
	}

	/**
	 * 保存影厅类型
	 * 
	 * @param hallType
	 *            影厅类型
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hallType-save")
	public ModelAndView save(HallType hallType) {
		hallTypeService.createHallType(hallType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("hallType.add.success"),
				"/operate/hallType-list?selectedId=" + hallType.getId());
	}

	/**
	 * 编辑影厅类型。
	 * 
	 * @param model
	 *            数据模型
	 * @param hallType
	 *            影厅类型
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hallType-edit")
	public void edit(Model model, HallType hallType) {
		model.addAttribute("hallType", hallType);
	}

	/**
	 * 更新影厅类型。
	 * 
	 * @param hallType
	 *            影厅类型
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_MANAGE")
	@RequestMapping("hallType-update")
	public ModelAndView update(HallType hallType) {
		hallTypeService.updateHallType(hallType);
		return NavTabResultUtils.forward(
				messageSource.get("hallType.edit.success"),
				"/operate/hallType-list?selectedId=" + hallType.getId());
	}

}