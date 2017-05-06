package cn.mopon.cec.site.actions.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;

/**
 * 产品管理菜单。
 */
@Controller("product.menu")
@RequestMapping("/product")
@Auth({ "PRODUCT_VIEW" })
public class MenuAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do
	}
}
