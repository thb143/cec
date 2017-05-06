package cn.mopon.cec.core.access.member.hln;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.access.member.MemberAccessService;
import cn.mopon.cec.core.access.member.enums.MemberReplyError;
import cn.mopon.cec.core.access.member.hln.vo.TicketDiscountVo;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.access.ticket.ng.NgTicketService;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.constants.Chars;
import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;
import coo.base.util.BeanUtils;

/**
 * 国标会员卡接口实现。
 */
public class HLNMemberCardService implements MemberAccessService {
	private Logger log = LoggerFactory.getLogger(getClass());
	private MemberSettings settings;

	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            会员卡设置
	 */
	public HLNMemberCardService(MemberSettings settings) {
		this.settings = settings;
	}

	@Override
	public MemberCard getVerifyCard(MemberCard memberCard) {
		return getMemberCardInfo(memberCard);
	}

	@Override
	public MemberCard getMemberCardInfo(MemberCard memberCard) {
		MemberDetailQuery cardInfoQuery = new MemberDetailQuery(
				memberCard.getCardCode());
		HLNServiceQuery<MemberDetailQuery> serviceQuery = createHlnServiceQuery(cardInfoQuery);

		MemberDetailReply cardInfoReply = new MemberDetailReply();
		HLNServiceReply<MemberDetailReply> serviceReply = createHlnServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getMemberCard();
	}

	@Override
	public MemberCard getMemberCardInfoByChip(MemberCard memberCard) {
		return getMemberCardInfo(memberCard);
	}

	@Override
	public Double getMemberCardRecharge(MemberCard memberCard, String money,
			String orderNo) {
		return null;
	}

	@Override
	public Double getMemberCardRechargeByChip(MemberCard memberCard,
			String money, String orderNo) {
		return null;
	}

