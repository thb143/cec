package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.SnackGroup;
import cn.mopon.cec.core.service.SnackGroupService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 卖品分类管理。
 */
@Controller
@RequestMapping("/product")
public class SnackGroupAction {
	@Resource
	private SnackGroupService snackGroupService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看卖品分类页面。
	 * 
	 * @param selectedId
	 *            选择卖品分类ID
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("snackGroup-list")
	public void list(String selectedId, Model model) {
		model.addAttribute("selectedId", selectedId);
		model.addAttribute("snackGroups", snackGroupService.getSnackGroups());
	}

	/**
	 * 新增卖品分类。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackGroup-add")
	public void add(Model model) {
		model.addAttribute(new SnackGroup());
	}

	/**
	 * 保存卖品分类。
	 * 
	 * @param snackGroup
	 *            卖品分类
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackGroup-save")
	public ModelAndView save(SnackGroup snackGroup) {
		snackGroupService.createSnackGroup(snackGroup);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("snackGroup.add.success"),
				"/product/snackGroup-list?selectedId=" + snackGroup.getId());
	}

	/**
	 * 编辑卖品分类。
	 * 
	 * @param model
	 *            数据模型
	 * @param snackGroup
	 *            卖品分类
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackGroup-edit")
	public void edit(Model model, SnackGroup snackGroup) {
		model.addAttribute(snackGroupService.getSnackGroup(snackGroup.getId()));
	}

	/**
	 * 更新卖品分类。
	 * 
	 * @param snackGroup
	 *            卖品分类
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackGroup-update")
	public ModelAndView update(SnackGroup snackGroup) {
		snackGroupService.updateSnackGroup(snackGroup);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("snackGroup.edit.success"),
				"/product/snackGroup-list?selectedId=" + snackGroup.getId());
	}
}