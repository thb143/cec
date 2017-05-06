package cn.mopon.cec.core.access.member.dx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;

/**
 * 鼎新会员卡接口实现。
 */
public class DXMemberService implements MemberAccessService {
	private Logger log = LoggerFactory.getLogger(getClass());
	private MemberSettings settings;
	private String cookie;
	private String authCode;
	private String accessTokenForPay = null;

	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public DXMemberService(MemberSettings settings) {
		this.settings = settings;
	}

	@Override
	public MemberCard getVerifyCard(MemberCard memberCard) {
		return getMemberCardInfo(memberCard);
	}

	@Override
	public MemberCard getMemberCardInfo(MemberCard memberCard) {
		getAccessToken(memberCard);

		MemberDetailQuery memberCardDetailQuery = new MemberDetailQuery(
				memberCard, settings.getUsername(), accessTokenForPay);

		DXServiceQuery<MemberDetailQuery> serviceQuery = createDXServiceQuery(memberCardDetailQuery);
		MemberDetailReply memberCardDetailReply = new MemberDetailReply();
		DXServiceReply<MemberDetailReply> serviceReply = createDXServiceReply(memberCardDetailReply);

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
		getAccessToken(memberCard);

		MemberRechargeQuery memberCardRechargeQuery = new MemberRechargeQuery(
				memberCard, money, orderNo, settings.getUsername(),
				accessTokenForPay);
		DXServiceQuery<MemberRechargeQuery> serviceQuery = createDXServiceQuery(memberCardRechargeQuery);

		MemberRechargeReply memberCardRechargeReply = new MemberRechargeReply();
		DXServiceReply<MemberRechargeReply> serviceReply = createDXServiceReply(memberCardRechargeReply);

		execute(serviceQuery, serviceReply);

		return getMemberCardInfo(memberCard).getBalance();
	}

	@Override
	public List<SeatInfo> getDiscountPrice(TicketOrder ticketOrder,
			MemberCard memberCard) {
		getAccessToken(memberCard);
		DiscountPriceQuery discountPriceQuery = new DiscountPriceQuery(
				ticketOrder, settings.getUsername(), accessTokenForPay);
		DXServiceQuery<DiscountPriceQuery> serviceQuery = createDXServiceQuery(discountPriceQuery);

		DiscountPriceReply discountPriceReply = new DiscountPriceReply();
		DXServiceReply<DiscountPriceReply> serviceReply = createDXServiceReply(discountPriceReply);

		execute(serviceQuery, serviceReply);

		ticketOrder.setSaleAmount(serviceReply.getReply().getSeatInfoStr(
				ticketOrder));

		List<SeatInfo> seatInfoList = new ArrayList<SeatInfo>();
		for (TicketOrderItem item : ticketOrder.getOrderItems()) {
			SeatInfo seatInfo = new SeatInfo();
			seatInfo.setSeatNo(item.getSeatCode());
			seatInfo.setTicketPrice(item.getSalePrice());
			seatInfoList.add(seatInfo);
		}
		return seatInfoList;
	}

	@Override
	public TicketOrder memberCardPay(TicketOrder ticketOrder,
			MemberCard memberCard) {
		getAccessToken(memberCard);
		MemberPayQuery memberCardPayQuery = new MemberPayQuery(ticketOrder,
				settings.getUsername(), accessTokenForPay);
		DXServiceQuery<MemberPayQuery> serviceQuery = createDXServiceQuery(memberCardPayQuery);

		MemberPayReply memberCardPayReply = new MemberPayReply();
		DXServiceReply<MemberPayReply> serviceReply = createDXServiceReply(memberCardPayReply);
		execute(serviceQuery, serviceReply);
		TicketOrder remoteOrder = serviceReply.getReply().getTicketOrder();
		remoteOrder.setOrderItems(ticketOrder.getOrderItems());
		return remoteOrder;
	}

	/**
	 * 获取令牌。
	 * 
	 * @param memberCard
	 *            会员卡
	 */
	private void getAccessToken(MemberCard memberCard) {
		getCookieForAuthCode(memberCard);
		getAuthCodeForAccessToken(memberCard);
		accessTokenForPay = getAccessToken(authCode);
	}

