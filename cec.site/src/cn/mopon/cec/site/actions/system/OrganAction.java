package cn.mopon.cec.site.actions.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Organ;
import cn.mopon.cec.core.service.SecurityService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.core.security.permission.AdminPermission;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 机构管理
 */
@Controller("system.organ")
@RequestMapping("/system")
@Auth(AdminPermission.CODE)
public class OrganAction {
	@Resource
	private SecurityService securityService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看机构列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param selectedOrganId
	 *            选中的机构ID
	 */
	@RequestMapping("organ-list")
	public void list(Model model, String selectedOrganId) {
		Organ rootOrgan = securityService.getCurrentOrgan();
		if (selectedOrganId == null) {
			selectedOrganId = rootOrgan.getId();
		}
		model.addAttribute("selectedOrganId", selectedOrganId);
		model.addAttribute("rootOrgan", rootOrgan);
	}

	/**
	 * 新增机构。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("organ-add")
	public void add(Model model) {
		Organ rootOrgan = securityService.getCurrentOrgan();
		Organ organ = new Organ();
		organ.setParent(rootOrgan);
		model.addAttribute("parentOrgans", rootOrgan.getOrganTree());
		model.addAttribute("rootOrgan", rootOrgan);
		model.addAttribute(organ);
	}

	/**
	 * 保存机构
	 * 
	 * @param organ
	 *            机构
	 * @return 返回提示信息。
	 */
	@RequestMapping("organ-save")
	public ModelAndView save(Organ organ) {
		securityService.createOrgan(organ);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("organ.add.success"),
				"/system/organ-list?selectedOrganId=" + organ.getId());
	}

	/**
	 * 编辑机构。
	 * 
	 * @param model
	 *            数据模型
	 * @param organ
	 *            机构
	 */
	@RequestMapping("organ-edit")
	public void edit(Model model, Organ organ) {
		Organ rootOrgan = securityService.getCurrentOrgan();
		model.addAttribute("rootOrgan", rootOrgan);
		List<Organ> parentOrgans = rootOrgan.getOrganTree();
		parentOrgans.remove(organ);
		model.addAttribute("parentOrgans", parentOrgans);
	}

	/**
	 * 更新机构。
	 * 
	 * @param organ
	 *            机构
	 * @return 返回提示信 息。
	 */
	@RequestMapping("organ-update")
	public ModelAndView update(Organ organ) {
		securityService.updateOrgan(organ);
		return NavTabResultUtils.forward(
				messageSource.get("organ.edit.success"),
				"/system/organ-list?selectedOrganId=" + organ.getId());
	}

	/**
	 * 删除机构。
	 * 
	 * @param organ
	 *            机构
	 * @return 返回提示信息。
	 */
	@RequestMapping("organ-delete")
	public ModelAndView delete(Organ organ) {
		securityService.deleteOrgan(organ);
		return NavTabResultUtils.forward(
				messageSource.get("organ.delete.success"),
				"organ-list?selectedOrganId="
						+ securityService.getCurrentOrgan().getId());
	}
}