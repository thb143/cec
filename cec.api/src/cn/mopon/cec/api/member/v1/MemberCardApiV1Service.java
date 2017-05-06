package cn.mopon.cec.api.member.v1;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.api.ApiReplyCode;
import cn.mopon.cec.api.member.MemberFacade;
import cn.mopon.cec.api.member.MemberReplyCode;
import cn.mopon.cec.core.access.member.MemberAccessService;
import cn.mopon.cec.core.access.member.MemberAccessServiceFactory;
import cn.mopon.cec.core.access.member.enums.MemberReplyError;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.service.HomeService;
import cn.mopon.cec.core.service.VoucherService;
import coo.base.exception.BusinessException;
import coo.base.util.BeanUtils;
import coo.base.util.DateUtils;
import coo.core.hibernate.dao.Dao;

/**
 * 接出接口服务类。
 */
@Service
public class MemberCardApiV1Service {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private MemberFacade memberFacade;
	@Resource
	private HomeService homeService;
	@Resource
	private VoucherService voucherService;
	@Resource
	private Dao<TicketVoucher> ticketVoucherDao;

	/**
	 * 会员卡登入验证。
	 * 
	 * @param query
	 *            登入验证请求对象
	 * @return 返回会员卡信息对象。
	 */
	public CardVerifyReply verifyCard(CardVerifyQuery query) {
		Cinema cinema = memberFacade.queryCinema(query.getCinemaCode());
		MemberAccessService service = getMemberAccessService(cinema);
		MemberCard memberCard = new MemberCard(query.getCinemaCode(),
				query.getCardCode(), query.getPassword());
		MemberCard card = null;
		try {
			card = service.getVerifyCard(memberCard);
		} catch (BusinessException be) {
			throw new ApiException(MemberReplyCode.CARD_OR_PASSWORD_ERROR);
		}
		card.setCinemaCode(memberCard.getCinemaCode());
		card.setCardCode(memberCard.getCardCode());
		return new CardVerifyReply(card);
	}

	/**
	 * 查询会员卡信息(会员卡号)。
	 * 
	 * @param query
	 *            查询会员卡请求对象
	 * @return 返回会员卡信息对象。
	 */
	public CardInfoByCodeReply queryCardByCode(CardInfoByCodeQuery query) {
		Cinema cinema = memberFacade.queryCinema(query.getCinemaCode());
		MemberAccessService service = getMemberAccessService(cinema);
		MemberCard memberCard = new MemberCard(query.getCinemaCode(),
				query.getCardCode(), query.getPassword());

		MemberCard card = null;
		try {
			card = service.getMemberCardInfo(memberCard);
		} catch (BusinessException be) {
			throw new ApiException(MemberReplyCode.CARD_OR_PASSWORD_ERROR);
		}
		card.setCinemaCode(memberCard.getCinemaCode());
		card.setCardCode(memberCard.getCardCode());
		return new CardInfoByCodeReply(card);
	}

	/**
	 * 查询会员卡信息(芯片号)。
	 * 
	 * @param query
	 *            查询会员卡请求对象
	 * @return 返回会员卡信息对象。
	 */
	public CardInfoByChipReply getMemberCardInfoByChip(CardInfoByChipQuery query) {
		Cinema cinema = memberFacade.queryCinema(query.getCinemaCode());
		MemberAccessService service = getMemberAccessService(cinema);
		MemberCard memberCard = new MemberCard(query.getCinemaCode(),
				query.getChipCode());
		MemberCard card = null;
		try {
			card = service.getMemberCardInfoByChip(memberCard);
		} catch (BusinessException be) {
			throw new ApiException(MemberReplyCode.MEMBERCHIP_NOT_EXIST);
		}
		card.setCinemaCode(memberCard.getCinemaCode());
		card.setCardCode(memberCard.getChipCode());
		return new CardInfoByChipReply(card);
	}

