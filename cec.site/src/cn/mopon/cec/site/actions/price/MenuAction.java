package cn.mopon.cec.site.actions.price;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;

/**
 * 产品管理菜单。
 */
@Controller("price.menu")
@RequestMapping("/price")
@Auth({ "POLICY_VIEW", "POLICY_MANAGE", "POLICY_SWITCH", "POLICY_AUDIT",
		"POLICY_APPROVE", "PRODUCT_MANAGE" })
public class MenuAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		//nothing to do
	}
}
