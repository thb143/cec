package cn.mopon.cec.site.actions.operate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;

/**
 * 运营管理菜单。
 */
@Controller("operate.menu")
@RequestMapping("/operate")
@Auth({ "CINEMA_VIEW", "FILM_VIEW", "CIRCUITSETTINGS_MANAGE", "CHANNEL_VIEW" })
public class MenuAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do 
	}
}