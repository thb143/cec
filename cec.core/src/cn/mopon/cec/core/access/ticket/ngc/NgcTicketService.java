package cn.mopon.cec.core.access.ticket.ngc;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;
import coo.base.util.BeanUtils;

/**
 * NGC网络售票接口实现。
 */
public class NgcTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public NgcTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery(cinemaCode);
		NgcServiceQuery<CinemaQuery> serviceQuery = createServiceQuery(cinemaQuery);

		CinemaReply cinemaReply = new CinemaReply();
		NgcServiceReply<CinemaReply> serviceReply = createServiceReply(cinemaReply);

		execute(serviceQuery, serviceReply);
		Cinema cinema = serviceReply.getReply().getCinema();
		cinema.setHalls(getHalls(cinema));
		return cinema;
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		NgcServiceQuery<SeatQuery> serviceQuery = createServiceQuery(seatQuery);

		SeatReply seatReply = new SeatReply();
		NgcServiceReply<SeatReply> serviceReply = createServiceReply(seatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery sessionQuery = new SessionQuery(startDate,
				cinema.getCode());
		NgcServiceQuery<SessionQuery> serviceQuery = createServiceQuery(sessionQuery);

		SessionReply sessionReply = new SessionReply();
		NgcServiceReply<SessionReply> serviceReply = createServiceReply(sessionReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows();
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow,
				status);
		NgcServiceQuery<SessionSeatQuery> serviceQuery = createServiceQuery(sessionSeatQuery);

		SessionSeatReply sessionSeatReply = new SessionSeatReply();
		NgcServiceReply<SessionSeatReply> serviceReply = createServiceReply(sessionSeatReply);

		execute(serviceQuery, serviceReply);

		return serviceReply.getReply().getShowSeats();
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order);
		NgcServiceQuery<LockSeatQuery> serviceQuery = createServiceQuery(lockSeatQuery);

		LockSeatReply lockSeatReply = new LockSeatReply();
		NgcServiceReply<LockSeatReply> serviceReply = createServiceReply(lockSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder();
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery seatQuery = new ReleaseSeatQuery(order);
		NgcServiceQuery<ReleaseSeatQuery> serviceQuery = createServiceQuery(seatQuery);

		ReleaseSeatReply sessionReply = new ReleaseSeatReply();
		NgcServiceReply<ReleaseSeatReply> serviceReply = createServiceReply(sessionReply);
		execute(serviceQuery, serviceReply);
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order);
		NgcServiceQuery<SubmitOrderQuery> serviceQuery = createServiceQuery(submitOrderQuery);

		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		NgcServiceReply<SubmitOrderReply> serviceReply = createServiceReply(submitOrderReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder(order);
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RevokeQuery revokeQuery = new RevokeQuery(order);
		NgcServiceQuery<RevokeQuery> serviceQuery = createServiceQuery(revokeQuery);

		RevokeReply revokeReply = new RevokeReply();
		NgcServiceReply<RevokeReply> serviceReply = createServiceReply(revokeReply);

		execute(serviceQuery, serviceReply);
		return true;
	}

	@Override
	public TicketOrder queryOrder(TicketOrder order) {
		OrderQuery orderQuery = new OrderQuery(order);
		NgcServiceQuery<OrderQuery> serviceQuery = createServiceQuery(orderQuery);

		OrderReply orderReply = new OrderReply();
		NgcServiceReply<OrderReply> serviceReply = createServiceReply(orderReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder();
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		PrintQuery printQuery = new PrintQuery(order);
		NgcServiceQuery<PrintQuery> serviceQuery = createServiceQuery(printQuery);

		PrintReply printReply = new PrintReply();
		NgcServiceReply<PrintReply> serviceReply = createServiceReply(printReply);

		execute(serviceQuery, serviceReply);
		TicketOrder remoteOrder = serviceReply.getReply().getTicketOrder();
		BeanUtils.copyFields(order, remoteOrder, "orderItems");
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			for (TicketOrderItem remoteOrderItem : remoteOrder.getOrderItems()) {
				if (orderItem.getSeatCode().equals(
						remoteOrderItem.getSeatCode())) {
					orderItem.setBarCode(remoteOrderItem.getBarCode());
					BeanUtils.copyFields(orderItem, remoteOrderItem);
				}
			}
		}
		return remoteOrder;
	}

	@Override
	public boolean confirmPrint(TicketOrder order) {
		ConfirmPrintQuery confirmPrintQuery = new ConfirmPrintQuery(order);
		NgcServiceQuery<ConfirmPrintQuery> serviceQuery = createServiceQuery(confirmPrintQuery);

		ConfirmPrintReply confirmPrintReply = new ConfirmPrintReply();
		NgcServiceReply<ConfirmPrintReply> serviceReply = createServiceReply(confirmPrintReply);
		try {
			execute(serviceQuery, serviceReply);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取影厅列表。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回影厅列表。
	 */
	private List<Hall> getHalls(Cinema cinema) {
		HallQuery hallQuery = new HallQuery(cinema.getCode());
		NgcServiceQuery<HallQuery> serviceQuery = createServiceQuery(hallQuery);

		HallReply hallReply = new HallReply();
		NgcServiceReply<HallReply> serviceReply = createServiceReply(hallReply);

		execute(serviceQuery, serviceReply);

		return serviceReply.getReply().getHalls();
	}

	/**
	 * 调用cec服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	protected void execute(NgcServiceQuery<?> serviceQuery,
			NgcServiceReply<?> serviceReply) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = settings.getUrl() + "/"
				+ serviceQuery.getQuery().getAction();
		List<NameValuePair> params = serviceQuery.getQuery().getParams();
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getRequestConfig());

			HttpEntity queryEntity = new UrlEncodedFormEntity(params);
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
	private <T extends NgcQuery> NgcServiceQuery<T> createServiceQuery(T query) {
		return new NgcServiceQuery<T>(query, settings.getUsername(),
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
	private <T extends NgcReply> NgcServiceReply<T> createServiceReply(T reply) {
		return new NgcServiceReply<T>(reply);
	}
}