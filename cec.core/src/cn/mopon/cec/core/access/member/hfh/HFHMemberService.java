package cn.mopon.cec.core.access.member.hfh;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import cn.mopon.cec.core.access.member.MemberAccessService;
import cn.mopon.cec.core.access.member.hfh.vo.TicketDiscountVo;
import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;

/**
 * 火凤凰会员卡接口实现。
 */
public class HFHMemberService implements MemberAccessService {
	private Logger log = LoggerFactory.getLogger(getClass());
	private MemberSettings settings;

	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            会员卡设置
	 */
	public HFHMemberService(MemberSettings settings) {
		this.settings = settings;
	}

	@Override
	public MemberCard getVerifyCard(MemberCard memberCard) {
		return getMemberCardInfo(memberCard);
	}

	@Override
	public MemberCard getMemberCardInfo(MemberCard memberCard) {
		MemberDetailQuery memberCardDetailQuery = new MemberDetailQuery(
				memberCard);
		HFHServiceQuery<MemberDetailQuery> serviceQuery = createHFHServiceQuery(memberCardDetailQuery);

		MemberDetailReply memberCardDetailReply = new MemberDetailReply();
		HFHServiceReply<MemberDetailReply> serviceReply = createHFHServiceReply(memberCardDetailReply);

		execute(serviceQuery, serviceReply);

		return serviceReply.getReply().getMemberCard();
	}

	@Override
	public MemberCard getMemberCardInfoByChip(MemberCard memberCard) {
		return null;
	}

	@Override
	public Double getMemberCardRecharge(MemberCard memberCard, String money,
			String orderNo) {
		String token = getToken();
		MemberRechargeQuery memberCardRechargeQuery = new MemberRechargeQuery(
				token, memberCard, money);
		HFHServiceQuery<MemberRechargeQuery> serviceQuery = createHFHServiceQuery(memberCardRechargeQuery);
		MemberRechargeReply memberCardRechargeReply = new MemberRechargeReply();
		HFHServiceReply<MemberRechargeReply> serviceReply = createHFHServiceReply(memberCardRechargeReply);

		execute(serviceQuery, serviceReply);

		return serviceReply.getReply().getBalance();
	}

	/**
	 * 获取座位信息列表。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @param memberCard
	 *            会员卡
	 * @return 返回座位信息列表。
	 */
	@Override
	public List<SeatInfo> getDiscountPrice(TicketOrder ticketOrder,
			MemberCard memberCard) {
		List<TicketDiscountVo> ticketDiscounts = getTicketDiscount(ticketOrder,
				memberCard.getCardCode());
		Integer ticketCount = ticketOrder.getTicketCount();

		List<Double> price = new ArrayList<Double>();
		// 价格从低到高升序（价格越低，说明优惠越大）
		for (TicketDiscountVo ticketDiscount : ticketDiscounts) {
			// 为0时，购票数量不受限制 或者 需要购买的票数小于等于可购买票数
			if (ticketDiscount.getCount() == 0
					|| ticketCount - ticketDiscount.getCount() <= 0) {
				for (int i = 1; i <= ticketCount; i++) {
					price.add(ticketDiscount.getDiscountPrice());
				}
				break;
			}
			// 最多买N张票 需要购买的票数大于可购买票数
			ticketCount = ticketCount - ticketDiscount.getCount();
			for (int i = 1; i <= ticketDiscount.getCount(); i++) {
				price.add(ticketDiscount.getDiscountPrice());
			}
		}
		// 获取票价变量
		int num = 0;
		List<SeatInfo> seatInfoList = new ArrayList<SeatInfo>();
		for (TicketOrderItem item : ticketOrder.getOrderItems()) {
			SeatInfo seatInfo = new SeatInfo();
			seatInfo.setSeatNo(item.getSeatCode());
			seatInfo.setTicketPrice(price.get(num));
			seatInfoList.add(seatInfo);
			num++;
		}
		return seatInfoList;
	}

