package cn.mopon.cec.site.actions.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;

/**
 * 订单管理菜单。
 */
@Controller("order.menu")
@RequestMapping("/order")
@Auth("ORDER_VIEW")
public class MenuAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do
	}
}
