package cn.mopon.cec.core.access.member.mtx;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.access.member.MemberAccessService;
import cn.mopon.cec.core.access.member.enums.MemberReplyError;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.access.ticket.mtx.MtxTicketService;
import cn.mopon.cec.core.access.ticket.mtx.vo.ShowVo;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.AccessModel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.base.exception.BusinessException;
import coo.base.util.BeanUtils;
import coo.base.util.NumberUtils;
import coo.core.xstream.GenericXStream;

/**
 * 满天星会员卡接口实现。
 */
public class MTXMemberService implements MemberAccessService {
	private Logger log = LoggerFactory.getLogger(getClass());
	private MemberSettings settings;
	@XStreamOmitField
	protected XStream xstream = new GenericXStream();

	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            会员卡设置
	 */
	public MTXMemberService(MemberSettings settings) {
		this.settings = settings;

	}

	@Override
	public MemberCard getVerifyCard(MemberCard memberCard) {
		MemberVerifyQuery loginCardQuery = new MemberVerifyQuery(memberCard,
				settings);
		MTXServiceQuery<MemberVerifyQuery> serviceQuery = createMTXServiceQuery(loginCardQuery);
		MemberVerifyReply loginCardReply = new MemberVerifyReply();
		MTXServiceReply<MemberVerifyReply> serviceReply = createMTXServiceReply(loginCardReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCard();
	}

	@Override
	public MemberCard getMemberCardInfo(MemberCard memberCard) {
		MemberDetailQuery cardInfoQuery = new MemberDetailQuery(memberCard,
				settings);
		MTXServiceQuery<MemberDetailQuery> serviceQuery = createMTXServiceQuery(cardInfoQuery);
		MemberDetailReply cardInfoReply = new MemberDetailReply();
		MTXServiceReply<MemberDetailReply> serviceReply = createMTXServiceReply(cardInfoReply);
		try {
			execute(serviceQuery, serviceReply);
		} catch (BusinessException be) {
			throw new BusinessException(
					MemberReplyError.MEMBER_NOT_EXIST.getValue());
		}
		return serviceReply.getReply().getCard();
	}

	@Override
	public MemberCard getMemberCardInfoByChip(MemberCard memberCard) {
		MemberDetailByChipQuery cardInfoQuery = new MemberDetailByChipQuery(
				memberCard, settings);
		MTXServiceQuery<MemberDetailByChipQuery> serviceQuery = createMTXServiceQuery(cardInfoQuery);
		MemberDetailByChipReply cardInfoReply = new MemberDetailByChipReply();
		MTXServiceReply<MemberDetailByChipReply> serviceReply = createMTXServiceReply(cardInfoReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCard();
	}

	@Override
	public Double getMemberCardRecharge(MemberCard memberCard, String price,
			String partnerDepositCode) {
		MemberRechargeQuery cardRechargeQuery = new MemberRechargeQuery(
				memberCard, price, settings);
		MTXServiceQuery<MemberRechargeQuery> serviceQuery = createMTXServiceQuery(cardRechargeQuery);
		MemberRechargeReply cardRechargeReply = new MemberRechargeReply();
		MTXServiceReply<MemberRechargeReply> serviceReply = createMTXServiceReply(cardRechargeReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCard().getBalance();
	}

	@Override
	public Double getMemberCardRechargeByChip(MemberCard memberCard,
			String price, String orderNo) {
		MemberRechargeByChipQuery cardRechargeQuery = new MemberRechargeByChipQuery(
				memberCard, price, settings);
		MTXServiceQuery<MemberRechargeByChipQuery> serviceQuery = createMTXServiceQuery(cardRechargeQuery);
		MemberRechargeByChipReply cardRechargeReply = new MemberRechargeByChipReply();
		MTXServiceReply<MemberRechargeByChipReply> serviceReply = createMTXServiceReply(cardRechargeReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCard().getBalance();
	}

	@Override
	public List<SeatInfo> getDiscountPrice(TicketOrder order,
			MemberCard memberCard) {
		getMemberCardInfo(memberCard);
		// 设置订单排期号为会员排期号
		setShowCode(order);
		DiscountPriceQuery cardDiscountQuery = new DiscountPriceQuery(order,
				memberCard.getCardCode(), settings);
		MTXServiceQuery<DiscountPriceQuery> serviceQuery = createMTXServiceQuery(cardDiscountQuery);
		DiscountPriceReply cardDiscountReply = new DiscountPriceReply();
		MTXServiceReply<DiscountPriceReply> serviceReply = createMTXServiceReply(cardDiscountReply);
		MemberCard card = new MemberCard();
		execute(serviceQuery, serviceReply);
		card = serviceReply.getReply().getCard();
		if ("0".equals(serviceReply.getReply().getDiscountType())) {
			card.setDiscountPrice(NumberUtils.halfUp(order.getStdPrice()
					* card.getDiscountPrice() / 10.0));
		} else if ("1".equals(serviceReply.getReply().getDiscountType())) {
			card.setDiscountPrice(card.getDiscountPrice());
		}
		List<SeatInfo> seatInfoList = new ArrayList<SeatInfo>();
		for (TicketOrderItem item : order.getOrderItems()) {
			SeatInfo seatInfo = new SeatInfo();
			seatInfo.setSeatNo(item.getSeatCode());
			seatInfo.setTicketPrice(card.getDiscountPrice());
			seatInfoList.add(seatInfo);
		}
		return seatInfoList;
	}

	/**
	 * 设置排期编码。
	 * 
	 * @param order
	 *            选座票订单
	 */
	private void setShowCode(TicketOrder order) {
		TicketSettings cinemaTicketSettings = new TicketSettings();
		TicketSettings ticketSettings = order.getCinema().getTicketSettings();
		BeanUtils.copyFields(ticketSettings, cinemaTicketSettings);
		if (order.getCinema().getTicketSettings().getAccessType().getModel() == AccessModel.CENTER) {
			cinemaTicketSettings
					.setUrl(ticketSettings.getAccessType().getUrl());
			cinemaTicketSettings.setUsername(ticketSettings.getAccessType()
					.getUsername());
			cinemaTicketSettings.setPassword(ticketSettings.getAccessType()
					.getPassword());
		}
		MtxTicketService ticketService = new MtxTicketService(
				cinemaTicketSettings);
		List<ShowVo> shows = ticketService.getFeatureInfo(order);
		for (ShowVo show : shows) {
			if (show.getFeatureAppNo().equals(order.getShowCode())) {
				order.setShowCode(show.getFeatureNo());
				break;
			}
		}
	}

	@Override
	public TicketOrder memberCardPay(TicketOrder order, MemberCard memberCard) {
		getMemberCardInfo(memberCard);
		MemberPayQuery cardPayQuery = new MemberPayQuery(order, memberCard,
				settings);
		MTXServiceQuery<MemberPayQuery> serviceQuery = createMTXServiceQuery(cardPayQuery);
		MemberPayReply cardPayReply = new MemberPayReply();
		MTXServiceReply<MemberPayReply> serviceReply = createMTXServiceReply(cardPayReply);

		try {
			execute(serviceQuery, serviceReply);
		} catch (BusinessException be) {
			if ("1".equals(serviceReply.getReply().getCode())) {
				throw new BusinessException(
						MemberReplyError.MEMBER_LACK_BALANCE.getValue());
			}
			throw new BusinessException(be.getMessage());
		}
		TicketAccessService ticketAccessService = TicketAccessServiceFactory
				.getTicketService(order.getCinema().getTicketSettings());
		TicketOrder ticketOrder = null;
		try {
			ticketOrder = ticketAccessService.submitOrder(order);
		} catch (BusinessException be) {
			log.error("会员卡扣款成功，确认订单时发生异常。", be);
			throw new BusinessException(
					MemberReplyError.ORDER_SUBMIT_FAILED.getValue());
		}
		return ticketOrder;
	}

	/**
	 * 调用漫天星网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	private void execute(MTXServiceQuery<?> serviceQuery,
			MTXServiceReply<?> serviceReply) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(settings.getUrl());

			HttpEntity queryEntity = genQueryEntity(serviceQuery);
			httpPost.setEntity(queryEntity);
			httpPost.setConfig(getConfig());
			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpClient.execute(httpPost).getEntity();
			long endTime = System.currentTimeMillis();
			log.info("接口调用[耗时:{}]", endTime - startTime);
			parseReplyEntity(replyEntity, serviceReply);
		} catch (BusinessException be) {
			log.error("调用满天星接口失败。", be);
			throw be;
		} catch (Exception e) {
			log.error("调用满天星接口失败。", e);
			throw new BusinessException("调用会员卡接口失败,请检查影院接入配置或网络。", e);
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
	private HttpEntity genQueryEntity(MTXServiceQuery<?> serviceQuery)
			throws Exception {
		long startTime = System.currentTimeMillis();
		SOAPMessage queryMessage = serviceQuery.getSoapMessage();
		SOAPElement bodyElement = (SOAPElement) queryMessage.getSOAPBody()
				.getChildElements().next();
		bodyElement.removeAttribute("xmlns:ns1");
		bodyElement.addNamespaceDeclaration("ns1",
				"http://webservice.pay.cmts.cn");
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
			MTXServiceReply<?> serviceReply) throws Exception {
		long startTime = System.currentTimeMillis();
		SOAPMessage replyMessage = MessageFactory.newInstance(
				SOAPConstants.SOAP_1_1_PROTOCOL).createMessage(null,
				replyEntity.getContent());
		String replyMsg = serviceReply.setSoapMessage(replyMessage);
		long endTime = System.currentTimeMillis();
		log.info("解析响应消息[耗时:{}][内容：{}]", endTime - startTime,
				replyMsg.toString());
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
	private <T extends MTXQuery> MTXServiceQuery<T> createMTXServiceQuery(
			T query) {
		return new MTXServiceQuery<T>(query, settings.getUsername());
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
	private <T extends MTXReply> MTXServiceReply<T> createMTXServiceReply(
			T reply) {
		return new MTXServiceReply<T>(reply);
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

	@Override
	public Boolean refundMemberTicket(TicketOrder ticketOrder,
			MemberCard memberCard) {
		TicketAccessService accessService = TicketAccessServiceFactory
				.getTicketService(ticketOrder.getCinema().getTicketSettings());
		boolean flag = accessService.refundTicket(ticketOrder);
		if (flag) {
			getMemberCardRecharge(memberCard, getSumPrice(ticketOrder), "");
		}
		return flag;
	}

	/**
	 * 获取订单总的票房价。
	 * 
	 * @param ticketOrder
	 *            订单
	 * @return 返回总的票房价。
	 */
	private String getSumPrice(TicketOrder ticketOrder) {
		double sumPrice = 0d;
		for (TicketOrderItem order : ticketOrder.getOrderItems()) {
			sumPrice += order.getSubmitPrice();
		}
		return sumPrice + "";
	}
}
