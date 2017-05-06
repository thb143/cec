package cn.mopon.cec.site.actions.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.core.model.BenefitCardOrderSearchModel;
import cn.mopon.cec.core.service.BenefitCardOrderService;
import cn.mopon.cec.core.service.BenefitCardTypeService;
import cn.mopon.cec.core.service.ChannelService;
import coo.core.security.annotations.Auth;

/**
 * 权益卡订单。
 */
@Controller
@RequestMapping("/order")
public class BenefitCardOrderAction {
	@Resource
	private BenefitCardOrderService benefitCardOrderService;
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private ChannelService channelService;

	/**
	 * 权益卡开卡订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("benefitCardOpenOrder-list")
	public void listOpenOrder(Model model,
			BenefitCardOrderSearchModel searchModel) {
		searchModel.setOrderType("open");
		model.addAttribute("orderPages",
				benefitCardOrderService.searchOpenOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardTypes",
				benefitCardTypeService.getValidBenefitCardTypes());
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 权益卡续费订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("benefitCardRechargeOrder-list")
	public void listRechargeOrder(Model model,
			BenefitCardOrderSearchModel searchModel) {
		searchModel.setOrderType("recharge");
		model.addAttribute("orderPages",
				benefitCardOrderService.searchRechargeOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardTypes",
				benefitCardTypeService.getValidBenefitCardTypes());
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 权益卡消费订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("benefitCardConsumeOrder-list")
	public void listConsumeOrder(Model model,
			BenefitCardOrderSearchModel searchModel) {
		searchModel.setOrderType("consume");
		model.addAttribute("orderPages",
				benefitCardOrderService.searchConsumeOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardTypes",
				benefitCardTypeService.getValidBenefitCardTypes());
		model.addAttribute("channels", channelService.getAllChannels());
	}

	/**
	 * 权益卡卖品消费订单列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("ORDER_VIEW")
	@RequestMapping("benefitCardConsumeSnackOrder-list")
	public void listConsumeSnackOrder(Model model,
			BenefitCardOrderSearchModel searchModel) {
		searchModel.setOrderType("consume");
		model.addAttribute("orderPages",
				benefitCardOrderService.searchConsumeSnackOrder(searchModel));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardTypes",
				benefitCardTypeService.getValidBenefitCardTypes());
		model.addAttribute("channels", channelService.getAllChannels());
	}
}