package cn.mopon.cec.api.actions.api.ticket;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.mopon.cec.api.actions.api.ApiAction;
import cn.mopon.cec.api.ticket.v1.BenefitCardQuery;
import cn.mopon.cec.api.ticket.v1.BenefitCardReply;
import cn.mopon.cec.api.ticket.v1.BenefitCardTypesQuery;
import cn.mopon.cec.api.ticket.v1.BenefitCardTypesReply;
import cn.mopon.cec.api.ticket.v1.ChangeMobileQuery;
import cn.mopon.cec.api.ticket.v1.ChangeMobileReply;
import cn.mopon.cec.api.ticket.v1.ChangeVoucherQuery;
import cn.mopon.cec.api.ticket.v1.ChangeVoucherReply;
import cn.mopon.cec.api.ticket.v1.ChannelsQuery;
import cn.mopon.cec.api.ticket.v1.ChannelsReply;
import cn.mopon.cec.api.ticket.v1.CinemaQuery;
import cn.mopon.cec.api.ticket.v1.CinemaReply;
import cn.mopon.cec.api.ticket.v1.CinemasQuery;
import cn.mopon.cec.api.ticket.v1.CinemasReply;
import cn.mopon.cec.api.ticket.v1.ConfirmPrintQuery;
import cn.mopon.cec.api.ticket.v1.ConfirmPrintReply;
import cn.mopon.cec.api.ticket.v1.FilmQuery;
import cn.mopon.cec.api.ticket.v1.FilmReply;
import cn.mopon.cec.api.ticket.v1.FilmsQuery;
import cn.mopon.cec.api.ticket.v1.FilmsReply;
import cn.mopon.cec.api.ticket.v1.HallsQuery;
import cn.mopon.cec.api.ticket.v1.HallsReply;
import cn.mopon.cec.api.ticket.v1.LockSeatQuery;
import cn.mopon.cec.api.ticket.v1.LockSeatReply;
import cn.mopon.cec.api.ticket.v1.MarkSnackRevokedQuery;
import cn.mopon.cec.api.ticket.v1.MarkSnackRevokedReply;
import cn.mopon.cec.api.ticket.v1.MarkTicketRevokedQuery;
import cn.mopon.cec.api.ticket.v1.MarkTicketRevokedReply;
import cn.mopon.cec.api.ticket.v1.OpenBenefitCardOrderQuery;
import cn.mopon.cec.api.ticket.v1.OpenBenefitCardOrderReply;
import cn.mopon.cec.api.ticket.v1.OpenBenefitCardQuery;
import cn.mopon.cec.api.ticket.v1.OpenBenefitCardReply;
import cn.mopon.cec.api.ticket.v1.OrderQuery;
import cn.mopon.cec.api.ticket.v1.OrderReply;
import cn.mopon.cec.api.ticket.v1.PrintByVerifyCodeQuery;
import cn.mopon.cec.api.ticket.v1.PrintByVoucherQuery;
import cn.mopon.cec.api.ticket.v1.PrintReply;
import cn.mopon.cec.api.ticket.v1.RechargeBenefitCardOrderQuery;
import cn.mopon.cec.api.ticket.v1.RechargeBenefitCardOrderReply;
import cn.mopon.cec.api.ticket.v1.RechargeBenefitCardQuery;
import cn.mopon.cec.api.ticket.v1.RechargeBenefitCardReply;
import cn.mopon.cec.api.ticket.v1.ReleaseSeatQuery;
import cn.mopon.cec.api.ticket.v1.ReleaseSeatReply;
import cn.mopon.cec.api.ticket.v1.ResetVoucherQuery;
import cn.mopon.cec.api.ticket.v1.ResetVoucherReply;
import cn.mopon.cec.api.ticket.v1.RevokeSnackQuery;
import cn.mopon.cec.api.ticket.v1.RevokeSnackReply;
import cn.mopon.cec.api.ticket.v1.RevokeTicketQuery;
import cn.mopon.cec.api.ticket.v1.RevokeTicketReply;
import cn.mopon.cec.api.ticket.v1.SeatsQuery;
import cn.mopon.cec.api.ticket.v1.SeatsReply;
import cn.mopon.cec.api.ticket.v1.ShowQuery;
import cn.mopon.cec.api.ticket.v1.ShowReply;
import cn.mopon.cec.api.ticket.v1.ShowSeatsQuery;
import cn.mopon.cec.api.ticket.v1.ShowSeatsReply;
import cn.mopon.cec.api.ticket.v1.ShowsQuery;
import cn.mopon.cec.api.ticket.v1.ShowsReply;
import cn.mopon.cec.api.ticket.v1.SnackOrderQuery;
import cn.mopon.cec.api.ticket.v1.SnackOrderReply;
import cn.mopon.cec.api.ticket.v1.SnacksQuery;
import cn.mopon.cec.api.ticket.v1.SnacksReply;
import cn.mopon.cec.api.ticket.v1.SubmitOrderQuery;
import cn.mopon.cec.api.ticket.v1.SubmitOrderReply;
import cn.mopon.cec.api.ticket.v1.SubmitSnackQuery;
import cn.mopon.cec.api.ticket.v1.SubmitSnackReply;
import cn.mopon.cec.api.ticket.v1.TicketApiV1Service;
import cn.mopon.cec.core.enums.TicketApiMethod;