	/**
	 * 会员卡充值(会员卡号)。
	 * 
	 * @param query
	 *            会员卡充值请求对象
	 * @return 返回会员卡充值结果对象。
	 */
	public CardRechargeByCodeReply cardRechargeByCode(
			CardRechargeByCodeQuery query) {
		checkMoney(query.getMoney());
		Cinema cinema = memberFacade.queryCinema(query.getCinemaCode());
		MemberAccessService service = getMemberAccessService(cinema);
		MemberCard memberCard = new MemberCard(query.getCinemaCode(),
				query.getCardCode(), query.getPassword());
		double balance = 0d;
		try {
			balance = service.getMemberCardRecharge(memberCard,
					query.getMoney(), query.getPartnerDepositCode());
		} catch (BusinessException be) {
			throw new ApiException(MemberReplyCode.CARD_OR_PASSWORD_ERROR);
		}
		return new CardRechargeByCodeReply(balance);
	}

	/**
	 * 会员卡充值(芯片号)。
	 * 
	 * @param query
	 *            会员卡充值请求对象
	 * @return 返回会员卡充值结果对象。
	 */
	public CardRechargeByChipReply cardRechargeByChip(
			CardRechargeByChipQuery query) {
		checkMoney(query.getMoney());
		Cinema cinema = memberFacade.queryCinema(query.getCinemaCode());
		MemberAccessService service = getMemberAccessService(cinema);
		MemberCard memberCard = new MemberCard(query.getCinemaCode(),
				query.getChipCode());
		double balance = 0d;
		try {
			balance = service.getMemberCardRechargeByChip(memberCard,
					query.getMoney(), query.getPartnerDepositCode());
		} catch (BusinessException be) {
			throw new ApiException(MemberReplyCode.MEMBERCHIP_NOT_EXIST);
		}
		return new CardRechargeByChipReply(balance);
	}

	/**
	 * 查询会员卡折扣。
	 * 
	 * @param query
	 *            查询会员卡折扣请求对象
	 * @return 返回会员卡折扣信息对象。
	 */
	public DiscountPriceReply getDiscountPrice(DiscountPriceQuery query) {
		TicketOrder order = memberFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		MemberAccessService service = getMemberAccessService(order.getCinema());
		MemberCard memberCard = new MemberCard(order.getCinema().getCode(),
				query.getCardCode(), query.getPassword());
		List<SeatInfo> seatInfoList = null;
		try {
			seatInfoList = service.getDiscountPrice(order, memberCard);
		} catch (BusinessException be) {
			if (MemberReplyError.MEMBER_NOT_EXIST.getValue().equals(
					be.getMessage())) {
				throw new ApiException(MemberReplyCode.CARD_OR_PASSWORD_ERROR);
			}
			throw be;
		}
		return new DiscountPriceReply(seatInfoList);
	}

	/**
	 * 会员卡支付扣款。
	 * 
	 * @param query
	 *            会员卡扣款请求对象
	 * @return 返回会员卡余额信息对象。
	 */
	@Transactional
	public CardPayReply memberCardPay(CardPayQuery query) {
		TicketOrder order = memberFacade.queryTicketOrder(
				query.getChannelCode(), query.getOrderCode());
		CardPayReply reply = new CardPayReply();
		MemberCard memberCard = new MemberCard(order.getCinema().getCode(),
				query.getCardCode(), query.getPassword());
		try {
			// 调用会员卡付款接口。
			submitOrder(order, memberCard, query);
			reply.setPrintMode(order.getCinema().getTicketSettings()
					.getPrintMode());
			reply.setProvider(order.getProvider());
			reply.setVerifyCode(order.getVoucher().getVerifyCode());
			reply.setPrintCode(order.getVoucher().getPrintCode());
			reply.setVoucherCode(order.getVoucher().getCode());
		} catch (BusinessException be) {
			processPayException(order, reply, be);
		} catch (ApiException ae) {
			log.error("确认订单时发生异常。", ae);
			reply.setReplyCode(ae.getReplyCode());
		} catch (Exception e) {
			log.error("确认订单时发生未预料到的异常。", e);
			throw new ApiException(ApiReplyCode.FAILED);
		}
		return reply;
	}

