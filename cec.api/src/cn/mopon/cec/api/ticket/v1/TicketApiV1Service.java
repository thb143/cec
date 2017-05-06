package cn.mopon.cec.api.ticket.v1;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.api.ApiReplyCode;
import cn.mopon.cec.api.ticket.TicketFacade;
import cn.mopon.cec.api.ticket.TicketReplyCode;
import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.entity.BenefitCardRechargeOrder;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.entity.SnackOrder;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.enums.BenefitCardRechargeStatus;
import cn.mopon.cec.core.service.Circuit;
import coo.base.exception.BusinessException;

/**
 * 接出接口服务类。
 */
@Service
public class TicketApiV1Service {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private TicketFacade ticketFacade;
	@Resource
	private Circuit circuit;
	@Value(value = "${content.server.url}")
	private String url;

	/**
	 * 查询影院列表。
	 * 
	 * @param query
	 *            查询影院列表请求对象
	 * @return 返回查询影院列表响应对象。
	 */
	@Transactional(readOnly = true)
	public CinemasReply queryCinemas(CinemasQuery query) {
		Channel channel = ticketFacade.queryChannel(query.getChannelCode());
		return new CinemasReply(channel.getOpenedCinemas(), url);
	}

	/**
	 * 查询影院。
	 * 
	 * @param query
	 *            查询影院请求对象
	 * @return 返回查询影院响应对象。
	 */
	@Transactional(readOnly = true)
	public CinemaReply queryCinema(CinemaQuery query) {
		Cinema cinema = ticketFacade.queryCinema(query.getCinemaCode());
		return new CinemaReply(cinema, url);
	}

	/**
	 * 查询影厅列表。
	 * 
	 * @param query
	 *            查询影厅列表请求对象
	 * @return 返回查询影厅列表响应对象。
	 */
	@Transactional(readOnly = true)
	public HallsReply queryHalls(HallsQuery query) {
		Cinema cinema = ticketFacade.queryCinema(query.getCinemaCode());
		return new HallsReply(cinema.getEnableHalls());
	}

	/**
	 * 查询座位列表。
	 * 
	 * @param query
	 *            查询座位列表请求对象
	 * @return 返回查询座位列表响应对象。
	 */
	@Transactional(readOnly = true)
	public SeatsReply querySeats(SeatsQuery query) {
		Hall hall = ticketFacade.queryHall(query.getCinemaCode(),
				query.getHallCode());
		return new SeatsReply(hall.getSeats());
	}

	/**
	 * 查询影片列表。
	 * 
	 * @param query
	 *            查询影片列表请求对象
	 * @return 返回查询影片列表响应对象。
	 */
	@Transactional(readOnly = true)
	public FilmsReply queryFilms(FilmsQuery query) {
		List<Film> films = ticketFacade.queryFilms(query.getStartDate());
		return new FilmsReply(films);
	}

	/**
	 * 查询影片。
	 * 
	 * @param query
	 *            查询影片请求对象
	 * @return 返回查询影片响应对象。
	 */
	@Transactional(readOnly = true)
	public FilmReply queryFilm(FilmQuery query) {
		Film film = ticketFacade.queryFilm(query.getFilmCode());
		return new FilmReply(film);
	}

	/**
	 * 查询场次列表。
	 * 
	 * @param query
	 *            查询场次列表请求对象
	 * @return 返回查询场次列表响应对象。
	 */
	@Transactional(readOnly = true)
	public ShowsReply queryShows(ShowsQuery query) {
		List<ChannelShow> channelShows = ticketFacade.queryChannelShowByCinema(
				query.getChannelCode(), query.getCinemaCode(),
				query.getStartDate(), query.getStatus());
		return new ShowsReply(channelShows);
	}

	/**
	 * 查询替代场次。
	 * 
	 * @param query
	 *            查询场次请求对象
	 * @return 返回查询场次响应对象。
	 */
	@Transactional(readOnly = true)
	public ShowReply queryReplaceShow(ShowQuery query) {
		ChannelShow channelShow = ticketFacade.queryReplaceChannelShow(
				query.getChannelCode(), query.getChannelShowCode());
		return new ShowReply(channelShow);
	}

	/**
	 * 查询场次座位。
	 * 
	 * @param query
	 *            查询场次座位请求对象
	 * @return 返回查询场次座位响应对象。
	 */
	@Transactional(readOnly = true)
	public ShowSeatsReply queryShowSeats(ShowSeatsQuery query) {
		ChannelShow channelShow = ticketFacade.queryChannelShow(
				query.getChannelCode(), query.getChannelShowCode());
		return new ShowSeatsReply(ticketFacade.getShowSeats(channelShow,
				query.getStatus()));
	}

