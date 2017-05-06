package cn.mopon.cec.core.access.ticket.std;

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
 * 国标票务接入接口。
 */
public class StdTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public StdTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery(cinemaCode);
		StdServiceQuery<CinemaQuery> serviceQuery = createStdServiceQuery(cinemaQuery);

		CinemaReply cinemaReply = new CinemaReply();
		StdServiceReply<CinemaReply> serviceReply = createStdServiceReply(cinemaReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCinema();
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		StdServiceQuery<SeatQuery> serviceQuery = createStdServiceQuery(seatQuery);

		SeatReply seatReply = new SeatReply();
		StdServiceReply<SeatReply> serviceReply = createStdServiceReply(seatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getHall().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery sessionQuery = new SessionQuery(cinema, startDate, endDate);
		StdServiceQuery<SessionQuery> serviceQuery = createStdServiceQuery(sessionQuery);

		SessionReply sessionReply = new SessionReply();
		StdServiceReply<SessionReply> serviceReply = createStdServiceReply(sessionReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows();
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow);
		StdServiceQuery<SessionSeatQuery> serviceQuery = createStdServiceQuery(sessionSeatQuery);

		SessionSeatReply sessionSeatReply = new SessionSeatReply();
		StdServiceReply<SessionSeatReply> serviceReply = createStdServiceReply(sessionSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShowSeats(status);
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order);
		StdServiceQuery<LockSeatQuery> serviceQuery = createStdServiceQuery(lockSeatQuery);

		LockSeatReply lockSeatReply = new LockSeatReply();
		StdServiceReply<LockSeatReply> serviceReply = createStdServiceReply(lockSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery releaseSeatQuery = new ReleaseSeatQuery(order);
		StdServiceQuery<ReleaseSeatQuery> serviceQuery = createStdServiceQuery(releaseSeatQuery);

		ReleaseSeatReply releaseSeatReply = new ReleaseSeatReply();
		StdServiceReply<ReleaseSeatReply> serviceReply = createStdServiceReply(releaseSeatReply);

		execute(serviceQuery, serviceReply);
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order);
		StdServiceQuery<SubmitOrderQuery> serviceQuery = createStdServiceQuery(submitOrderQuery);

		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		StdServiceReply<SubmitOrderReply> serviceReply = createStdServiceReply(submitOrderReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RefundTicketQuery query = new RefundTicketQuery(order);
		StdServiceQuery<RefundTicketQuery> serviceQuery = createStdServiceQuery(query);

		RefundTicketReply reply = new RefundTicketReply();
		StdServiceReply<RefundTicketReply> serviceReply = createStdServiceReply(reply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getRefunded();
	}

	@Override
	public TicketOrder queryOrder(TicketOrder ticketOrder) {
		OrderQuery query = new OrderQuery(ticketOrder);
		StdServiceQuery<OrderQuery> serviceQuery = createStdServiceQuery(query);

		OrderReply reply = new OrderReply();
		StdServiceReply<OrderReply> serviceReply = createStdServiceReply(reply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder();
	}

	@Override
	public TicketOrder queryPrint(TicketOrder ticketOrder) {
		PrintStatusQuery printStatusQuery = new PrintStatusQuery(ticketOrder);
		StdServiceQuery<PrintStatusQuery> serviceQuery = createStdServiceQuery(printStatusQuery);
		PrintStatusReply printStatusReply = new PrintStatusReply();
		StdServiceReply<PrintStatusReply> serviceReply = createStdServiceReply(printStatusReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getOrder(ticketOrder);
	}

	@Override
	public boolean confirmPrint(TicketOrder order) {
		throw new BusinessException("该接入商不支持打票。");
	}

	/**
	 * 调用国标网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	private void execute(StdServiceQuery<?> serviceQuery,
			StdServiceReply<?> serviceReply) {
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
	private <T extends StdQuery> StdServiceQuery<T> createStdServiceQuery(
			T query) {
		return new StdServiceQuery<T>(query, settings.getUsername(),
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
	private <T extends StdReply> StdServiceReply<T> createStdServiceReply(
			T reply) {
		return new StdServiceReply<T>(reply);
	}
}