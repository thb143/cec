package cn.mopon.cec.site.actions.price;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.enums.SpecialPolicyType;
import cn.mopon.cec.core.model.CinemaModel;
import cn.mopon.cec.core.model.ProviceCinemaListModel;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.FilmService;
import cn.mopon.cec.core.service.SpecialPolicyService;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.core.util.IEnumUtils;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 特殊定价策略管理。
 */
@Controller
@RequestMapping("/price")
public class SpecialPolicyAction {
	@Resource
	private MessageSource messageSource;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private ChannelService channelService;
	@Resource
	private FilmService filmService;

	/**
	 * 查看特殊定价策略列表。
	 * 
	 * @param selectedSpecialPolicyId
	 *            选中的特殊定价策略ID
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("specialPolicy-list")
	public void list(String selectedSpecialPolicyId, Model model,
			SearchModel searchModel) {
		model.addAttribute("selectedSpecialPolicyId", selectedSpecialPolicyId);
		model.addAttribute("specialPolicys",
				specialPolicyService.getYearSpecialPolicyListModel(searchModel));
	}

	/**
	 * 查看特殊定价策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            特殊定价策略ID
	 */
	@Auth("POLICY_VIEW")
	@RequestMapping("specialPolicy-view")
	public void view(Model model, String policyId) {
		SpecialPolicy specialPolicy = specialPolicyService
				.getSpecialPolicy(policyId);
		model.addAttribute("specialPolicy", specialPolicy);
	}

