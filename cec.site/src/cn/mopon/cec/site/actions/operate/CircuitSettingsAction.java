package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.CircuitSettings;
import cn.mopon.cec.core.service.Circuit;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.NavTabResultUtils;

/**
 * 院线设置管理。
 */
@Controller
@RequestMapping("/operate")
public class CircuitSettingsAction {
	@Resource
	private Circuit circuit;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看院线设置。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("CIRCUITSETTINGS_MANAGE")
	@RequestMapping("circuitSettings-view")
	public void view(Model model) {
		model.addAttribute("circuitSettings", circuit.getCircuitSettings());
	}

	/**
	 * 保存院线设置。
	 * 
	 * @param circuitSettings
	 *            院线设置
	 * @return 返回提示信息。
	 */
	@Auth("CIRCUITSETTINGS_MANAGE")
	@RequestMapping("circuitSettings-save")
	public ModelAndView save(CircuitSettings circuitSettings) {
		circuit.save(circuitSettings);
		return NavTabResultUtils.reload(messageSource
				.get("circuitSettings.set.success"));
	}
}