	@Override
	public List<SeatInfo> getDiscountPrice(TicketOrder ticketOrder,
			MemberCard memberCard) {
		DiscountPriceQuery cardInfoQuery = new DiscountPriceQuery(memberCard,
				ticketOrder);
		HLNServiceQuery<DiscountPriceQuery> serviceQuery = createHlnServiceQuery(cardInfoQuery);

		DiscountPriceReply cardInfoReply = new DiscountPriceReply();
		HLNServiceReply<DiscountPriceReply> serviceReply = createHlnServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);
		/** 设置订单销售金额以及订单明细的销售价，并设置座位折扣价 */
		List<TicketDiscountVo> ticketDiscountVos = serviceReply.getReply()
				.getTicketDiscounts();
		List<SeatInfo> seatInfoList = new ArrayList<SeatInfo>();
		StringBuilder seats = new StringBuilder();
		for (TicketDiscountVo discountVo : ticketDiscountVos) {
			if (seats.length() > 0) {
				seats.append(Chars.COMMA);
			}
			seats.append(discountVo.getSeatNo()).append(Chars.COLON)
					.append(discountVo.getTicketPrice());

			SeatInfo seatInfo = new SeatInfo();
			BeanUtils.copyFields(discountVo, seatInfo);
			seatInfoList.add(seatInfo);
		}
		ticketOrder.setSaleAmount(seats.toString());
		return seatInfoList;
	}

	@Override
	public TicketOrder memberCardPay(TicketOrder ticketOrder,
			MemberCard memberCard) {
		String orderNo = getOrderNo(ticketOrder, memberCard);
		MemberPayQuery cardInfoQuery = new MemberPayQuery(memberCard,
				ticketOrder, orderNo, settings.getUsername());
		HLNServiceQuery<MemberPayQuery> serviceQuery = createHlnServiceQuery(cardInfoQuery);

		MemberPayReply cardInfoReply = new MemberPayReply();
		HLNServiceReply<MemberPayReply> serviceReply = createHlnServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);

		return submitMemberOrder(ticketOrder, orderNo);
	}

	/**
	 * 确认会员订单
	 * 
	 * @param ticketOrder
	 *            订单对象
	 * @param orderNo
	 *            会员订单号
	 * @return 订单对象
	 */
	private TicketOrder submitMemberOrder(TicketOrder ticketOrder,
			String orderNo) {
		TicketOrder remoteTicketOrder = null;
		try {
			TicketSettings settings = ticketOrder.getCinema()
					.getTicketSettings();
			TicketSettings cinemaTicketSettings = new TicketSettings();
			BeanUtils.copyFields(settings, cinemaTicketSettings);
			if (settings.getAccessType().getModel() == AccessModel.CENTER) {
				cinemaTicketSettings.setUrl(settings.getAccessType().getUrl());
				cinemaTicketSettings.setUsername(settings.getAccessType()
						.getUsername());
				cinemaTicketSettings.setPassword(settings.getAccessType()
						.getPassword());
			}
			NgTicketService ngTicketService = new NgTicketService(
					cinemaTicketSettings);
			remoteTicketOrder = ngTicketService.submitMemberOrder(ticketOrder,
					orderNo);
		} catch (BusinessException be) {
			throw new BusinessException(
					MemberReplyError.ORDER_SUBMIT_FAILED.getValue());
		}
		return remoteTicketOrder;
	}

	/**
	 * 获取会员订单的订单号。
	 * 
	 * @param ticketOrder
	 *            订单
	 * @param memberCard
	 *            会员卡
	 * @return 返回会员订单的订单号。
	 */
	private String getOrderNo(TicketOrder ticketOrder, MemberCard memberCard) {
		DiscountPriceQuery cardInfoQuery = new DiscountPriceQuery(memberCard,
				ticketOrder);
		HLNServiceQuery<DiscountPriceQuery> serviceQuery = createHlnServiceQuery(cardInfoQuery);

		DiscountPriceReply cardInfoReply = new DiscountPriceReply();
		HLNServiceReply<DiscountPriceReply> serviceReply = createHlnServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCinemaOrderCode();
	}

	/**
	 * 调用国标网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	private void execute(HLNServiceQuery<?> serviceQuery,
			HLNServiceReply<?> serviceReply) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(settings.getUrl());
			httpPost.setConfig(getConfig());

			HttpEntity queryEntity = genQueryEntity(serviceQuery);
			httpPost.setEntity(queryEntity);

			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpClient.execute(httpPost).getEntity();
			long endTime = System.currentTimeMillis();
			log.info("接口调用[耗时:{}]", endTime - startTime);
			parseReplyEntity(replyEntity, serviceReply);
		} catch (BusinessException be) {
			log.error("调用网络售票接口失败。", be);
			throw be;
		} catch (Exception e) {
			log.error("调用网络售票接口失败。", e);
			throw new BusinessException("调用影院网络售票接口失败,请检查影院接入配置或网络。", e);
		}
	}

	/**
	 * 获取请求配置。
	 * 
	 * @return 返回设置的请求配置。
	 */
	private RequestConfig getConfig() {
		// 设置超时时间
		return RequestConfig
				.custom()
				.setSocketTimeout(
						settings.getAccessType().getSocketTimeout() * 1000)
				.setConnectTimeout(
						settings.getAccessType().getConnectTimeout() * 1000)
				.build();
	}

	/**
	 * 生成请求消息。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @return 返回生成的请求消息。
	 * @throws Exception
	 *             异常信息。
	 */
	private HttpEntity genQueryEntity(HLNServiceQuery<?> serviceQuery)
			throws Exception {
		long startTime = System.currentTimeMillis();
		SOAPMessage queryMessage = serviceQuery.getSoapMessage();
		ByteArrayOutputStream msgOut = new ByteArrayOutputStream();
		queryMessage.writeTo(msgOut);
		HttpEntity queryEntity = new ByteArrayEntity(msgOut.toByteArray());
		long endTime = System.currentTimeMillis();
		log.info("生成请求消息[耗时:{}][内容：{}]", endTime - startTime, msgOut.toString());
		return queryEntity;
	}

	/**
	 * 解析响应消息。
	 * 
	 * @param replyEntity
	 *            响应消息
	 * @param serviceReply
	 *            服务响应对象
	 * @throws Exception
	 *             异常信息。
	 */
	private void parseReplyEntity(HttpEntity replyEntity,
			HLNServiceReply<?> serviceReply) throws Exception {
		long startTime = System.currentTimeMillis();
		String replyString = EntityUtils.toString(replyEntity, Encoding.UTF_8);
		InputStream input = new ByteArrayInputStream(replyString.toString()
				.getBytes());
		SOAPMessage replyMessage = MessageFactory.newInstance(
				SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(null, input);
		long endTime = System.currentTimeMillis();
		log.info("解析响应消息[耗时:{}][内容：{}]", endTime - startTime, replyString);
		serviceReply.setSoapMessage(replyMessage);
	}

	/**
	 * 创建服务请求对象。
	 * 
	 * @param <T>
	 *            请求对象类型
	 * @param query
	 *            请求对象
	 * @return 返回服务请求对象。
	 */
	private <T extends HLNQuery> HLNServiceQuery<T> createHlnServiceQuery(
			T query) {
		return new HLNServiceQuery<T>(query);
	}

	/**
	 * 创建服务响应对象。
	 * 
	 * @param <T>
	 *            响应对象类型
	 * @param reply
	 *            响应对象
	 * @return 返回服务响应对象。
	 */
	private <T extends HLNReply> HLNServiceReply<T> createHlnServiceReply(
			T reply) {
		return new HLNServiceReply<T>(reply);
	}

	public MemberSettings getSettings() {
		return settings;
	}

	public void setSettings(MemberSettings settings) {
		this.settings = settings;
	}

	@Override
	public Boolean refundMemberTicket(TicketOrder ticketOrder,
			MemberCard memberCard) {
		boolean flag = refundment(ticketOrder);
		if (flag) {
			TicketAccessService accessService = TicketAccessServiceFactory
					.getTicketService(ticketOrder.getCinema()
							.getTicketSettings());
			return accessService.refundTicket(ticketOrder);
		}
		return flag;
	}

	/**
	 * 获取会员订单的订单号。
	 * 
	 * @param ticketOrder
	 *            订单
	 * @return 返回会员订单的订单号。
	 */
	private Boolean refundment(TicketOrder ticketOrder) {
		MemberRefundmentQuery memberRefundmentQuery = new MemberRefundmentQuery(
				ticketOrder, "02352157132477058454", settings.getUsername());
		HLNServiceQuery<MemberRefundmentQuery> serviceQuery = createHlnServiceQuery(memberRefundmentQuery);

		MemberRefundmentReply memberRefundmentReply = new MemberRefundmentReply();
		HLNServiceReply<MemberRefundmentReply> serviceReply = createHlnServiceReply(memberRefundmentReply);

		execute(serviceQuery, serviceReply);
		return true;
	}
}
