package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.model.ChannelShowSearchModel;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.TicketOrderService;
import coo.base.model.Page;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.NavTabResultUtils;

/**
 * 渠道排期管理。
 */
@Controller
@RequestMapping("/product")
public class ChannelShowAction {
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private ChannelService channelService;
	@Resource
	private TicketOrderService ticketOrderService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看渠道排期管理主页面。
	 * 
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("channelShow-main")
	public void main(Model model) {
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 查看渠道排期列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("channelShow-list")
	public void list(Model model, ChannelShowSearchModel searchModel) {
		Page<ChannelShow> channelShowPage = channelShowService
				.searchChannelShow(searchModel);
		model.addAttribute(channelService.getChannel(searchModel.getChannelId()));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("channelShowPage", channelShowPage);
	}

	/**
	 * 查看渠道排期。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelShow
	 *            渠道排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("channelShow-view")
	public void view(Model model, ChannelShow channelShow) {
		model.addAttribute(channelShow);
	}

	/**
	 * 查看排期正常订单记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelShow
	 *            渠道排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("channelShow-normal-order-list")
	public void listNormalOrder(Model model, ChannelShow channelShow) {
		model.addAttribute("orders",
				ticketOrderService.searchNormalOrder(channelShow));
	}

	/**
	 * 查看排期退款订单记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param channelShow
	 *            渠道排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("channelShow-revoke-order-list")
	public void listRevokeOrder(Model model, ChannelShow channelShow) {
		model.addAttribute("orders",
				ticketOrderService.searchRevokeOrder(channelShow));
	}

	/**
	 * 上架渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("channelShow-enable")
	public ModelAndView enable(ChannelShow channelShow) {
		channelShowService.enableChannelShow(channelShow);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("channelShow.enable.success"),
				"channelShowListBox");
	}

	/**
	 * 批量上架渠道排期。
	 * 
	 * @param channelShowIds
	 *            渠道排期ID列表
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("channelShow-batch-enable")
	public ModelAndView batchEnable(String[] channelShowIds) {
		channelShowService.batchEnableChannelShows(channelShowIds);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("channelShow.batch.enable.success"),
				"channelShowListBox");
	}

	/**
	 * 下架渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("channelShow-disable")
	public ModelAndView disable(ChannelShow channelShow) {
		channelShowService.disableChannelShow(channelShow);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("channelShow.disable.success"),
				"channelShowListBox");
	}

	/**
	 * 批量下架渠道排期。
	 * 
	 * @param channelShowIds
	 *            渠道排期ID列表
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("channelShow-batch-disable")
	public ModelAndView batchDisable(String[] channelShowIds) {
		channelShowService.batchDisableChannelShows(channelShowIds);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("channelShow.batch.disable.success"),
				"channelShowListBox");
	}
}