	/**
	 * 获取Auth2.0 AccessToken。
	 * 
	 * @param authCode
	 *            Auth2.0 authCode
	 * @return 返回AccessToken。
	 */
	private String getAccessToken(String authCode) {
		AccessTokenQuery accessTokenQuery = new AccessTokenQuery(authCode,
				settings.getUsername());

		DXServiceQuery<AccessTokenQuery> serviceQuery = createDXServiceQuery(accessTokenQuery);

		AccessTokenReply accessTokenReply = new AccessTokenReply();
		accessTokenReply.setXml(false);
		DXServiceReply<AccessTokenReply> serviceReply = createDXServiceReply(accessTokenReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getAccessToken();
	}

	/**
	 * 获取Auth2.0 Code。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 */
	private void getAuthCodeForAccessToken(MemberCard memberCard) {
		AuthCodeQuery authCodeQuery = new AuthCodeQuery(
				memberCard.getCardCode(), memberCard.getPassword(),
				settings.getUsername(), memberCard.getCinemaCode());
		DXServiceQuery<AuthCodeQuery> serviceQuery = createDXServiceQuery(authCodeQuery);

		AuthCodeReply authCodeReply = new AuthCodeReply();
		authCodeReply.setXml(false);
		DXServiceReply<AuthCodeReply> serviceReply = createDXServiceReply(authCodeReply);
		execute(serviceQuery, serviceReply);
	}

	/**
	 * 获取登录验证通过后的cookie。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 */
	private void getCookieForAuthCode(MemberCard memberCard) {
		CookieQuery cookieQuery = new CookieQuery(memberCard.getCinemaCode(),
				settings.getUsername(), memberCard.getCardCode(),
				memberCard.getPassword());
		DXServiceQuery<CookieQuery> serviceQuery = createDXServiceQuery(cookieQuery);

		CookieReply cookieReply = new CookieReply();
		cookieReply.setXml(false);
		DXServiceReply<CookieReply> serviceReply = createDXServiceReply(cookieReply);
		execute(serviceQuery, serviceReply);
		if (!"success".equals(serviceReply.getReply().getStatus())) {
			throw new BusinessException(
					MemberReplyError.MEMBER_NOT_EXIST.getValue());
		}
	}

	/**
	 * 调用鼎新会员卡服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	protected void execute(DXServiceQuery<?> serviceQuery,
			DXServiceReply<?> serviceReply) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(settings.getUrl() + "/"
				+ serviceQuery.getQuery().getAction());
		log.info("接口地址[{}]", settings.getUrl() + "/"
				+ serviceQuery.getQuery().getAction());
		try {
			httpPost.addHeader("Cookie", cookie);
			httpPost.setConfig(getConfig());
			httpPost.setEntity(new UrlEncodedFormEntity(serviceQuery.getQuery()
					.getParams()));
			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(httpPost);
			if (response.getFirstHeader("Set-Cookie") != null) {
				cookie = response.getFirstHeader("Set-Cookie").getValue();
				cookie = cookie.substring(0, cookie.lastIndexOf(";"));
				log.info("鼎新验证登录Cookie信息：" + cookie);
			}
			Header locationHeader = response.getFirstHeader("Location");
			if (locationHeader != null
					&& StringUtils
							.contains(locationHeader.getValue(), "&code=")) {
				authCode = locationHeader.getValue().substring(
						locationHeader.getValue().lastIndexOf("code=") + 5,
						locationHeader.getValue().length());
				log.info("鼎新AuthCode信息：" + authCode);
			}
			long endTime = System.currentTimeMillis();
			log.info("接口调用[耗时:{}]", endTime - startTime);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String line = null;
			StringBuilder resp = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				resp.append(line);
			}
			reader.close();
			log.info("接口返回的XML[{}]", resp.toString());
			if (serviceReply.getReply().isXml()) {
				serviceReply.genReplyMessageByXml(resp.toString());
			} else {
				if (!StringUtils.isBlank(resp.toString())) {
					String json = resp.toString();
					if (json.charAt(1) == '('
							&& json.charAt(json.length() - 1) == ')') {
						json = json.substring(2, json.length() - 1);
					}
					serviceReply.genReplyMessageByJson(json);
				}
			}
		} catch (BusinessException be) {
			log.error("调用鼎新会员卡接口失败。", be);
			throw be;
		} catch (Exception e) {
			log.error("调用鼎新会员卡接口失败。", e);
			throw new UncheckedException("调用鼎新会员卡接口失败,请检查会员卡接入配置或网络。", e);
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
	private <T extends DXQuery> DXServiceQuery<T> createDXServiceQuery(T query) {
		return new DXServiceQuery<T>(query, settings.getPassword());
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
	private <T extends DXReply> DXServiceReply<T> createDXServiceReply(T reply) {
		return new DXServiceReply<T>(reply);
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
		TicketAccessService accessService = TicketAccessServiceFactory
				.getTicketService(ticketOrder.getCinema().getTicketSettings());
		return accessService.refundTicket(ticketOrder);
	}
}