	/**
	 * 新增特殊定价策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param type
	 *            类型
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-add")
	public void addSpecialPolicy(Model model, String type) {
		SpecialPolicy specialPolicy = new SpecialPolicy();
		specialPolicy.setType(IEnumUtils.getIEnumByValue(
				SpecialPolicyType.class, type));
		model.addAttribute("specialPolicy", specialPolicy);
	}

	/**
	 * 保存特殊定价策略。
	 * 
	 * @param specialPolicy
	 *            特殊定价策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-save")
	public ModelAndView saveSpecialPolicy(SpecialPolicy specialPolicy) {
		specialPolicyService.createSpecialPolicy(specialPolicy);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.add.success"),
				"/price/specialPolicy-list?selectedSpecialPolicyId="
						+ specialPolicy.getId());
	}

	/**
	 * 编辑特殊定价策略。
	 * 
	 * @param model
	 *            数据模型
	 * @param policyId
	 *            特殊定价策略ID
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialPolicy-edit")
	public void editSpecialPolicy(Model model, String policyId) {
		SpecialPolicy specialPolicy = specialPolicyService
				.getSpecialPolicy(policyId);
		model.addAttribute("hallList", specialPolicy.getHallModels());
		model.addAttribute("specialPolicy", specialPolicy);
	}

	/**
	 * 更新特殊定价策略。
	 * 
	 * @param specialPolicy
	 *            特殊定价策略
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialPolicy-update")
	public ModelAndView updateSpecialPolicy(SpecialPolicy specialPolicy) {
		specialPolicyService.updateSpecialPolicy(specialPolicy);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.edit.success"),
				"/price/specialPolicy-list?selectedSpecialPolicyId="
						+ specialPolicy.getId());
	}

	/**
	 * 删除特殊定价策略。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-delete")
	public ModelAndView deleteSpecialPolicy(String policyId) {
		specialPolicyService.deleteSpecialPolicy(specialPolicyService
				.getSpecialPolicy(policyId));
		return NavTabResultUtils.reload(messageSource
				.get("policy.delete.success"));
	}

	/**
	 * 上移特殊定价策略。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-up")
	@ResponseBody
	public String up(String policyId) {
		specialPolicyService.upSpecialPolicy(specialPolicyService
				.getSpecialPolicy(policyId));
		return "";
	}

	/**
	 * 下移特殊定价策略。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-down")
	@ResponseBody
	public String down(String policyId) {
		specialPolicyService.downSpecialPolicy(specialPolicyService
				.getSpecialPolicy(policyId));
		return "";
	}

	/**
	 * 启用特殊定价策略。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialPolicy-enable")
	public ModelAndView enable(String policyId) {
		SpecialPolicy policy = specialPolicyService.getSpecialPolicy(policyId);
		List<Show> shows = specialPolicyService.enableSpecialPolicy(policy);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils
				.forward(messageSource.get("policy.enable.success"),
						"/price/specialPolicy-list?selectedSpecialPolicyId="
								+ policyId);
	}

	/**
	 * 停用特殊定价策略。
	 * 
	 * @param policyId
	 *            特殊定价策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_SWITCH")
	@RequestMapping("specialPolicy-disable")
	public ModelAndView disable(String policyId) {
		SpecialPolicy policy = specialPolicyService.getSpecialPolicy(policyId);
		List<Show> shows = specialPolicyService.disableSpecialPolicy(policy);
		channelShowService.batchGenChannelShows(shows);
		return NavTabResultUtils
				.forward(messageSource.get("policy.disable.success"),
						"/price/specialPolicy-list?selectedSpecialPolicyId="
								+ policyId);
	}

	/**
	 * 选择特殊定价策略类型。
	 * 
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-type-select")
	public void selectSpecialPolicyType() {
		// nothing to do
	}

	/**
	 * 复制新增。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-copy-select")
	public void copySelect(Model model, SearchModel searchModel) {
		model.addAttribute("specialPolicyPage",
				specialPolicyService.searchSpecialPolicys(searchModel));
	}

	/**
	 * 复制特殊定价策略。
	 * 
	 * @param policyId
	 *            被复制特殊定价策略ID
	 * @return 返回提示信息。
	 */
	@Auth("POLICY_MANAGE")
	@RequestMapping("specialPolicy-copy")
	public ModelAndView copy(String policyId) {
		SpecialPolicy specialPolicy = specialPolicyService
				.copySpecialPolicy(specialPolicyService
						.getSpecialPolicy(policyId));
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("policy.copy.success"),
				"/price/specialPolicy-list?selectedSpecialPolicyId="
						+ specialPolicy.getId());
	}

	/**
	 * 选择影院影厅。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaModel
	 *            查询条件
	 * @param policyId
	 *            特殊定价策略ID
	 */
	@RequestMapping("specialPolicy-hall-select")
	public void hallSelect(Model model, CinemaModel cinemaModel, String policyId) {
		List<Cinema> cinemas = new ArrayList<>();
		if (StringUtils.isEmpty(policyId)) {
			addEnabledCinemaHalls(cinemaModel, cinemas);
		} else {
			addPolicyCinemas(policyId, cinemas);
		}
		ProviceCinemaListModel proviceCinemaListModel = new ProviceCinemaListModel();
		for (Cinema cinema : cinemas) {
			proviceCinemaListModel.addProvinceCinemaModel(cinema);
		}
		cinemaModel.setSpecialPolicyId(policyId);
		model.addAttribute("cinemaModel", cinemaModel);
		model.addAttribute("specialPolicy", new SpecialPolicy());
		model.addAttribute("proviceCinemas", proviceCinemaListModel);
	}

	/**
	 * 选择渠道。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinemaIds
	 *            影院ID
	 * @param policyId
	 *            特殊定价策略ID
	 */
	@RequestMapping("specialPolicy-channel-select")
	public void channelSelect(Model model, String policyId, String cinemaIds) {
		List<Channel> openChannels = channelService
				.getChannelsByCinema(cinemaIds);
		if (!StringUtils.isEmpty(policyId)) {
			List<Channel> orgiChannels = specialPolicyService.getSpecialPolicy(
					policyId).getChannels();
			for (Channel orgiChannel : orgiChannels) {
				boolean exist = false;
				for (Channel openChannel : openChannels) {
					if (openChannel.getId().equals(orgiChannel.getId())) {
						exist = true;
						break;
					}
				}
				// 追加已选择过的渠道
				if (!exist) {
					openChannels.add(orgiChannel);
				}
			}
			model.addAttribute("channelList", openChannels);
		} else {
			model.addAttribute("channelList", openChannels);
		}
	}

	/**
	 * 选择影片。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@RequestMapping("specialPolicy-film-select")
	public void filmSelect(Model model, SearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("filmList", filmService.findFilms(searchModel));
	}

	/**
	 * 根据特殊定价策略类型初始化。
	 * 
	 * @return 返回对应的特殊定价策略。
	 */
	@ModelAttribute("specialPolicy")
	public SpecialPolicy getSpecialPolicy() {
		return new SpecialPolicy();
	}
	
	/**
	 * 添加策略的影院。
	 * 
	 * @param policyId
	 *            策略id
	 * @param cinemas
	 *            影院列表
	 */
	private void addPolicyCinemas(String policyId, List<Cinema> cinemas) {
		SpecialPolicy specialPolicy = specialPolicyService
				.getSpecialPolicy(policyId);
		Map<Cinema, List<Hall>> cinemaMap = specialPolicy.getAllCinemas();
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
}