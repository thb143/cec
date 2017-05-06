package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import coo.core.security.annotations.Auth;
import cn.mopon.cec.core.model.BenefitCardUserSearchModel;
import cn.mopon.cec.core.service.BenefitCardService;
import cn.mopon.cec.core.service.BenefitCardUserService;
import cn.mopon.cec.core.service.ChannelService;

/**
 * 权益卡用户管理。
 */
@Controller
@RequestMapping("/product")
public class BenefitCardUserAction {

	@Resource
	private BenefitCardUserService benefitCardUserService;
	@Resource
	private BenefitCardService benefitCardService;
	@Resource
	private ChannelService channelService;

	/**
	 * 权益卡用户列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            查询条件
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardUser-list")
	public void list(Model model, BenefitCardUserSearchModel searchModel) {
		model.addAttribute("channels", channelService.getAllChannels());
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardUserPage",
				benefitCardUserService.searchBenefitCardUser(searchModel));
	}

	/**
	 * 用户卡列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param userId
	 *            权益卡用户ID
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardUser-cards")
	public void cards(Model model, String userId) {
		model.addAttribute("cards",
				benefitCardService.searchBenefitCardByUser(userId));
	}
}
