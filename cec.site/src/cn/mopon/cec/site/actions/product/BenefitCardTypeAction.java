package cn.mopon.cec.site.actions.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.model.BenefitCardTypeSearchModel;
import cn.mopon.cec.core.service.BenefitCardTypeService;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.ChannelShowService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 权益卡卡类管理。
 */
@Controller
@RequestMapping("/product")
public class BenefitCardTypeAction {
	@Resource
	private ChannelService channelService;
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 增加卡类。
	 * 
	 * @param model
	 *            数据模型对象
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-add")
	public void add(Model model) {
		model.addAttribute("benefitCardType", new BenefitCardType());
		model.addAttribute("channelList", channelService.getAllChannels());
	}

	/**
	 * 保存新增权益卡类
	 * 
	 * @param benefitCardType
	 *            权益卡卡类
	 * @return 模型数据对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-save")
	public ModelAndView save(BenefitCardType benefitCardType) {
		benefitCardTypeService.createBenefitCardType(benefitCardType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("benefitCardType.add.success"),
				"/product/benefitCardType-main");
	}

	/**
	 * 加载权益卡卡类及规则页面
	 * 
	 * @param benefitCardTypeId
	 *            卡类ID
	 * @param model
	 *            数据模型对象
	 * @param searchModel
	 *            权益卡类查询对象
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardType-main")
	public void list(String benefitCardTypeId, Model model,
			BenefitCardTypeSearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("benefitCardTypeId", benefitCardTypeId);
		model.addAttribute("benefitCardTypes",
				benefitCardTypeService.searchBenefitCardType(searchModel));
	}

	/**
	 * 加载权益卡类编辑页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardType
	 *            权益卡卡类对象
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-edit")
	public void edit(Model model, BenefitCardType benefitCardType) {
		model.addAttribute("benefitCardType", benefitCardTypeService
				.getBenefitCardType(benefitCardType.getId()));
		model.addAttribute("channelList", channelService.getAllChannels());
	}

	/**
	 * 保存修改后的权益卡
	 * 
	 * @param benefitCardType
	 *            权益卡对象
	 * @return 模型数据对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-update")
	public ModelAndView update(BenefitCardType benefitCardType) {
		benefitCardTypeService.updateBenefitCardType(benefitCardType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("benefitCardType.update.success"),
				"/product/benefitCardType-main?benefitCardTypeId="
						+ benefitCardType.getId());
	}

	/**
	 * 启用权益卡类
	 * 
	 * @param id
	 *            权益卡类ID
	 * @return 模型数据对象。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("benefitCardType-enable")
	public ModelAndView enable(String id) {
		List<Show> shows = benefitCardTypeService.enable(benefitCardTypeService
				.getBenefitCardType(id));
		channelShowService.batchGenChannelShows(shows);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("benefitCardType.enable.success"),
				"/product/benefitCardType-main?benefitCardTypeId=" + id);
	}

	/**
	 * 停用权益卡类
	 * 
	 * @param id
	 *            权益卡类ID
	 * @return 模型数据对象。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("benefitCardType-disabled")
	public ModelAndView disabled(String id) {
		benefitCardTypeService.disabled(benefitCardTypeService
				.getBenefitCardType(id));
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("benefitCardType.disabled.success"),
				"/product/benefitCardType-main?benefitCardTypeId=" + id);
	}

	/**
	 * 停用权益卡类
	 * 
	 * @param id
	 *            权益卡类ID
	 * @return 模型数据对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-delete")
	public ModelAndView delete(String id) {
		benefitCardTypeService.delete(benefitCardTypeService
				.getBenefitCardType(id));
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("benefitCardType.delete.success"),
				"/product/benefitCardType-main");
	}

	/**
	 * 加载卡类规则页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardType
	 *            权益卡类对象
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardType-view")
	public void view(Model model, BenefitCardType benefitCardType) {
		benefitCardType = benefitCardTypeService
				.getBenefitCardType(benefitCardType.getId());
		model.addAttribute("benefitCardType", benefitCardType);
	}

	/**
	 * 加载票务卡类规则页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardType
	 *            权益卡类对象
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardType-rule-view")
	public void viewRule(Model model, BenefitCardType benefitCardType) {
		benefitCardType = benefitCardTypeService
				.getBenefitCardType(benefitCardType.getId());
		model.addAttribute("benefitCardType", benefitCardType);
	}

	/**
	 * 加载卖品卡类规则页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardType
	 *            权益卡类对象
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("benefitCardType-snackRule-view")
	public void viewSnackRule(Model model, BenefitCardType benefitCardType) {
		benefitCardType = benefitCardTypeService
				.getBenefitCardType(benefitCardType.getId());
		model.addAttribute("benefitCardType", benefitCardType);
	}
}
