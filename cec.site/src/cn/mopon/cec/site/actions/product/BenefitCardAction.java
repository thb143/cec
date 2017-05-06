package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.model.BenefitCardSearchModel;
import cn.mopon.cec.core.service.BenefitCardService;
import cn.mopon.cec.core.service.BenefitCardTypeService;
import cn.mopon.cec.core.service.ChannelService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 卡管理。
 */
@Controller
@RequestMapping("/product")
public class BenefitCardAction {
	@Resource
	private BenefitCardService benefitCardService;
	@Resource
	private ChannelService channelService;
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 卡管理列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            查询条件
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCard-list")
	public void list(Model model, BenefitCardSearchModel searchModel) {
		model.addAttribute("channels", channelService.getAllChannels());
		model.addAttribute("types",
				benefitCardTypeService.getValidBenefitCardTypes());
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardPage",
				benefitCardService.searchBenefitCard(searchModel));
	}

	/**
	 * 权益卡信息。
	 * 
	 * @param model
	 *            数据模型
	 * @param benefitCard
	 *            权益卡
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCard-view")
	public void view(Model model, BenefitCard benefitCard) {
		model.addAttribute(benefitCardService.getBenefitCard(benefitCard
				.getId()));
	}

	/**
	 * 冻结权益卡。
	 * 
	 * @param benefitCard
	 *            权益卡对象
	 * @return 操作结果。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCard-disable")
	public ModelAndView disable(BenefitCard benefitCard) {
		benefitCardService.disableBenefitCard(benefitCard);
		return DialogResultUtils.reload(messageSource
				.get("benefitCard.disable.success"));
	}

	/**
	 * 解冻权益卡。
	 * 
	 * @param benefitCard
	 *            权益卡对象
	 * @return 操作结果。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCard-enable")
	public ModelAndView enable(BenefitCard benefitCard) {
		benefitCardService.enableBenefitCard(benefitCard);
		return DialogResultUtils.reload(messageSource
				.get("benefitCard.enable.success"));
	}

	/**
	 * 权益卡延期。
	 * 
	 * @param benefitCard
	 *            权益卡对象
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCard-delay")
	public ModelAndView delay(BenefitCard benefitCard) {
		benefitCardService.delayBenefitCard(benefitCard);
		return DialogResultUtils.reload(messageSource
				.get("benefitCard.delay.success"));
	}
}
