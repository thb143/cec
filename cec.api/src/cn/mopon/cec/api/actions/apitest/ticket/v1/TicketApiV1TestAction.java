package cn.mopon.cec.api.actions.apitest.ticket.v1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.api.ticket.v1.BenefitCardQuery;
import cn.mopon.cec.api.ticket.v1.BenefitCardTypesQuery;
import cn.mopon.cec.api.ticket.v1.ChangeMobileQuery;
import cn.mopon.cec.api.ticket.v1.ChangeVoucherQuery;
import cn.mopon.cec.api.ticket.v1.ChannelsQuery;
import cn.mopon.cec.api.ticket.v1.CinemaQuery;
import cn.mopon.cec.api.ticket.v1.CinemasQuery;
import cn.mopon.cec.api.ticket.v1.ConfirmPrintQuery;
import cn.mopon.cec.api.ticket.v1.FilmQuery;
import cn.mopon.cec.api.ticket.v1.FilmsQuery;
import cn.mopon.cec.api.ticket.v1.HallsQuery;
import cn.mopon.cec.api.ticket.v1.LockSeatQuery;
import cn.mopon.cec.api.ticket.v1.MarkSnackRevokedQuery;
import cn.mopon.cec.api.ticket.v1.MarkTicketRevokedQuery;
import cn.mopon.cec.api.ticket.v1.OpenBenefitCardOrderQuery;
import cn.mopon.cec.api.ticket.v1.OpenBenefitCardQuery;
import cn.mopon.cec.api.ticket.v1.OrderQuery;
import cn.mopon.cec.api.ticket.v1.PrintByVerifyCodeQuery;
import cn.mopon.cec.api.ticket.v1.PrintByVoucherQuery;
import cn.mopon.cec.api.ticket.v1.RechargeBenefitCardOrderQuery;
import cn.mopon.cec.api.ticket.v1.RechargeBenefitCardQuery;
import cn.mopon.cec.api.ticket.v1.ReleaseSeatQuery;
import cn.mopon.cec.api.ticket.v1.ResetVoucherQuery;
import cn.mopon.cec.api.ticket.v1.RevokeSnackQuery;
import cn.mopon.cec.api.ticket.v1.RevokeTicketQuery;
import cn.mopon.cec.api.ticket.v1.SeatsQuery;
import cn.mopon.cec.api.ticket.v1.ShowQuery;
import cn.mopon.cec.api.ticket.v1.ShowSeatsQuery;
import cn.mopon.cec.api.ticket.v1.ShowsQuery;
import cn.mopon.cec.api.ticket.v1.SnackOrderQuery;
import cn.mopon.cec.api.ticket.v1.SnacksQuery;
import cn.mopon.cec.api.ticket.v1.SubmitOrderQuery;
import cn.mopon.cec.api.ticket.v1.SubmitSnackQuery;
import coo.core.security.annotations.Auth;
import coo.core.security.permission.AdminPermission;

/**
 * 标准接口测试。
 */
@Controller
@RequestMapping("/apitest/ticket/v1")
@Auth(AdminPermission.CODE)
public class TicketApiV1TestAction {
	/**
	 * 查看菜单。
	 */
	@RequestMapping("menu")
	public void menu() {
		// nothing to do
	}