/**
 * 接出API。
 */
@Controller
@RequestMapping("/api/ticket/v1")
public class TicketApiV1Action extends ApiAction {
	@Resource
	private TicketApiV1Service ticketApiV1Service;

	/**
	 * 查询影院列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryCinemas")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_CINEMAS)
	public CinemasReply queryCinemas(@Valid CinemasQuery query) {
		return ticketApiV1Service.queryCinemas(query);
	}

	/**
	 * 查询影院。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryCinema")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_CINEMA)
	public CinemaReply queryCinema(@Valid CinemaQuery query) {
		return ticketApiV1Service.queryCinema(query);
	}

	/**
	 * 查询影厅列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryHalls")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_HALLS)
	public HallsReply queryHalls(@Valid HallsQuery query) {
		return ticketApiV1Service.queryHalls(query);
	}

	/**
	 * 查询影厅座位列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("querySeats")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_SEATS)
	public SeatsReply querySeats(@Valid SeatsQuery query) {
		return ticketApiV1Service.querySeats(query);
	}

	/**
	 * 查询影片列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryFilms")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_FILMS)
	public FilmsReply queryFilms(@Valid FilmsQuery query) {
		return ticketApiV1Service.queryFilms(query);
	}

	/**
	 * 查询影片。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryFilm")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_FILM)
	public FilmReply queryFilm(@Valid FilmQuery query) {
		return ticketApiV1Service.queryFilm(query);
	}

	/**
	 * 查询场次列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryShows")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_SHOWS)
	public ShowsReply queryShows(@Valid ShowsQuery query) {
		return ticketApiV1Service.queryShows(query);
	}

	/**
	 * 查询替代场次。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryReplaceShow")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_REPLACE_SHOW)
	public ShowReply queryReplaceShow(@Valid ShowQuery query) {
		return ticketApiV1Service.queryReplaceShow(query);
	}

	/**
	 * 查询场次座位列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryShowSeats")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_SHOW_SEATS)
	public ShowSeatsReply queryShowSeats(@Valid ShowSeatsQuery query) {
		return ticketApiV1Service.queryShowSeats(query);
	}

	/**
	 * 锁座。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("lockSeats")
	@TikcetApiMethodAuth(TicketApiMethod.LOCK_SEATS)
	public LockSeatReply lockSeats(@Valid LockSeatQuery query) {
		return ticketApiV1Service.lockSeats(query);
	}

	/**
	 * 释放座位。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("releaseSeats")
	@TikcetApiMethodAuth(TicketApiMethod.RELEASE_SEATS)
	public ReleaseSeatReply releaseSeats(@Valid ReleaseSeatQuery query) {
		return ticketApiV1Service.releaseSeats(query);
	}

	/**
	 * 确认订单。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("submitOrder")
	@TikcetApiMethodAuth(TicketApiMethod.SUBMIT_ORDER)
	public SubmitOrderReply submitOrder(@Valid SubmitOrderQuery query) {
		return ticketApiV1Service.submitOrder(query);
	}

	/**
	 * 查询订单。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryOrder")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_ORDER)
	public OrderReply queryOrder(@Valid OrderQuery query) {
		return ticketApiV1Service.queryOrder(query);
	}

	/**
	 * 退票。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("revokeTicket")
	@TikcetApiMethodAuth(TicketApiMethod.REVOKE_TICKET)
	public RevokeTicketReply revokeTicket(@Valid RevokeTicketQuery query) {
		return ticketApiV1Service.revokeTicket(query);
	}

	/**
	 * 标记退票。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("markTicketRevoked")
	@TikcetApiMethodAuth(TicketApiMethod.MARK_TICKET_REVOKED)
	public MarkTicketRevokedReply markTicketRevoked(
			@Valid MarkTicketRevokedQuery query) {
		return ticketApiV1Service.markTicketRevoked(query);
	}

	/**
	 * 根据凭证编码进行凭证验证。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryPrintByVoucher")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_PRINT_BY_VOUCHER)
	public PrintReply queryPrintByVoucher(@Valid PrintByVoucherQuery query) {
		return ticketApiV1Service.queryPrintByVoucher(query);
	}

	/**
	 * 根据取票号和取票验证码进行凭证验证。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryPrintByVerifyCode")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_PRINT_BY_VERIFYCODE)
	public PrintReply queryPrintByVerifyCode(@Valid PrintByVerifyCodeQuery query) {
		return ticketApiV1Service.queryPrintByVerifyCode(query);
	}

	/**
	 * 确认打票。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("confirmPrint")
	@TikcetApiMethodAuth(TicketApiMethod.CONFIRM_PRINT)
	public ConfirmPrintReply confirmPrint(@Valid ConfirmPrintQuery query) {
		return ticketApiV1Service.confirmPrint(query);
	}

	/**
	 * 更换凭证。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("changeVoucher")
	@TikcetApiMethodAuth(TicketApiMethod.CHANGE_VOUCHER)
	public ChangeVoucherReply changeVoucher(@Valid ChangeVoucherQuery query) {
		return ticketApiV1Service.changeVoucher(query);
	}

	/**
	 * 重置凭证。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("resetVoucher")
	@TikcetApiMethodAuth(TicketApiMethod.RESET_VOUCHER)
	public ResetVoucherReply reprintVoucher(@Valid ResetVoucherQuery query) {
		return ticketApiV1Service.resetVoucher(query);
	}

	/**
	 * 查询渠道列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryChannels")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_CHANNELS)
	public ChannelsReply queryChannels(@Valid ChannelsQuery query) {
		return ticketApiV1Service.queryChannels(query);
	}

	/**
	 * 查询卖品列表。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("querySnacks")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_SNACKS)
	public SnacksReply querySnacks(@Valid SnacksQuery query) {
		return ticketApiV1Service.querySnacks(query);
	}

	/**
	 * 获取卡类。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryBenefitCardTypes")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_BENEFITCARDTYPE)
	public BenefitCardTypesReply queryBenefitCardTypes(
			@Valid BenefitCardTypesQuery query) {
		return ticketApiV1Service.queryBenefitCardTypes(query);

	}

	/**
	 * 开卡。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("openBenefitCard")
	@TikcetApiMethodAuth(TicketApiMethod.OPEN_BENEFITCARD)
	public OpenBenefitCardReply queryBenefitCard(
			@Valid OpenBenefitCardQuery query) {
		return ticketApiV1Service.openBenefitCard(query);
	}

	/**
	 * 续费。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("rechargeBenefitCard")
	@TikcetApiMethodAuth(TicketApiMethod.RECHARGE_BENEFITCARD)
	public RechargeBenefitCardReply rechargeBenefitCard(
			@Valid RechargeBenefitCardQuery query) {
		return ticketApiV1Service.rechargeBenefitCard(query);

	}

	/**
	 * 查询续费订单。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryRechargeBenefitCardOrder")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_RECHARGEBENEFITCATDORDER)
	public RechargeBenefitCardOrderReply queryRechargeBenefitCardOrder(
			@Valid RechargeBenefitCardOrderQuery query) {
		return ticketApiV1Service.queryRechargeBenefitCardOrder(query);

	}

	/**
	 * 查询开卡订单。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryOpenBenefitCardOrder")
	@TikcetApiMethodAuth(TicketApiMethod.OPEN_BENEFITCARDORDER)
	public OpenBenefitCardOrderReply queryOpenBenefitCardOrder(
			@Valid OpenBenefitCardOrderQuery query) {
		return ticketApiV1Service.queryOpenBenefitCardOrder(query);

	}

	/**
	 * 查询卡信息。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("queryBenefitCard")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_BENEFITCARD)
	public BenefitCardReply queryBenefitCard(@Valid BenefitCardQuery query) {
		return ticketApiV1Service.queryBenefitCard(query);
	}

	/**
	 * 更改手机号码。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("changeMobile")
	@TikcetApiMethodAuth(TicketApiMethod.CHANGE_MOBILE)
	public ChangeMobileReply queryBenefitCard(@Valid ChangeMobileQuery query) {
		return ticketApiV1Service.changeMobile(query);
	}

	/**
	 * 确认卖品。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("submitSnack")
	@TikcetApiMethodAuth(TicketApiMethod.SUBMIT_SNACK)
	public SubmitSnackReply submitSnack(@Valid SubmitSnackQuery query) {
		return ticketApiV1Service.submitSnack(query);
	}

	/**
	 * 退订卖品。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("revokeSnack")
	@TikcetApiMethodAuth(TicketApiMethod.REVOKE_SNACK)
	public RevokeSnackReply revokeSnack(@Valid RevokeSnackQuery query) {
		return ticketApiV1Service.revokeSnack(query);
	}

	/**
	 * 标记退订卖品。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("markSnackRevoked")
	@TikcetApiMethodAuth(TicketApiMethod.MARK_SNACK_REVOKED)
	public MarkSnackRevokedReply markSnackRevoked(
			@Valid MarkSnackRevokedQuery query) {
		return ticketApiV1Service.markSnackRevoked(query);
	}

	/**
	 * 查询卖品订单。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回响应对象。
	 */
	@RequestMapping("querySnackOrder")
	@TikcetApiMethodAuth(TicketApiMethod.QUERY_SNACK_ORDER)
	public SnackOrderReply querySnackOrder(@Valid SnackOrderQuery query) {
		return ticketApiV1Service.querySnackOrder(query);
	}
}