	/**
	 * 锁定座位。
	 * 
	 * @param query
	 *            锁座请求对象
	 * @return 返回锁座响应对象。
	 */
	@Transactional
	public LockSeatReply lockSeats(LockSeatQuery query) {
		ChannelShow channelShow = ticketFacade.queryChannelShow(
				query.getChannelCode(), query.getChannelShowCode());
		LockSeatReply reply = new LockSeatReply();
		try {
			TicketOrder order = ticketFacade.lockSeats(channelShow,
					query.getChannelOrderCode(), query.getSeatCodes(),
					query.getSubmitPrice());
			reply.setOrderCode(order.getCode());
			reply.setLockTime(circuit.getLockSeatTime());
		} catch (BusinessException be) {
			reply.setReplyCode(TicketReplyCode.ORDER_LOCK_SEAT_FAILED);
		}
		return reply;
	}

	/**
	 * 确认订单。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回确认订单响应对象。
	 */
	public SubmitOrderReply submitOrder(SubmitOrderQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		SubmitOrderReply reply = new SubmitOrderReply();
		try {
			ticketFacade.payOrder(order, query);
			ticketFacade.submitOrder(order);
			reply.setPrintMode(order.getCinema().getTicketSettings()
					.getPrintMode());
			reply.setProvider(order.getProvider());
			reply.setVerifyCode(order.getVoucher().getVerifyCode());
			reply.setPrintCode(order.getVoucher().getPrintCode());
			reply.setVoucherCode(order.getVoucher().getCode());
		} catch (BusinessException be) {
			log.error("确认订单失败。", be);
			reply.setReplyCode(TicketReplyCode.ORDER_SUBMIT_FAILED);
		} catch (ApiException ae) {
			log.error("确认订单时发生异常。", ae);
			reply.setReplyCode(ae.getReplyCode());
		} catch (Exception e) {
			log.error("确认订单时发生未预料到的异常。", e);
			reply.setReplyCode(ApiReplyCode.FAILED);
		}
		return reply;
	}

	/**
	 * 释放座位。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回释放座位响应对象。
	 */
	@Transactional
	public ReleaseSeatReply releaseSeats(ReleaseSeatQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		ReleaseSeatReply reply = new ReleaseSeatReply();
		try {
			ticketFacade.releaseSeats(order);
		} catch (BusinessException be) {
			reply.setReplyCode(TicketReplyCode.ORDER_RELEASE_SEAT_FAILED);
		} catch (ApiException ae) {
			reply.setReplyCode(ae.getReplyCode());
		}
		return reply;
	}

	/**
	 * 查询订单。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回查询订单响应对象。
	 */
	@Transactional(readOnly = true)
	public OrderReply queryOrder(OrderQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		return new OrderReply(order);
	}

	/**
	 * 退票。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回退票响应对象。
	 */
	@Transactional
	public RevokeTicketReply revokeTicket(RevokeTicketQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		RevokeTicketReply reply = new RevokeTicketReply();
		try {
			ticketFacade.revokeTicket(order);
		} catch (BusinessException be) {
			reply.setReplyCode(TicketReplyCode.ORDER_REVOKE_FAILED);
		}
		return reply;
	}

	/**
	 * 标记退票。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回标记退票响应对象。
	 */
	@Transactional
	public MarkTicketRevokedReply markTicketRevoked(MarkTicketRevokedQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		ticketFacade.markTicketRevoked(order);
		return new MarkTicketRevokedReply();
	}

	/**
	 * 通过凭证编码查询验证信息。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回验证结果。
	 */
	@Transactional
	public PrintReply queryPrintByVoucher(PrintByVoucherQuery query) {
		TicketOrder order = ticketFacade.queryPrintByCode(query.getOrderCode(),
				query.getVoucherCode());
		return new PrintReply(order);
	}

	/**
	 * 通过取票号和取票验证码查询验证信息。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回验证结果。
	 */
	@Transactional
	public PrintReply queryPrintByVerifyCode(PrintByVerifyCodeQuery query) {
		TicketOrder order = ticketFacade.queryByVerifyCode(
				query.getOrderCode(), query.getPrintCode(),
				query.getVerifyCode());
		return new PrintReply(order);
	}

