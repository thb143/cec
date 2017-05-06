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
import cn.mopon.cec.core.entity.BenefitCardTypeRule;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.model.CinemaModel;
import cn.mopon.cec.core.model.ProviceCinemaListModel;
import cn.mopon.cec.core.service.BenefitCardTypeRuleService;
import cn.mopon.cec.core.service.BenefitCardTypeService;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 权益卡卡类规则管理类
 * 
 */
@Controller
@RequestMapping("product")
public class BenefitCardTypeRuleAction {
	@Resource
	private BenefitCardTypeService benefitCardTypeService;
	@Resource
	private BenefitCardTypeRuleService benefitCardTypeRuleService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private CinemaService cinemaService;

	/**
	 * 加载权益卡卡类规则添加页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param benefitCardTypeId
	 *            权益卡卡类ID
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-rule-add")
	public void add(Model model, String benefitCardTypeId) {
		BenefitCardType benefitCardType = benefitCardTypeService
				.getBenefitCardType(benefitCardTypeId);
		model.addAttribute("benefitCardType", benefitCardType);
		BenefitCardTypeRule benefitCardTypeRule = new BenefitCardTypeRule();
		benefitCardTypeRule.setType(benefitCardType);
		model.addAttribute("benefitCardTypeRule", benefitCardTypeRule);
	}

	/**
	 * 加载影院影厅设置页面
	 * 
	 * @param model
	 *            数据模型对象
	 * @param ruleId
	 *            权益卡卡类规则ID
	 * @param cinemaModel
	 *            影院数据模型
	 */
	@RequestMapping("benefitCardTypeRule-cinema-set")
	public void cinemaSet(Model model, String ruleId, CinemaModel cinemaModel) {
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
		model.addAttribute("benefitCardTypeRule", new BenefitCardTypeRule());
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
		BenefitCardTypeRule rule = benefitCardTypeRuleService
				.getBenefitCardTypeRule(ruleId);
		Map<Cinema, List<Hall>> cinemaMap = rule.getAllCinemas();
		cinemas.clear();
		for (Entry<Cinema, List<Hall>> entry : cinemaMap.entrySet()) {
			Cinema cinema = entry.getKey();
			cinema.setHalls(entry.getValue());
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
	private void addEnabledCinemaHalls(CinemaModel cinemaModel,
			List<Cinema> cinemas) {
		List<Cinema> cinemaList = cinemaService
				.searchEnabledCinemas(cinemaModel);
		cinemas.addAll(cinemaList);
		// 获取启用影院下已启用的影厅。
		for (Cinema cinema : cinemaList) {
			if (CollectionUtils.isEmpty(cinema.getEnableHalls())) {
				cinemas.remove(cinema);
			} else {
				cinema.setHalls(cinema.getEnableHalls());
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
	@RequestMapping("benefitCardType-rule-save")
	public ModelAndView save(BenefitCardTypeRule benefitCardTypeRule) {
		benefitCardTypeRuleService
				.createBenefitCardTypeRule(benefitCardTypeRule);
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
	@RequestMapping("benefitCardType-rule-edit")
	public void edit(Model model, BenefitCardTypeRule benefitCardTypeRule) {
		model.addAttribute("hallList", benefitCardTypeRule.getCinemas());
		model.addAttribute("benefitCardTypeRule", benefitCardTypeRule);
	}

	/**
	 * 保存卡类规则修改结果
	 * 
	 * @param benefitCardTypeRule
	 *            卡类规则对象
	 * @return 数据模型对象。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("benefitCardType-rule-update")
	public ModelAndView update(BenefitCardTypeRule benefitCardTypeRule) {
		benefitCardTypeRuleService.update(benefitCardTypeRule);
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
	@RequestMapping("benefitCardTypeRule-enable")
	public ModelAndView enabled(BenefitCardTypeRule benefitCardTypeRule) {
		List<Show> shows = benefitCardTypeRuleService
				.enabled(benefitCardTypeRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("benefitCardTypeRule.enable.success"),
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
	@RequestMapping("benefitCardTypeRule-disabled")
	public ModelAndView disabled(BenefitCardTypeRule benefitCardTypeRule) {
		List<Show> shows = benefitCardTypeRuleService
				.disabled(benefitCardTypeRule);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("benefitCardTypeRule.disabled.success"),
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
	@RequestMapping("benefitCardTypeRule-delete")
	public ModelAndView delete(BenefitCardTypeRule benefitCardTypeRule) {
		benefitCardTypeRuleService.delete(benefitCardTypeRule);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("benefitCardTypeRule.delete.success"),
				"benefitCardTypeRuleBox");
	}
}
