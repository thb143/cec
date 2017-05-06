package cn.mopon.cec.core.access.ticket.ng;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import cn.mopon.cec.core.access.ticket.AbstractTicketService;
import cn.mopon.cec.core.access.ticket.converter.HttpEntityUtils;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;

/**
 * NG私有票务接入接口。
 */
public class NgTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public NgTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery(cinemaCode);
		NgServiceQuery<CinemaQuery> serviceQuery = createNgServiceQuery(cinemaQuery);

		CinemaReply cinemaReply = new CinemaReply();
		NgServiceReply<CinemaReply> serviceReply = createNgServiceReply(cinemaReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCinema();
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		NgServiceQuery<SeatQuery> serviceQuery = createNgServiceQuery(seatQuery);

		SeatReply seatReply = new SeatReply();
		NgServiceReply<SeatReply> serviceReply = createNgServiceReply(seatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getHall().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery sessionQuery = new SessionQuery(cinema, startDate, endDate);
		NgServiceQuery<SessionQuery> serviceQuery = createNgServiceQuery(sessionQuery);

		SessionReply sessionReply = new SessionReply();
		NgServiceReply<SessionReply> serviceReply = createNgServiceReply(sessionReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows();
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow);
		NgServiceQuery<SessionSeatQuery> serviceQuery = createNgServiceQuery(sessionSeatQuery);

		SessionSeatReply sessionSeatReply = new SessionSeatReply();
		NgServiceReply<SessionSeatReply> serviceReply = createNgServiceReply(sessionSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShowSeats(status);
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order);
		NgServiceQuery<LockSeatQuery> serviceQuery = createNgServiceQuery(lockSeatQuery);

		LockSeatReply lockSeatReply = new LockSeatReply();
		NgServiceReply<LockSeatReply> serviceReply = createNgServiceReply(lockSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery releaseSeatQuery = new ReleaseSeatQuery(order);
		NgServiceQuery<ReleaseSeatQuery> serviceQuery = createNgServiceQuery(releaseSeatQuery);

		ReleaseSeatReply releaseSeatReply = new ReleaseSeatReply();
		NgServiceReply<ReleaseSeatReply> serviceReply = createNgServiceReply(releaseSeatReply);

		execute(serviceQuery, serviceReply);
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order);
		NgServiceQuery<SubmitOrderQuery> serviceQuery = createNgServiceQuery(submitOrderQuery);

		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		NgServiceReply<SubmitOrderReply> serviceReply = createNgServiceReply(submitOrderReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RefundTicketQuery query = new RefundTicketQuery(order);
		NgServiceQuery<RefundTicketQuery> serviceQuery = createNgServiceQuery(query);

		RefundTicketReply reply = new RefundTicketReply();
		NgServiceReply<RefundTicketReply> serviceReply = createNgServiceReply(reply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getRefunded();
	}

	@Override
	public TicketOrder queryOrder(TicketOrder ticketOrder) {
		OrderQuery query = new OrderQuery(ticketOrder);
		NgServiceQuery<OrderQuery> serviceQuery = createNgServiceQuery(query);

		OrderReply reply = new OrderReply();
		NgServiceReply<OrderReply> serviceReply = createNgServiceReply(reply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		PrintStatusQuery printStatusQuery = new PrintStatusQuery(order);
		NgServiceQuery<PrintStatusQuery> serviceQuery = createNgServiceQuery(printStatusQuery);
		PrintStatusReply printStatusReply = new PrintStatusReply();
		NgServiceReply<PrintStatusReply> serviceReply = createNgServiceReply(printStatusReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder(order);
	}

	@Override
	public boolean confirmPrint(TicketOrder order) {
		ConfirmPrintQuery confirmPrintQuery = new ConfirmPrintQuery(order);
		NgServiceQuery<ConfirmPrintQuery> serviceQuery = createNgServiceQuery(confirmPrintQuery);

		ConfirmPrintReply confirmPrintReply = new ConfirmPrintReply();
		NgServiceReply<ConfirmPrintReply> serviceReply = createNgServiceReply(confirmPrintReply);

		try {
			execute(serviceQuery, serviceReply);
		} catch (BusinessException e) {
			return false;
		}
		return true;
	}

	/**
	 * 调用国标网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	private void execute(NgServiceQuery<?> serviceQuery,
			NgServiceReply<?> serviceReply) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = settings.getUrl();
		String params = "";
		try {

			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getRequestConfig());

			params = serviceQuery.genQuery();
			HttpEntity queryEntity = new StringEntity(params, Encoding.UTF_8);
			httpPost.setEntity(queryEntity);

			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpClient.execute(httpPost).getEntity();
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
		return new NgServiceQuery<T>(query, settings.getUsername(),
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
	private <T extends NgReply> NgServiceReply<T> createNgServiceReply(T reply) {
		return new NgServiceReply<T>(reply);
	}

	/**
	 * 会员订单确认方法
	 * 
	 * @param ticketOrder
	 *            订单对象
	 * @param orderNo
	 *            会员订单号
	 * @return 确认后的订单信息
	 */
	public TicketOrder submitMemberOrder(TicketOrder ticketOrder, String orderNo) {
		MemberSubmitOrderQuery memberSubmitOrderQuery = new MemberSubmitOrderQuery(
				ticketOrder, orderNo);
		NgServiceQuery<MemberSubmitOrderQuery> serviceQuery = createNgServiceQuery(memberSubmitOrderQuery);
		MemberSubmitOrderReply memberSubmitOrderReply = new MemberSubmitOrderReply();
		NgServiceReply<MemberSubmitOrderReply> serviceReply = createNgServiceReply(memberSubmitOrderReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}
}