	/**
	 * 确认打票。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回验证结果。
	 */
	@Transactional
	public ConfirmPrintReply confirmPrint(ConfirmPrintQuery query) {
		ConfirmPrintReply reply = new ConfirmPrintReply();
		try {
			boolean isPrint = ticketFacade.confirmPrint(query.getOrderCode(),
					query.getVoucherCode());
			if (!isPrint) {
				reply.setReplyCode(TicketReplyCode.VOUCHER_PRINT);
			}
		} catch (BusinessException e) {
			reply.setReplyCode(TicketReplyCode.CONFIRM_PRINT_REFUSE);
		} catch (ApiException ae) {
			reply.setReplyCode(ae.getReplyCode());
		} catch (Exception e) {
			reply.setReplyCode(ApiReplyCode.FAILED);
		}
		return reply;
	}

	/**
	 * 更换凭证。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回更换凭证响应对象。
	 */
	@Transactional
	public ChangeVoucherReply changeVoucher(ChangeVoucherQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		ticketFacade.changeVoucher(order);
		return new ChangeVoucherReply(order.getVoucher());
	}

	/**
	 * 重置凭证。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回重置凭证响应对象。
	 */
	@Transactional
	public ResetVoucherReply resetVoucher(ResetVoucherQuery query) {
		TicketOrder order = ticketFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		ticketFacade.resetVoucher(order);
		return new ResetVoucherReply();
	}

	/**
	 * 查询渠道列表。
	 * 
	 * @param query
	 *            查询渠道列表请求对象
	 * @return 返回查询渠道列表响应对象。
	 */
	@Transactional(readOnly = true)
	public ChannelsReply queryChannels(ChannelsQuery query) {
		List<Channel> channels = ticketFacade.queryChannels();
		return new ChannelsReply(channels);
	}

	/**
	 * 查询卖品列表。
	 * 
	 * @param query
	 *            查询卖品列表请求对象
	 * @return 返回查询卖品列表响应对象。
	 */
	@Transactional(readOnly = true)
	public SnacksReply querySnacks(SnacksQuery query) {
		List<SnackChannel> snackChannels = ticketFacade.querySnackChannels(
				query.getChannelCode(), query.getCinemaCode(),
				query.getStatus());
		List<BenefitCardType> types = ticketFacade.queryBenefitCardTypes(query
				.getChannelCode());
		return new SnacksReply(snackChannels, types, url);
	}

	/**
	 * 获取卡类。
	 * 
	 * @param query
	 *            获取卡类请求对象
	 * @return 返回获取卡类响应对象。
	 */
	@Transactional(readOnly = true)
	public BenefitCardTypesReply queryBenefitCardTypes(
			BenefitCardTypesQuery query) {
		List<BenefitCardType> types = ticketFacade.queryBenefitCardTypes(query
				.getChannelCode());
		return new BenefitCardTypesReply(types);
	}

	/**
	 * 开卡。
	 * 
	 * @param query
	 *            获取开卡请求对象
	 * @return 返回获取开卡响应对象。
	 */
	@Transactional
	public OpenBenefitCardReply openBenefitCard(OpenBenefitCardQuery query) {
		BenefitCard benefitCard = ticketFacade.openBenefitCard(
				query.getChannelCode(), query.getMobile(), query.getTypeCode(),
				query.getChannelOrderCode(), query.getInitAmount(),
				query.getBirthday());
		return new OpenBenefitCardReply(benefitCard);
	}

	/**
	 * 查询开卡订单。
	 * 
	 * @param query
	 *            获取查询开卡订单请求对象
	 * @return 返回获取开卡订单响应对象。
	 */
	@Transactional(readOnly = true)
	public OpenBenefitCardOrderReply queryOpenBenefitCardOrder(
			OpenBenefitCardOrderQuery query) {
		BenefitCard benefitCard = ticketFacade
				.queryBenefitCardByChannelOrderCode(
						query.getChannelOrderCode(), query.getChannelCode());
		return new OpenBenefitCardOrderReply(benefitCard);
	}

	/**
	 * 查询卡信息。
	 * 
	 * @param query
	 *            查询卡信息请求对象
	 * @return 返回卡信息响应对象。
	 */
	@Transactional(readOnly = true)
	public BenefitCardReply queryBenefitCard(BenefitCardQuery query) {
		BenefitCard benefitCard = ticketFacade.queryBenefitCard(
				query.getChannelCode(), query.getTypeCode(), query.getMobile());
		return new BenefitCardReply(benefitCard);
	}

