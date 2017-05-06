package cn.mopon.cec.core.access.member.ng;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import cn.mopon.cec.core.access.member.AbstractMemberService;
import cn.mopon.cec.core.access.member.ng.vo.NgSeatInfo;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.access.ticket.converter.HttpEntityUtils;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;

/**
 * 鼎新网络售票接口实现。
 */
public class NgMemberService extends AbstractMemberService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public NgMemberService(MemberSettings settings) {
		super(settings);
	}

	@Override
	public MemberCard getMemberCardInfo(MemberCard memberCard) {
		CardInfoQuery query = new CardInfoQuery(memberCard,
				settings.getPassword());
		NgServiceQuery<CardInfoQuery> serviceQuery = createNgServiceQuery(query);

		CardInfoReply cardInfoReply = new CardInfoReply();
		NgServiceReply<CardInfoReply> serviceReply = createNgServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getData();
	}

	@Override
	public MemberCard getMemberCardInfoByChip(MemberCard memberCard) {
		CardInfoQuery query = new CardInfoQuery(memberCard,
				settings.getPassword());
		NgServiceQuery<CardInfoQuery> serviceQuery = createNgServiceQuery(query);

		CardInfoReply cardInfoReply = new CardInfoReply();
		NgServiceReply<CardInfoReply> serviceReply = createNgServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getData();
	}

	@Override
	public MemberCard getVerifyCard(MemberCard memberCard) {
		CardInfoQuery query = new CardInfoQuery(memberCard,
				settings.getPassword());
		NgServiceQuery<CardInfoQuery> serviceQuery = createNgServiceQuery(query);

		CardInfoReply cardInfoReply = new CardInfoReply();
		NgServiceReply<CardInfoReply> serviceReply = createNgServiceReply(cardInfoReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getData();
	}

	@Override
	public List<SeatInfo> getDiscountPrice(TicketOrder ticketOrder,
			MemberCard memberCard) {
		PriceQuery query = new PriceQuery(memberCard,
				ticketOrder.getCinemaOrderCode(), settings.getPassword());
		NgServiceQuery<PriceQuery> serviceQuery = createNgServiceQuery(query);

		PriceReply priceReply = new PriceReply();
		NgServiceReply<PriceReply> serviceReply = createNgServiceReply(priceReply);
		execute(serviceQuery, serviceReply);
		List<NgSeatInfo> ngSeatInfos = serviceReply.getReply().getData()
				.getData();
		List<SeatInfo> seatInfos = new ArrayList<>();
		for (NgSeatInfo ngSeatInfo : ngSeatInfos) {
			SeatInfo seatInfo = new SeatInfo(ngSeatInfo);
			seatInfos.add(seatInfo);
		}
		return seatInfos;
	}

	@Override
	public Double getMemberCardRecharge(MemberCard memberCard, String money,
			String channelOrderNo) {
		CardRechargeQuery query = new CardRechargeQuery(memberCard, money,
				channelOrderNo, channelOrderNo, settings.getPassword());
		NgServiceQuery<CardRechargeQuery> serviceQuery = createNgServiceQuery(query);
		CardRechargeReply reply = new CardRechargeReply();
		NgServiceReply<CardRechargeReply> serviceReply = createNgServiceReply(reply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getData().getCardBalance();
	}

	@Override
	public Boolean refundMemberTicket(TicketOrder ticketOrder,
			MemberCard memberCard) {
		RefundTicketQuery query = new RefundTicketQuery(ticketOrder,
				memberCard, settings.getPassword());
		NgServiceQuery<RefundTicketQuery> serviceQuery = createNgServiceQuery(query);

		RefundTicketReply reply = new RefundTicketReply();
		NgServiceReply<RefundTicketReply> serviceReply = createNgServiceReply(reply);
		execute(serviceQuery, serviceReply);
		return true;
	}

	@Override
	public TicketOrder memberCardPay(TicketOrder order, MemberCard memberCard) {
		ConfirmOrderQuery query = new ConfirmOrderQuery(order, memberCard,
				settings.getPassword());
		NgServiceQuery<ConfirmOrderQuery> serviceQuery = createNgServiceQuery(query);

		ConfirmOrderReply reply = new ConfirmOrderReply();
		NgServiceReply<ConfirmOrderReply> serviceReply = createNgServiceReply(reply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getData().getOrder();
	}

	// @Override
	// public List<MemberOrder> queryOrders(MemberCard memberCard,
	// String startDate, String endDate) {
	// OrdersQuery query = new OrdersQuery(memberCard, startDate, endDate,
	// settings.getPassword());
	// NgServiceQuery<OrdersQuery> serviceQuery = createNgServiceQuery(query);
	//
	// OrdersReply reply = new OrdersReply();
	// NgServiceReply<OrdersReply> serviceReply = createNgServiceReply(reply);
	// execute(serviceQuery, serviceReply);
	// return serviceReply.getReply().getData().getOrders();
	// }

	/**
	 * 调用鼎新网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	protected void execute(NgServiceQuery<?> serviceQuery,
			NgServiceReply<?> serviceReply) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url = settings.getUrl() + "/"
				+ serviceQuery.getQuery().getAction();
		List<NameValuePair> params = serviceQuery.getQuery().getParams();
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getRequestConfig());

			HttpEntity queryEntity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(queryEntity);

			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpclient.execute(httpPost).getEntity();
			long endTime = System.currentTimeMillis();
			String replyString = HttpEntityUtils.getString(replyEntity);
			logReply(url, params, replyString, endTime - startTime);

			serviceReply.genReply(replyString);
		} catch (BusinessException be) {
			logError(url, params, be);
			throw be;
		} catch (Exception e) {
			logError(url, params, e);
			throw new UncheckedException("调用影院网络售票接口发生异常。", e);
		}
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
	private <T extends NgQuery> NgServiceQuery<T> createNgServiceQuery(T query) {
		return new NgServiceQuery<T>(query, settings);
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
	private <T extends NgReply> NgServiceReply<T> createNgServiceReply(T reply) {
		return new NgServiceReply<T>(reply);
	}

	@Override
	public Double getMemberCardRechargeByChip(MemberCard memberCard,
			String money, String orderNo) {
		// TODO Auto-generated method stub
		return null;
	}

}