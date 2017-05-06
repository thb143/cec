package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.MemberAccessTypeService;
import cn.mopon.cec.core.service.MemberSettingsService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.NavTabResultUtils;

/**
 * 会员接入设置管理。
 */
@Controller
@RequestMapping("/operate")
public class MemberSettingsAction {
	@Resource
	private MemberSettingsService memberSettingsService;
	@Resource
	private MemberAccessTypeService memberAccessTypeService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 编辑会员接入设置。
	 * 
	 * @param model
	 *            数据模型
	 * @param id
	 *            会员设置ID
	 */
	@Auth("CINEMA_VIEW")
	@RequestMapping("memberSettings-edit")
	public void editMemberSettings(Model model, String id) {
		MemberSettings memberSettings = memberSettingsService
				.getMemberSettings(id);
		if (memberSettings == null) {
			memberSettings = new MemberSettings();
			memberSettings.setId(id);
		}
		model.addAttribute("memberSettings", memberSettings);
		model.addAttribute("memberAccessTypes",
				memberAccessTypeService.getMemberAccessTypeList(id));
	}

	/**
	 * 更新会员接入设置。
	 * 
	 * @param memberSettings
	 *            会员接入设置
	 * @return 返回提示信息。
	 */
	@Auth("CINEMA_SET")
	@RequestMapping("memberSettings-update")
	public ModelAndView updateMemberSettings(MemberSettings memberSettings) {
		Cinema origCinema = cinemaService.getCinema(memberSettings.getId());
		memberSettingsService.updateMemberSettings(origCinema, memberSettings);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("memberSettings.edit.success"), "cinemaBox");
	}

}
