package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.SnackType;
import cn.mopon.cec.core.service.SnackGroupService;
import cn.mopon.cec.core.service.SnackTypeService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 卖品类型管理。
 */
@Controller
@RequestMapping("/product")
public class SnackTypeAction {
	@Resource
	private SnackTypeService snackTypeService;
	@Resource
	private SnackGroupService snackGroupService;
	@Resource
	private MessageSource messageSource;
	@Value(value = "${content.server.url}")
	private String url;

	/**
	 * 查看卖品类型页面。
	 * 
	 * @param groupId
	 *            卖品分类ID
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("snackType-view")
	public void view(String groupId, Model model) {
		url = url.endsWith("/") ? url : url + "/";
		model.addAttribute("imagePath", url);
		model.addAttribute("groupId", groupId);
		model.addAttribute("snackTypes",
				snackTypeService.getSnackTypes(groupId));
	}

	/**
	 * 新增卖品类型。
	 * 
	 * @param groupId
	 *            卖品分类ID
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackType-add")
	public void add(String groupId, Model model) {
		SnackType snackType = new SnackType();
		snackType.setGroup(snackGroupService.getSnackGroup(groupId));
		model.addAttribute(snackType);
	}

	/**
	 * 保存卖品类型。
	 * 
	 * @param snackType
	 *            卖品类型
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackType-save")
	public ModelAndView save(SnackType snackType) {
		snackTypeService.createSnackType(snackType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("snackType.add.success"),
				"/product/snackGroup-list?selectedId="
						+ snackType.getGroup().getId());
	}

	/**
	 * 编辑卖品类型。
	 * 
	 * @param model
	 *            数据模型
	 * @param snackType
	 *            卖品类型
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackType-edit")
	public void edit(Model model, SnackType snackType) {
		url = url.endsWith("/") ? url : url + "/";
		model.addAttribute("imagePath", url);
		model.addAttribute(snackTypeService.getSnackType(snackType.getId()));
	}

	/**
	 * 更新卖品类型。
	 * 
	 * @param snackType
	 *            卖品类型
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snackType-update")
	public ModelAndView update(SnackType snackType) {
		snackTypeService.updateSnackType(snackType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("snackType.edit.success"),
				"/product/snackGroup-list?selectedId="
						+ snackType.getGroup().getId());
	}
}