	@Override
	public TicketOrder memberCardPay(TicketOrder ticketOrder,
			MemberCard memberCard) {
		MemberPayQuery memberCardPayQuery = new MemberPayQuery(ticketOrder,
				memberCard);
		HFHServiceQuery<MemberPayQuery> serviceQuery = createHFHServiceQuery(memberCardPayQuery);

		MemberPayReply memberCardPayReply = new MemberPayReply();
		HFHServiceReply<MemberPayReply> serviceReply = createHFHServiceReply(memberCardPayReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder();
	}

	/**
	 * 获取令牌。
	 * 
	 * @return 返回令牌。
	 */
	private String getToken() {
		TokenQuery tokenQuery = new TokenQuery();
		HFHServiceQuery<TokenQuery> serviceQuery = createHFHServiceQuery(tokenQuery);
		TokenReply tokenReply = new TokenReply();
		HFHServiceReply<TokenReply> serviceReply = createHFHServiceReply(tokenReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getToken();
	}

	/**
	 * 获取会员卡优惠列表。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @param cardCode
	 *            会员卡号
	 * @return 返回会员卡优惠列表。
	 */
	private List<TicketDiscountVo> getTicketDiscount(TicketOrder ticketOrder,
			String cardCode) {
		DiscountPriceQuery discountPriceQuery = new DiscountPriceQuery(
				ticketOrder, cardCode);
		HFHServiceQuery<DiscountPriceQuery> serviceQuery = createHFHServiceQuery(discountPriceQuery);
		DiscountPriceReply discountPriceReply = new DiscountPriceReply();
		HFHServiceReply<DiscountPriceReply> serviceReply = createHFHServiceReply(discountPriceReply);
		execute(serviceQuery, serviceReply);
		List<TicketDiscountVo> ticketDiscounts = serviceReply.getReply()
				.getTicketDiscounts();
		Collections.sort(ticketDiscounts, new Comparator<TicketDiscountVo>() {
			/**
			 * 参数比较。
			 * 
			 * @param arg0
			 *            参数1
			 * @param arg1
			 *            参数2
			 * @return 如果参数1位于参数2之前，返回负值，如果两个参数在排序中位置相同，则返回 0 ，否则，则返回正值。
			 */
			public int compare(TicketDiscountVo arg0, TicketDiscountVo arg1) {
				return arg0.getDiscountPrice().compareTo(
						arg1.getDiscountPrice());
			}
		});
		return ticketDiscounts;
	}

	/**
	 * 调用火凤凰会员卡服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	protected void execute(HFHServiceQuery<?> serviceQuery,
			HFHServiceReply<?> serviceReply) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(settings.getUrl() + "/"
				+ serviceQuery.getQuery().getMethod());
		try {
			httpPost.setConfig(getConfig());
			httpPost.setEntity(new UrlEncodedFormEntity(serviceQuery.getQuery()
					.getNvps()));
			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpclient.execute(httpPost).getEntity();
			long endTime = System.currentTimeMillis();
			log.info("接口调用[耗时:{}]", endTime - startTime);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					replyEntity.getContent(), "UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			log.info("接口响应消息[{}]", sb);
			reader.close();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			ByteArrayInputStream input = new ByteArrayInputStream(sb.toString()
					.getBytes(Encoding.UTF_8));
			Document document = builder.parse(input);
			String str = serviceReply.genReplyMessage(document.getFirstChild()
					.getTextContent());
			log.info("接口返回[耗时:{}]", str);
		} catch (BusinessException be) {
			log.error("调用火凤凰会员卡接口失败。", be);
			throw be;
		} catch (Exception e) {
			log.error("调用火凤凰会员卡接口失败。", e);
			throw new UncheckedException("调用火凤凰会员卡接口失败,请检查会员卡接入配置或网络。", e);
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
	 * 创建服务请求对象。
	 * 
	 * @param <T>
	 *            请求对象类型
	 * @param query
	 *            请求对象
	 * @return 返回服务请求对象。
	 */
	private <T extends HFHQuery> HFHServiceQuery<T> createHFHServiceQuery(
			T query) {
		if (query.getNeedCinemaParams()) {
			query.genCinemaParams(settings.getCinema());
		}
		return new HFHServiceQuery<T>(query, settings.getUsername(),
				settings.getPassword());
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
	private <T extends HFHReply> HFHServiceReply<T> createHFHServiceReply(
			T reply) {
		return new HFHServiceReply<T>(reply);
	}

	public MemberSettings getSettings() {
		return settings;
	}

	public void setSettings(MemberSettings settings) {
		this.settings = settings;
	}

	@Override
	public Double getMemberCardRechargeByChip(MemberCard memberCard,
			String money, String orderNo) {
		return null;
	}

	@Override
	public Boolean refundMemberTicket(TicketOrder ticketOrder,
			MemberCard memberCard) {
		return null;
	}
}