	/**
	 * 续费。
	 * 
	 * @param query
	 *            获取续费请求对象
	 * @return 返回续费响应对象。
	 */
	public RechargeBenefitCardReply rechargeBenefitCard(
			RechargeBenefitCardQuery query) {
		// 获取权益卡
		BenefitCard card = ticketFacade.getBenefitCard(query.getChannelCode(),
				query.getMobile(), query.getTypeCode());
		if (card == null) {
			ApiException.thrown(TicketReplyCode.BENEFITCARD_NOT_EXIST);
		}
		// 保存续费订单。
		BenefitCardRechargeOrder rechargeOrder = ticketFacade
				.saveRechargeBenefitCardOrder(card,
						query.getChannelOrderCode(), query.getRechargeAmount());
		// 续费
		ticketFacade.rechargeBenefitCard(card, rechargeOrder);

		if (rechargeOrder.getStatus() != BenefitCardRechargeStatus.SUCCESS) {
			ApiException.thrown(TicketReplyCode.RECHARGE_BENEFITCARD_FAIL);
		}
		return new RechargeBenefitCardReply(rechargeOrder);
	}

	/**
	 * 查询续费订单。
	 * 
	 * @param query
	 *            获取续费订单请求对象
	 * @return 返回续费订单响应对象。
	 */
	@Transactional(readOnly = true)
	public RechargeBenefitCardOrderReply queryRechargeBenefitCardOrder(
			RechargeBenefitCardOrderQuery query) {
		BenefitCardRechargeOrder rechargeOrder = ticketFacade
				.queryBenefitCardRechargeOrder(query.getChannelCode(),
						query.getChannelOrderCode());
		return new RechargeBenefitCardOrderReply(rechargeOrder);
	}

	/**
	 * 更改手机号码。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回更改手机号码响应对象。
	 */
	@Transactional
	public ChangeMobileReply changeMobile(ChangeMobileQuery query) {
		ChangeMobileReply reply = new ChangeMobileReply();
		try {
			ticketFacade.changeMobile(query.getChannelCode(),
					query.getTypeCode(), query.getOldMobile(),
					query.getNewMobile());
		} catch (BusinessException be) {
			reply.setReplyCode(ApiReplyCode.FAILED);
		} catch (ApiException ae) {
			reply.setReplyCode(ae.getReplyCode());
		}
		return reply;
	}

	/**
	 * 确认卖品。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回更改手机号码响应对象。
	 */
	public SubmitSnackReply submitSnack(SubmitSnackQuery query) {
		SubmitSnackReply reply = new SubmitSnackReply();
		try {
			SnackOrder order = ticketFacade.submitSnack(query.getChannelCode(),
					query.getChannelOrderCode(), query.getMobile(),
					query.getSnacks(), query.getBenefit());
			reply.setVoucherCode(order.getVoucher().getCode());
			reply.setOrderCode(order.getCode());
		} catch (BusinessException be) {
			log.error("确认卖品订单失败。", be);
			reply.setReplyCode(TicketReplyCode.SNACK_SUBMIT_FAILED);
		} catch (ApiException ae) {
			log.error("确认卖品订单时发生异常。", ae);
			reply.setReplyCode(ae.getReplyCode());
		} catch (Exception e) {
			log.error("确认卖品订单时发生未预料到的异常。", e);
			reply.setReplyCode(ApiReplyCode.FAILED);
		}
		return reply;
	}

	/**
	 * 退订卖品。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回退订卖品响应对象。
	 */
	@Transactional
	public RevokeSnackReply revokeSnack(RevokeSnackQuery query) {
		SnackOrder order = ticketFacade.querySnackOrder(query.getChannelCode(),
				query.getOrderCode());
		RevokeSnackReply reply = new RevokeSnackReply();
		try {
			ticketFacade.revokeSnack(order);
		} catch (BusinessException be) {
			reply.setReplyCode(TicketReplyCode.SNACK_REVOKE_FAILED);
		}
		return reply;
	}

	/**
	 * 标记退订卖品。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回标记退订卖品响应对象。
	 */
	@Transactional
	public MarkSnackRevokedReply markSnackRevoked(MarkSnackRevokedQuery query) {
		SnackOrder order = ticketFacade.querySnackOrder(query.getChannelCode(),
				query.getOrderCode());
		ticketFacade.markSnackRevoked(order);
		return new MarkSnackRevokedReply();
	}

	/**
	 * 查询卖品订单。
	 * 
	 * @param query
	 *            查询条件
	 * @return 返回查询卖品订单响应对象。
	 */
	@Transactional(readOnly = true)
	public SnackOrderReply querySnackOrder(SnackOrderQuery query) {
		SnackOrder order = ticketFacade.querySnackOrder(query.getChannelCode(),
				query.getOrderCode());
		return new SnackOrderReply(order);
	}
}