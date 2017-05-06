package cn.mopon.cec.site.actions.stat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;

/**
 * 统计分析菜单。
 */
@Controller("stat.menu")
@RequestMapping("/stat")
@Auth({ "CINEMA_SETTLE_STAT", "CHANNEL_SETTLE_STAT", "CINEMA_SALE_STAT",
		"CHANNEL_SALE_STAT" })
public class MenuAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do
	}
}