	/**
	 * 查询影院列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryCinemas")
	public void queryCinemas(Model model) {
		model.addAttribute("query", new CinemasQuery());
	}

	/**
	 * 查询影院。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryCinema")
	public void queryCinema(Model model) {
		model.addAttribute("query", new CinemaQuery());
	}

	/**
	 * 查询影厅列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryHalls")
	public void queryHalls(Model model) {
		model.addAttribute("query", new HallsQuery());
	}

	/**
	 * 查询影片列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryFilms")
	public void queryFilms(Model model) {
		model.addAttribute("query", new FilmsQuery());
	}

	/**
	 * 查询影片。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryFilm")
	public void queryFilm(Model model) {
		model.addAttribute("query", new FilmQuery());
	}

	/**
	 * 查询排期列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryShows")
	public void queryShows(Model model) {
		model.addAttribute("query", new ShowsQuery());
	}

	/**
	 * 查询替代排期。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryReplaceShow")
	public void queryReplaceShow(Model model) {
		model.addAttribute("query", new ShowQuery());
	}

	/**
	 * 查询影厅座位列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("querySeats")
	public void querySeats(Model model) {
		model.addAttribute("query", new SeatsQuery());
	}

	/**
	 * 查询排期座位列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryShowSeats")
	public void queryShowSeats(Model model) {
		model.addAttribute("query", new ShowSeatsQuery());
	}

	/**
	 * 锁座。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("lockSeats")
	public void lockSeats(Model model) {
		model.addAttribute("query", new LockSeatQuery());
	}

	/**
	 * 释放座位。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("releaseSeats")
	public void releaseSeats(Model model) {
		model.addAttribute("query", new ReleaseSeatQuery());
	}

	/**
	 * 确认订单。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("submitOrder")
	public void submitOrder(Model model) {
		model.addAttribute("query", new SubmitOrderQuery());
	}

	/**
	 * 查询订单。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryOrder")
	public void queryOrder(Model model) {
		model.addAttribute("query", new OrderQuery());
	}

	/**
	 * 退票。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("revokeTicket")
	public void revokeTicket(Model model) {
		model.addAttribute("query", new RevokeTicketQuery());
	}

	/**
	 * 标记退票。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("markTicketRevoked")
	public void markTicketRevoked(Model model) {
		model.addAttribute("query", new MarkTicketRevokedQuery());
	}

	/**
	 * 根据凭证编码验证凭证。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryPrintByVoucher")
	public void queryPrintByVoucher(Model model) {
		model.addAttribute("query", new PrintByVoucherQuery());
	}

	/**
	 * 确认打票。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("confirmPrint")
	public void confirmPrint(Model model) {
		model.addAttribute("query", new ConfirmPrintQuery());
	}

	/**
	 * 根据取票号和取票验证码验证凭证。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryPrintByVerifyCode")
	public void queryPrintByVerifyCode(Model model) {
		model.addAttribute("query", new PrintByVerifyCodeQuery());
	}

	/**
	 * 更换凭证。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("changeVoucher")
	public void changeVoucher(Model model) {
		model.addAttribute("query", new ChangeVoucherQuery());
	}

	/**
	 * 重置凭证。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("resetVoucher")
	public void resetVoucher(Model model) {
		model.addAttribute("query", new ResetVoucherQuery());
	}

	/**
	 * 查询渠道列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryChannels")
	public void queryChannels(Model model) {
		model.addAttribute("query", new ChannelsQuery());
	}

	/**
	 * 查询卖品列表。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("querySnacks")
	public void querySnacks(Model model) {
		model.addAttribute("query", new SnacksQuery());
	}

	/**
	 * 获取卡类。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryBenefitCardTypes")
	public void queryBenefitCardTypes(Model model) {
		model.addAttribute("query", new BenefitCardTypesQuery());
	}

	/**
	 * 开卡。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("openBenefitCard")
	public void openBenefitCard(Model model) {
		model.addAttribute("query", new OpenBenefitCardQuery());
	}

	/**
	 * 查询开卡订单。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryOpenBenefitCardOrder")
	public void queryOpenBenefitCardOrder(Model model) {
		model.addAttribute("query", new OpenBenefitCardOrderQuery());
	}

	/**
	 * 续费。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("rechargeBenefitCard")
	public void rechargeBenefitCard(Model model) {
		model.addAttribute("query", new RechargeBenefitCardQuery());
	}

	/**
	 * 查询续费订单。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryRechargeBenefitCardOrder")
	public void queryRechargeBenefitCardOrder(Model model) {
		model.addAttribute("query", new RechargeBenefitCardOrderQuery());
	}

	/**
	 * 查询卡信息。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("queryBenefitCard")
	public void queryBenefitCard(Model model) {
		model.addAttribute("query", new BenefitCardQuery());
	}

	/**
	 * 更改手机号码。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("changeMobile")
	public void changeMobile(Model model) {
		model.addAttribute("query", new ChangeMobileQuery());
	}

	/**
	 * 确认卖品。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("submitSnack")
	public void submitSnack(Model model) {
		model.addAttribute("query", new SubmitSnackQuery());
	}

	/**
	 * 退订卖品。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("revokeSnack")
	public void revokeSnack(Model model) {
		model.addAttribute("query", new RevokeSnackQuery());
	}

	/**
	 * 标记退订卖品。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("markSnackRevoked")
	public void markSnackRevoked(Model model) {
		model.addAttribute("query", new MarkSnackRevokedQuery());
	}

	/**
	 * 查询卖品订单。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("querySnackOrder")
	public void querySnackOrder(Model model) {
		model.addAttribute("query", new SnackOrderQuery());
	}
}