package cn.mopon.cec.core.access.ticket.yxt;

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
 * YXT网络售票接口实现。
 */
public class YxtTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public YxtTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery(cinemaCode);
		YxtServiceQuery<CinemaQuery> serviceQuery = createServiceQuery(cinemaQuery);

		CinemaReply cinemaReply = new CinemaReply();
		YxtServiceReply<CinemaReply> serviceReply = createServiceReply(cinemaReply);

		execute(serviceQuery, serviceReply);
		Cinema cinema = serviceReply.getReply().getCinema();
		cinema.setHalls(getHalls(cinema));
		return cinema;
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		YxtServiceQuery<SeatQuery> serviceQuery = createServiceQuery(seatQuery);

		SeatReply seatReply = new SeatReply();
		YxtServiceReply<SeatReply> serviceReply = createServiceReply(seatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery sessionQuery = new SessionQuery(startDate,
				cinema.getCode());
		YxtServiceQuery<SessionQuery> serviceQuery = createServiceQuery(sessionQuery);

		SessionReply sessionReply = new SessionReply();
		YxtServiceReply<SessionReply> serviceReply = createServiceReply(sessionReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows();
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow,
				status);
		YxtServiceQuery<SessionSeatQuery> serviceQuery = createServiceQuery(sessionSeatQuery);

		SessionSeatReply sessionSeatReply = new SessionSeatReply();
		YxtServiceReply<SessionSeatReply> serviceReply = createServiceReply(sessionSeatReply);

		execute(serviceQuery, serviceReply);

		return serviceReply.getReply().getShowSeats();
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order);
		YxtServiceQuery<LockSeatQuery> serviceQuery = createServiceQuery(lockSeatQuery);

		LockSeatReply lockSeatReply = new LockSeatReply();
		YxtServiceReply<LockSeatReply> serviceReply = createServiceReply(lockSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder();
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery seatQuery = new ReleaseSeatQuery(order);
		YxtServiceQuery<ReleaseSeatQuery> serviceQuery = createServiceQuery(seatQuery);

		ReleaseSeatReply sessionReply = new ReleaseSeatReply();
		YxtServiceReply<ReleaseSeatReply> serviceReply = createServiceReply(sessionReply);
		execute(serviceQuery, serviceReply);
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order);
		YxtServiceQuery<SubmitOrderQuery> serviceQuery = createServiceQuery(submitOrderQuery);

		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		YxtServiceReply<SubmitOrderReply> serviceReply = createServiceReply(submitOrderReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder(order);
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RevokeQuery revokeQuery = new RevokeQuery(order);
		YxtServiceQuery<RevokeQuery> serviceQuery = createServiceQuery(revokeQuery);

		RevokeReply revokeReply = new RevokeReply();
		YxtServiceReply<RevokeReply> serviceReply = createServiceReply(revokeReply);

		execute(serviceQuery, serviceReply);
		return true;
	}

	@Override
	public TicketOrder queryOrder(TicketOrder order) {
		OrderQuery orderQuery = new OrderQuery(order);
		YxtServiceQuery<OrderQuery> serviceQuery = createServiceQuery(orderQuery);

		OrderReply orderReply = new OrderReply();
		YxtServiceReply<OrderReply> serviceReply = createServiceReply(orderReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder();
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		PrintQuery printQuery = new PrintQuery(order);
		YxtServiceQuery<PrintQuery> serviceQuery = createServiceQuery(printQuery);

		PrintReply printReply = new PrintReply();
		YxtServiceReply<PrintReply> serviceReply = createServiceReply(printReply);

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
		YxtServiceQuery<ConfirmPrintQuery> serviceQuery = createServiceQuery(confirmPrintQuery);

		ConfirmPrintReply confirmPrintReply = new ConfirmPrintReply();
		YxtServiceReply<ConfirmPrintReply> serviceReply = createServiceReply(confirmPrintReply);
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
		YxtServiceQuery<HallQuery> serviceQuery = createServiceQuery(hallQuery);

		HallReply hallReply = new HallReply();
		YxtServiceReply<HallReply> serviceReply = createServiceReply(hallReply);

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
	protected void execute(YxtServiceQuery<?> serviceQuery,
			YxtServiceReply<?> serviceReply) {
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
	private <T extends YxtQuery> YxtServiceQuery<T> createServiceQuery(T query) {
		return new YxtServiceQuery<T>(query, settings.getUsername(),
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
	private <T extends YxtReply> YxtServiceReply<T> createServiceReply(T reply) {
		return new YxtServiceReply<T>(reply);
	}
}