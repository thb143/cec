package cn.mopon.cec.site.actions.assist;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.service.SecurityService;

/**
 * 辅助组件。
 */
@Controller
@RequestMapping("/assist")
public class AssistAction {
	@Resource
	private SecurityService securityService;

	/**
	 * 查看所有可用的用户。
	 * 
	 * @param model
	 *            数据模型
	 * @param selectedUserIds
	 *            被选中的用户ID组
	 * @param inputName
	 *            用户类型（排期预警用户，渠道预警用户，订单预警用户，统计通知用户）。该参数有两个作用，参数带回和参数反传。
	 * 
	 */
	@RequestMapping("user-select")
	public void userSelect(Model model, String[] selectedUserIds,
			String inputName) {
		model.addAttribute("inputName", inputName);
		model.addAttribute("selectedUserIds", selectedUserIds);
		model.addAttribute("sendUserList", securityService.getAllEnabledUser());
	}

	/**
	 * 标注地图。
	 * 
	 * @param model
	 *            数据模型
	 * @param type
	 *            类型
	 */
	@RequestMapping("mark-map")
	public void markMap(Model model, String type) {
		model.addAttribute("type", type);
	}

	/**
	 * 百度地图。
	 * 
	 * @param model
	 *            数据模型
	 * @param type
	 *            类型
	 */
	@RequestMapping("baidu-map")
	public void baiduMap(Model model, String type) {
		model.addAttribute("type", type);
	}
}
