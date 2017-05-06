package cn.mopon.cec.api.actions.apitest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;
import coo.core.security.permission.AdminPermission;

/**
 * 运营管理菜单。
 */
@Controller("apitest.menu")
@RequestMapping("/apitest")
@Auth(AdminPermission.CODE)
public class MenuAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do
	}
}