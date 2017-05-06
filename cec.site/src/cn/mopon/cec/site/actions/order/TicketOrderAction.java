package cn.mopon.cec.site.actions.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.model.TicketOrderSearchModel;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.TicketOrderService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 票务订单。
 */
@Controller
@RequestMapping("/order")
public class TicketOrderAction {
	@Resource
	private TicketOrderService ticketOrderService;
	@Resource
	private ChannelService channelService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 选座票正常订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-success-list")
	public void listSuccess(Model model, TicketOrderSearchModel searchModel) {
		searchModel.setOrderStatus(TicketOrderStatus.SUCCESS);
		searchModel.setOrderBy("confirmTime");
		model.addAttribute("orderPages",
				ticketOrderService.searchTicketOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 选座票异常订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-paid-list")
	public void listPaid(Model model, TicketOrderSearchModel searchModel) {
		searchModel.setOrderStatus(TicketOrderStatus.PAID);
		searchModel.setOrderBy("payTime");
		model.addAttribute("orderPages",
				ticketOrderService.searchTicketOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 选座票失败订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-failed-list")
	public void listFailed(Model model, TicketOrderSearchModel searchModel) {
		searchModel.setOrderStatus(TicketOrderStatus.FAILED);
		searchModel.setOrderBy("confirmTime");
		model.addAttribute("orderPages",
				ticketOrderService.searchTicketOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 选座票未支付订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-unpaid-list")
	public void listUnpaid(Model model, TicketOrderSearchModel searchModel) {
		searchModel.setOrderStatus(TicketOrderStatus.UNPAID);
		searchModel.setOrderBy("createTime");
		model.addAttribute("orderPages",
				ticketOrderService.searchTicketOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 选座票退票订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-revoked-list")
	public void listRevoked(Model model, TicketOrderSearchModel searchModel) {
		searchModel.setOrderStatus(TicketOrderStatus.REVOKED);
		searchModel.setOrderBy("revokeTime");
		model.addAttribute("orderPages",
				ticketOrderService.searchTicketOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 选座票已取消订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-canceled-list")
	public void listCanceled(Model model, TicketOrderSearchModel searchModel) {
		searchModel.setOrderStatus(TicketOrderStatus.CANCELED);
		searchModel.setOrderBy("createTime");
		model.addAttribute("orderPages",
				ticketOrderService.searchTicketOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 查看选座票订单信息。
	 * 
	 * @param model
	 *            数据模型
	 * @param ticketOrder
	 *            选座票订单
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("ticketOrder-view")
	public void view(Model model, TicketOrder ticketOrder) {
		model.addAttribute("order", ticketOrder);
	}

	/**
	 * 同步订单。
	 * 
	 * @param model
	 *            数据模型
	 * @param ticketOrder
	 *            选座票订单
	 */
	@Auth("ORDER_MANAGE")
	@RequestMapping("ticketOrder-sync")
	public void sync(Model model, TicketOrder ticketOrder) {
		model.addAttribute("localOrder", ticketOrder);
		try {
			TicketOrder remoteOrder = ticketOrderService
					.getCinemaTicketOrder(ticketOrder);
			model.addAttribute("remoteOrder", remoteOrder);
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
		}
	}

	/**
	 * 标记失败。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回提示信息。
	 */
	@Auth("ORDER_MANAGE")
	@RequestMapping("ticketOrder-mark-failed")
	public ModelAndView markFailed(TicketOrder ticketOrder) {
		ticketOrderService.markTicketOrderFailed(ticketOrder);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("ticketOrder.mark.failed.success"));
	}

	/**
	 * 退票。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回提示信息。
	 */
	@Auth("ORDER_MANAGE")
	@RequestMapping("ticketOrder-revoke")
	public ModelAndView revoke(TicketOrder ticketOrder) {
		ticketOrderService.revokeTicketOrder(ticketOrder);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("ticketOrder.revoke.success"));
	}

	/**
	 * 标记退票。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回提示信息。
	 */
	@Auth("ORDER_MANAGE")
	@RequestMapping("ticketOrder-mark-revoked")
	public ModelAndView markRevoked(TicketOrder ticketOrder) {
		ticketOrderService.markTicketOrderRevoked(ticketOrder);
		return DialogResultUtils.closeAndReloadNavTab(messageSource
				.get("ticketOrder.mark.revoked.success"));
	}
}