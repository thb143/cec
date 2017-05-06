package cn.mopon.cec.site.actions.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.model.ShowSearchModel;
import cn.mopon.cec.core.service.BnLogger;
import cn.mopon.cec.core.service.ChannelShowService;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.ShowService;
import cn.mopon.cec.core.service.TicketOrderService;
import coo.base.model.Page;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 排期管理。
 */
@Controller
@RequestMapping("/product")
public class ShowAction {
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private TicketOrderService ticketOrderService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private BnLogger bnLogger;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看排期管理主页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-main")
	public void main(Model model, SearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaPage",
				cinemaService.searchCinema(searchModel));
	}

	/**
	 * 查看排期列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-list")
	public void list(Model model, ShowSearchModel searchModel) {
		Page<Show> showPage = showService.searchShow(searchModel);
		model.addAttribute(cinemaService.getCinema(searchModel.getCinemaId()));
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("showPage", showPage);
	}

	/**
	 * 同步排期。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("show-sync")
	public ModelAndView sync(String cinemaId) {
		showService.syncShows(cinemaService.getCinema(cinemaId));
		return NavTabResultUtils.reloadDiv(
				messageSource.get("show.sync.success"), "showListBox");
	}

	/**
	 * 查看排期。
	 * 
	 * @param model
	 *            数据模型
	 * @param show
	 *            排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-view")
	public void view(Model model, Show show) {
		model.addAttribute(show);
	}

	/**
	 * 查看排期更新日志。
	 * 
	 * @param model
	 *            数据模型
	 * @param show
	 *            排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-log-list")
	public void listLogs(Model model, Show show) {
		model.addAttribute("logs", bnLogger.searchEntityLog(show.getId()));
	}

	/**
	 * 查看排期正常订单记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param show
	 *            排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-normal-order-list")
	public void listNormalOrder(Model model, Show show) {
		model.addAttribute("orders", ticketOrderService.searchNormalOrder(show));
	}

	/**
	 * 查看排期退款订单记录。
	 * 
	 * @param model
	 *            数据模型
	 * @param show
	 *            排期
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-revoke-order-list")
	public void listRevokeOrder(Model model, Show show) {
		model.addAttribute("orders", ticketOrderService.searchRevokeOrder(show));
	}

	/**
	 * 查看渠道排期列表。
	 * 
	 * @param show
	 *            排期
	 * @param model
	 *            数据模型
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("show-channel-list")
	public void listChannelShows(Model model, Show show) {
		model.addAttribute("channelShows", show.getChannelShows());
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
	@RequestMapping("show-channel-enable")
	public ModelAndView enable(ChannelShow channelShow) {
		channelShowService.enableChannelShow(channelShow);
		return DialogResultUtils.reload(messageSource
				.get("channelShow.enable.success"));
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
	@RequestMapping("show-channel-batch-enable")
	public ModelAndView batchEnable(String[] channelShowIds) {
		channelShowService.batchEnableChannelShows(channelShowIds);
		return DialogResultUtils.reload(messageSource
				.get("channelShow.batch.enable.success"));
	}

	/**
	 * 下架渠道排期。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("show-channel-disable")
	public ModelAndView disable(ChannelShow channelShow) {
		channelShowService.disableChannelShow(channelShow);
		return DialogResultUtils.reload(messageSource
				.get("channelShow.disable.success"));
	}

	/**
	 * 批量下架渠道排期。
	 * 
	 * @param channelShowIds
	 *            渠道排期ID列表
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("show-channel-batch-disable")
	public ModelAndView batchDisable(String[] channelShowIds) {
		channelShowService.batchDisableChannelShows(channelShowIds);
		return DialogResultUtils.reload(messageSource
				.get("channelShow.batch.disable.success"));
	}

	/**
	 * 重新生成渠道排期。
	 * 
	 * @param show
	 *            排期
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("show-gen")
	public ModelAndView gen(Show show) {
		Integer count = channelShowService.genChannelShows(show);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("show.gen.success", count), "showListBox");
	}

	/**
	 * 批量重新生成渠道排期。
	 * 
	 * @param showIds
	 *            排期ID列表
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("show-batch-gen")
	public ModelAndView batchGen(String[] showIds) {
		Integer count = channelShowService.batchGenChannelShows(showIds);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("show.gen.success", count), "showListBox");
	}

	/**
	 * 查看影院结算规则匹配的排期列表。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("cinemaRule-show-list")
	public ModelAndView listMatchedShows(CinemaRule cinemaRule) {
		ModelAndView mv = new ModelAndView("/product/show-matched-list");
		mv.addObject("shows", showService.getMatchedShows(cinemaRule));
		return mv;
	}

	/**
	 * 查看影院结算策略匹配的排期列表。
	 * 
	 * @param cinemaPolicy
	 *            影院结算策略
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("cinemaPolicy-show-list")
	public ModelAndView listMatchedShows(CinemaPolicy cinemaPolicy) {
		ModelAndView mv = new ModelAndView("/product/show-matched-list");
		mv.addObject("shows", showService.getMatchedShows(cinemaPolicy));
		return mv;
	}

	/**
	 * 查看渠道结算规则匹配的排期列表。
	 * 
	 * @param channelRule
	 *            渠道结算规则
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("channelRule-show-list")
	public ModelAndView listMatchedShows(ChannelRule channelRule) {
		ModelAndView mv = new ModelAndView("/product/show-matched-list");
		mv.addObject("shows", showService.getMatchedShows(channelRule));
		return mv;
	}

	/**
	 * 查看渠道结算规则分组匹配的排期列表。
	 * 
	 * @param channelRuleGroup
	 *            渠道结算规则分组
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("channelRuleGroup-show-list")
	public ModelAndView listMatchedShows(ChannelRuleGroup channelRuleGroup) {
		ModelAndView mv = new ModelAndView("/product/show-matched-list");
		mv.addObject("shows", showService.getMatchedShows(channelRuleGroup));
		return mv;
	}

	/**
	 * 查看特殊定价策略匹配的排期列表。
	 * 
	 * @param specialPolicy
	 *            特殊定价策略
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("specialPolicy-show-list")
	public ModelAndView listMatchedShows(SpecialPolicy specialPolicy) {
		ModelAndView mv = new ModelAndView("/product/show-matched-list");
		mv.addObject("shows", showService.getMatchedShows(specialPolicy));
		return mv;
	}

	/**
	 * 查看权益卡规则匹配的排期列表。
	 * 
	 * @param benefitCardTypeRule
	 * @return 返回提示信息。
	 */
	@RequestMapping("benefitCardType-show-list")
	public ModelAndView listMatchedShows(BenefitCardType benefitCardType) {
		ModelAndView mv = new ModelAndView("/product/show-matched-list");
		mv.addObject("shows", showService.getMatchedShows(benefitCardType));
		return mv;
	}

	/**
	 * 批量重新生成渠道排期。
	 * 
	 * @param showIds
	 *            排期ID列表
	 * @return 返回提示信息。
	 * 
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("show-match-gen")
	public ModelAndView matchGen(String[] showIds) {
		Integer count = channelShowService.batchGenChannelShows(showIds);
		return DialogResultUtils.close(messageSource.get("show.gen.success",
				count));
	}
}