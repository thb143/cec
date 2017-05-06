package cn.mopon.cec.site.actions.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.model.CinemaSnackModel;
import cn.mopon.cec.core.model.ProviceCinemaListModel;
import cn.mopon.cec.core.service.BenefitCardTypeService;
import cn.mopon.cec.core.service.BenefitCardTypeSnackRuleService;
import cn.mopon.cec.core.service.CinemaService;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 权益卡卡类卖品规则管理类
 * 
 */
@Controller
@RequestMapping("product")
public class BenefitCardTypeSnackRuleAction {
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private BenefitCardTypeSnackRuleService benefitCardTypeSnackRuleService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 加载权益卡卡类规则添加页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardTypeId
	 *            权益卡卡类ID
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-snackRule-add")
	public void add(Model model, String benefitCardTypeId) {
		BenefitCardType benefitCardType = benefitCardTypeService
				.getBenefitCardType(benefitCardTypeId);
		model.addAttribute("benefitCardType", benefitCardType);
		BenefitCardTypeSnackRule benefitCardTypeSnackRule = new BenefitCardTypeSnackRule();
		benefitCardTypeSnackRule.setType(benefitCardType);
		model.addAttribute("snackRule", benefitCardTypeSnackRule);
	}

	/**
	 * 加载影院卖品设置页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param ruleId
	 *            权益卡卡类规则ID
	 * @param cinemaModel
	 *            影院数据模型
	 */
	@RequestMapping("benefitCardTypeSnackRule-cinema-set")
	public void cinemaSet(Model model, String ruleId,
			CinemaSnackModel cinemaModel) {
		List<Cinema> cinemas = new ArrayList<>();
		if (StringUtils.isEmpty(ruleId)) {
			addEnabledCinemaHalls(cinemaModel, cinemas);
		} else {
			addRuleCinemas(ruleId, cinemas);
		}
		ProviceCinemaListModel proviceCinemaListModel = new ProviceCinemaListModel();
		for (Cinema cinema : cinemas) {
			proviceCinemaListModel.addProvinceCinemaModel(cinema);
		}
		cinemaModel.setRuleId(ruleId);
		model.addAttribute("cinemaModel", cinemaModel);
		model.addAttribute("snackRule", new BenefitCardTypeSnackRule());
		model.addAttribute("proviceCinemas", proviceCinemaListModel);
	}

	/**
	 * 添加策略的影院。
	 * 
	 * @param ruleId
	 *            规则ID
	 * @param cinemas
	 *            影院列表
	 */
	private void addRuleCinemas(String ruleId, List<Cinema> cinemas) {
		BenefitCardTypeSnackRule rule = benefitCardTypeSnackRuleService
				.getBenefitCardTypeSnackRule(ruleId);
		Map<Cinema, List<Snack>> cinemaMap = rule.getAllCinemas();
		cinemas.clear();
		for (Entry<Cinema, List<Snack>> entry : cinemaMap.entrySet()) {
			Cinema cinema = entry.getKey();
			cinema.setSnacks(entry.getValue());
			cinemas.add(cinema);
		}
	}

	/**
	 * 添加启用影院影厅。
	 * 
	 * @param cinemaModel
	 *            影院模型
	 * @param cinemas
	 *            影院列表
	 */
	private void addEnabledCinemaHalls(CinemaSnackModel cinemaModel,
			List<Cinema> cinemas) {
		List<Cinema> cinemaList = cinemaService
				.searchEnabledCinemas(cinemaModel);
		cinemas.addAll(cinemaList);
		// 获取启用影院下已上架的卖品。
		for (Cinema cinema : cinemaList) {
			if (CollectionUtils.isEmpty(cinema.getSnacks())) {
				cinemas.remove(cinema);
			} else {
				cinema.setSnacks(cinema.getOnSnacks());
			}
		}
	}

	/**
	 * 保存权益卡类规则
	 * 
	 * @param benefitCardTypeRule
	 *            权益卡卡类规则对象
	 * @return 数据模型对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-snackRule-save")
	public ModelAndView save(BenefitCardTypeSnackRule benefitCardTypeSnackRule) {
		benefitCardTypeSnackRuleService
				.createBenefitCardTypeSnackRule(benefitCardTypeSnackRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("benefitCardTypeRule.add.success"),
				"benefitCardTypeRuleBox");
	}

	/**
	 * 加载权益卡卡类规则编辑页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardTypeRule
	 *            权益卡卡类规则对象
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-snackRule-edit")
	public void edit(Model model,
			BenefitCardTypeSnackRule benefitCardTypeSnackRule) {
		model.addAttribute("snackList", benefitCardTypeSnackRule.getCinemas());
		model.addAttribute("benefitCardTypeSnackRule", benefitCardTypeSnackRule);
	}

	/**
	 * 保存卡类规则修改结果
	 * 
	 * @param benefitCardTypeRule
	 *            卡类规则对象
	 * @return 数据模型对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-snackRule-update")
	public ModelAndView update(BenefitCardTypeSnackRule benefitCardTypeSnackRule) {
		benefitCardTypeSnackRuleService.update(benefitCardTypeSnackRule);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("benefitCardTypeRule.update.success"),
				"benefitCardTypeRuleBox");
	}

	/**
	 * 启用卡类规则
	 * 
	 * @param benefitCardTypeRule
	 *            卡类规则对象
	 * @return 数据模型对象。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("benefitCardTypeSnackRule-enable")
	public ModelAndView enabled(
			BenefitCardTypeSnackRule benefitCardTypeSnackRule) {
		benefitCardTypeSnackRuleService.enabled(benefitCardTypeSnackRule);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("benefitCardTypeSnackRule.enable.success"),
				"benefitCardTypeRuleBox");
	}

	/**
	 * 停用卡类规则
	 * 
	 * @param benefitCardTypeRule
	 *            卡类规则对象
	 * @return 数据模型对象。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("benefitCardTypeSnackRule-disabled")
	public ModelAndView disabled(
			BenefitCardTypeSnackRule benefitCardTypeSnackRule) {
		benefitCardTypeSnackRuleService.disabled(benefitCardTypeSnackRule);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("benefitCardTypeSnackRule.disabled.success"),
				"benefitCardTypeRuleBox");
	}

	/**
	 * 删除卡类规则
	 * 
	 * @param benefitCardTypeRule
	 *            卡类规则对象
	 * @return 数据模型对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardTypeSnackRule-delete")
	public ModelAndView delete(BenefitCardTypeSnackRule benefitCardTypeSnackRule) {
		benefitCardTypeSnackRuleService.delete(benefitCardTypeSnackRule);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("benefitCardTypeRule.delete.success"),
				"benefitCardTypeRuleBox");
	}
}