	/**
	 * 处理支付异常。
	 * 
	 * @param order
	 *            订单
	 * @param reply
	 *            响应
	 * @param exception
	 *            异常信息
	 */
	private void processPayException(TicketOrder order, CardPayReply reply,
			BusinessException exception) {
		if (MemberReplyError.MEMBER_NOT_EXIST.getValue().equals(
				exception.getMessage())) {
			throw new ApiException(MemberReplyCode.CARD_OR_PASSWORD_ERROR);
		} else if (MemberReplyError.MEMBER_LACK_BALANCE.getValue().equals(
				exception.getMessage())) {
			throw new ApiException(MemberReplyCode.BALANCE_NOT_ENOUGH);
		} else if (MemberReplyError.MEMBERCHIP_SEAT_PAY.getValue().equals(
				exception.getMessage())) {
			throw new ApiException(MemberReplyCode.MEMBERCHIP_SEAT_PAY);
		} else if (MemberReplyError.ORDER_SUBMIT_FAILED.getValue().equals(
				exception.getMessage())) {
			order.setStatus(TicketOrderStatus.PAID);
			log.error("消费扣款成功，确认订单失败。", exception);
			reply.setReplyCode(MemberReplyCode.PAY_SUBMIT_SUMBIT_ERROR);
		} else {
			log.error("确认订单失败。", exception);
			throw new ApiException(MemberReplyCode.ORDER_SUBMIT_FAILED);
		}
	}

	/**
	 * 确认订单。
	 * 
	 * @param order
	 *            选座票订单
	 * @param memberCard
	 *            会员卡
	 * @param query
	 *            卡支付请求对象
	 */
	public void submitOrder(TicketOrder order, MemberCard memberCard,
			CardPayQuery query) {
		MemberAccessService memberCardAccessService = getMemberAccessService(order
				.getCinema());
		// 支付订单。
		if (!order.isSubmitable()) {
			ApiException.thrown(MemberReplyCode.ORDER_STATUS_INVALID);
		}
		if (!order.isSeatsMatched(query.getSeatInfos())) {
			ApiException.thrown(MemberReplyCode.ORDER_SEATS_UNMATCH);
		}
		order.setSaleAmount(query.getSeatInfos());
		order.setPayTime(new Date());
		order.setMobile(query.getMobile());
		TicketOrder externalTicketOrder = memberCardAccessService
				.memberCardPay(order, memberCard);
		order.confirm(externalTicketOrder);
		createVoucher(order, externalTicketOrder);

		homeService.incrTicketOrderStat(order);
	}

	/**
	 * 创建凭证。
	 * 
	 * @param order
	 *            订单
	 * @param externalOrder
	 *            外部订单
	 */
	private void createVoucher(TicketOrder order, TicketOrder externalOrder) {
		TicketVoucher voucher = new TicketVoucher();
		BeanUtils.copyFields(externalOrder.getVoucher(), voucher);
		voucher.setOrder(order);
		voucher.setId(order.getId());
		voucher.setCode(voucherService.genTicketVoucherCode(order));
		voucher.setGenTime(new Date());
		Date showTime = voucher.getOrder().getShowTime();
		voucher.setExpireTime(DateUtils.getNextDay(showTime));
		order.setVoucher(voucher);
		ticketVoucherDao.save(voucher);
	}

	/**
	 * 生成选座票凭证。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public void genVoucher(TicketOrder order) {
		TicketVoucher voucher = new TicketVoucher();
		voucher.setId(order.getId());
		voucher.setCode(voucherService.genTicketVoucherCode(order));
		voucher.setOrder(order);
		voucher.setGenTime(new Date());
		Date showTime = voucher.getOrder().getShowTime();
		voucher.setExpireTime(DateUtils.getNextDay(showTime));
		order.setVoucher(voucher);
		ticketVoucherDao.save(voucher);
	}

	/**
	 * 获取会员卡接入服务组件。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回选座票接入服务组件。
	 */
	private MemberAccessService getMemberAccessService(Cinema cinema) {
		if (!cinema.getMemberSetted()) {
			throw new ApiException(MemberReplyCode.MEMBER_NOT_SETTED);
		}
		return MemberAccessServiceFactory.getMemberService(cinema
				.getMemberSettings());
	}

	/***
	 * 检查充值金额。
	 * 
	 * @param rechargeMoney
	 *            充值金额
	 */
	private void checkMoney(String rechargeMoney) {
		try {
			int money = Integer.parseInt(rechargeMoney);
			if (money <= 0) {
				ApiException.thrown(MemberReplyCode.AMOUNT_NOT_MINUS);
			}
		} catch (NumberFormatException e) {
			ApiException.thrown(MemberReplyCode.AMOUNT_FORMAT_ERROR);
		}
	}
}
