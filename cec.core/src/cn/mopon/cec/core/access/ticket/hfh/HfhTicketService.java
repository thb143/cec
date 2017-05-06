package cn.mopon.cec.core.access.ticket.hfh;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
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
import coo.base.util.DateUtils;

/**
 * 火凤凰(幸福蓝海)网络售票接口实现。
 */
public class HfhTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public HfhTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery();
		HfhServiceQuery<CinemaQuery> serviceQuery = createHFHServiceQuery(cinemaQuery);
		CinemaReply cinemaReply = new CinemaReply();
		HfhServiceReply<CinemaReply> serviceReply = createHFHServiceReply(cinemaReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCinema(cinemaCode);
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		HfhServiceQuery<SeatQuery> serviceQuery = createHFHServiceQuery(seatQuery);
		SeatReply seatReply = new SeatReply();
		HfhServiceReply<SeatReply> serviceReply = createHFHServiceReply(seatReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery query = new SessionQuery(cinema, DateUtils.format(
				startDate, DateUtils.SECOND), DateUtils.format(endDate,
				DateUtils.SECOND));
		HfhServiceQuery<SessionQuery> serviceQuery = createHFHServiceQuery(query);
		SessionReply reply = new SessionReply();
		HfhServiceReply<SessionReply> serviceReply = createHFHServiceReply(reply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows();
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow);
		HfhServiceQuery<SessionSeatQuery> serviceQuery = createHFHServiceQuery(sessionSeatQuery);
		SessionSeatReply sessionSeatReply = new SessionSeatReply();
		HfhServiceReply<SessionSeatReply> serviceSeatReply = createHFHServiceReply(sessionSeatReply);
		execute(serviceQuery, serviceSeatReply);
		return serviceSeatReply.getReply().getShowSeats(channelShow, status);
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order);
		HfhServiceQuery<LockSeatQuery> serviceQuery = createHFHServiceQuery(lockSeatQuery);
		LockSeatReply lockSeatReply = new LockSeatReply();
		HfhServiceReply<LockSeatReply> servecilockSeatReply = createHFHServiceReply(lockSeatReply);
		execute(serviceQuery, servecilockSeatReply);
		return servecilockSeatReply.getReply().getOrder(order);
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery releaseSeatQuery = new ReleaseSeatQuery(order);
		HfhServiceQuery<ReleaseSeatQuery> serviceQuery = createHFHServiceQuery(releaseSeatQuery);
		ReleaseSeatReply releaseSeatReply = new ReleaseSeatReply();
		HfhServiceReply<ReleaseSeatReply> serveciReleaseReply = createHFHServiceReply(releaseSeatReply);
		execute(serviceQuery, serveciReleaseReply);
	}

	@Override
	// 返回影院订单编码，覆盖临时影院订单编码
	public TicketOrder submitOrder(TicketOrder order) {
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order);
		HfhServiceQuery<SubmitOrderQuery> serviceQuery = createHFHServiceQuery(submitOrderQuery);
		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		HfhServiceReply<SubmitOrderReply> serveciOrderReply = createHFHServiceReply(submitOrderReply);
		execute(serviceQuery, serveciOrderReply);
		return serveciOrderReply.getReply().getOrder();
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RefundTicketQuery refundTicketQuery = new RefundTicketQuery(order);
		HfhServiceQuery<RefundTicketQuery> serviceQuery = createHFHServiceQuery(refundTicketQuery);
		RefundTicketReply refundTicketReply = new RefundTicketReply();
		HfhServiceReply<RefundTicketReply> serveciOrderReply = createHFHServiceReply(refundTicketReply);
		execute(serviceQuery, serveciOrderReply);
		return true;
	}

	@Override
	public TicketOrder queryOrder(TicketOrder order) {
		OrderQuery orderQuery = new OrderQuery(order);
		HfhServiceQuery<OrderQuery> serviceQuery = createHFHServiceQuery(orderQuery);
		OrderReply orderReply = new OrderReply();
		HfhServiceReply<OrderReply> serveciPrintReply = createHFHServiceReply(orderReply);
		execute(serviceQuery, serveciPrintReply);
		return serveciPrintReply.getReply().getOrder();
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		PrintQuery printQuery = new PrintQuery(order);
		HfhServiceQuery<PrintQuery> serviceQuery = createHFHServiceQuery(printQuery);
		PrintReply printReply = new PrintReply();
		HfhServiceReply<PrintReply> serveciPrintReply = createHFHServiceReply(printReply);
		execute(serviceQuery, serveciPrintReply);
		TicketOrder remoteOrder = serveciPrintReply.getReply().getOrder();
		BeanUtils.copyFields(order, remoteOrder, "orderItems");
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			for (TicketOrderItem remoteOrderItem : remoteOrder.getOrderItems()) {
				if (orderItem.getSeatCol().equals(remoteOrderItem.getSeatCol())
						&& orderItem.getSeatRow().equals(
								remoteOrderItem.getSeatRow())) {
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
		HfhServiceQuery<ConfirmPrintQuery> serviceQuery = createHFHServiceQuery(confirmPrintQuery);
		ConfirmPrintReply confirmPrintReply = new ConfirmPrintReply();
		HfhServiceReply<ConfirmPrintReply> serviceReply = createHFHServiceReply(confirmPrintReply);
		try {
			execute(serviceQuery, serviceReply);
		} catch (BusinessException e) {
			return false;
		}
		return true;
	}

	/**
	 * 调用火凤凰网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	protected void execute(HfhServiceQuery<?> serviceQuery,
			HfhServiceReply<?> serviceReply) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url = settings.getUrl() + "/"
				+ serviceQuery.getQuery().getMethod();
		List<NameValuePair> params = serviceQuery.getQuery().getNvps();
		try {
			URIBuilder builder = new URIBuilder(url);
			builder.addParameters(params);
			HttpGet httpGet = new HttpGet(builder.build());
			httpGet.setConfig(getRequestConfig());

			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpclient.execute(httpGet).getEntity();
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
	private <T extends HfhQuery> HfhServiceQuery<T> createHFHServiceQuery(
			T query) {
		query.addParam("userId", settings.getUsername());
		query.addParam("userPass", settings.getPassword());
		if (query.getNeedCinemaParams()) {
			query.genCinemaParams(settings.getCinema());
		}
		if (query.getNeedRandKey()) {
			query.genRandKey();
		}
		if (query.getNeedCheckValue()) {
			query.genCheckValue();
		}
		return new HfhServiceQuery<T>(query);
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
	private <T extends HfhReply> HfhServiceReply<T> createHFHServiceReply(
			T reply) {
		return new HfhServiceReply<T>(reply);
	}
}