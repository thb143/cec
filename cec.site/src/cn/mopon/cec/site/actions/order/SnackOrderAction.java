package cn.mopon.cec.site.actions.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.model.SnackOrderSearchModel;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.SnackOrderService;
import coo.core.security.annotations.Auth;

/**
 * 卖品订单。
 */
@Controller
@RequestMapping("/order")
public class SnackOrderAction {
	@Resource
	private SnackOrderService snackOrderService;
	@Resource
	private ChannelService channelService;

	/**
	 * 卖品正常订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("snackSuccessOrder-list")
	public void listSuccess(Model model, SnackOrderSearchModel searchModel) {
		searchModel.setOrderType("success");
		model.addAttribute("orderPages",
				snackOrderService.searchOpenOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 退卖品订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("backSnackOrder-list")
	public void listRevoked(Model model, SnackOrderSearchModel searchModel) {
		searchModel.setOrderType("back");
		model.addAttribute("orderPages",
				snackOrderService.searchBackOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 查看卖品订单信息。
	 * 
	 * @param model
	 *            数据模型
	 * @param snackOrder
	 *            卖品订单
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("snackOrder-view")
	public void view(Model model, SnackOrder snackOrder) {
		model.addAttribute("order", snackOrder);
	}

}