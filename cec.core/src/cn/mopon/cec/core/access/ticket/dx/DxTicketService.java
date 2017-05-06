package cn.mopon.cec.core.access.ticket.dx;

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
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;
import coo.base.util.BeanUtils;

/**
 * 鼎新网络售票接口实现。
 */
public class DxTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public DxTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery(cinemaCode);
		DxServiceQuery<CinemaQuery> serviceQuery = createDXServiceQuery(cinemaQuery);

		CinemaReply cinemaReply = new CinemaReply();
		DxServiceReply<CinemaReply> serviceReply = createDXServiceReply(cinemaReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getCinema();
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		DxServiceQuery<SeatQuery> serviceQuery = createDXServiceQuery(seatQuery);

		SeatReply seatReply = new SeatReply();
		DxServiceReply<SeatReply> serviceReply = createDXServiceReply(seatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery sessionQuery = new SessionQuery(cinema, startDate, endDate);
		DxServiceQuery<SessionQuery> serviceQuery = createDXServiceQuery(sessionQuery);

		SessionReply sessionReply = new SessionReply();
		DxServiceReply<SessionReply> serviceReply = createDXServiceReply(sessionReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows(cinema);
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow);
		DxServiceQuery<SessionSeatQuery> serviceQuery = createDXServiceQuery(sessionSeatQuery);

		SessionSeatReply sessionSeatReply = new SessionSeatReply();
		DxServiceReply<SessionSeatReply> serviceReply = createDXServiceReply(sessionSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShowSeats(status);
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order);
		DxServiceQuery<LockSeatQuery> serviceQuery = createDXServiceQuery(lockSeatQuery);

		LockSeatReply lockSeatReply = new LockSeatReply();
		DxServiceReply<LockSeatReply> serviceReply = createDXServiceReply(lockSeatReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder();
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery releaseSeatQuery = new ReleaseSeatQuery(order);
		DxServiceQuery<ReleaseSeatQuery> serviceQuery = createDXServiceQuery(releaseSeatQuery);

		ReleaseSeatReply releaseSeatReply = new ReleaseSeatReply();
		DxServiceReply<ReleaseSeatReply> serviceReply = createDXServiceReply(releaseSeatReply);

		execute(serviceQuery, serviceReply);
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order);
		DxServiceQuery<SubmitOrderQuery> serviceQuery = createDXServiceQuery(submitOrderQuery);

		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		DxServiceReply<SubmitOrderReply> serviceReply = createDXServiceReply(submitOrderReply);

		execute(serviceQuery, serviceReply);
		TicketOrder remoteOrder = serviceReply.getReply().getTicketOrder();
		remoteOrder.setOrderItems(order.getOrderItems());
		return queryTicketInfo(remoteOrder, order.getCinema().getCode());
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RefundTicketQuery refundTicketQuery = new RefundTicketQuery(order);
		DxServiceQuery<RefundTicketQuery> serviceQuery = createDXServiceQuery(refundTicketQuery);
		RefundTicketReply refundTicketReply = new RefundTicketReply();
		DxServiceReply<RefundTicketReply> serviceReply = createDXServiceReply(refundTicketReply);
		execute(serviceQuery, serviceReply);
		return true;
	}

	@Override
	public TicketOrder queryOrder(TicketOrder order) {
		OrderQuery orderQuery = new OrderQuery(order);
		DxServiceQuery<OrderQuery> serviceQuery = createDXServiceQuery(orderQuery);

		OrderReply orderReply = new OrderReply();
		DxServiceReply<OrderReply> serviceReply = createDXServiceReply(orderReply);

		execute(serviceQuery, serviceReply);
		TicketOrder remoteOrder = serviceReply.getReply().getTicketOrder();
		remoteOrder.setCinemaOrderCode(order.getCinemaOrderCode());
		remoteOrder.setOrderItems(order.getOrderItems());
		if (remoteOrder.getStatus() == TicketOrderStatus.FAILED) {
			return remoteOrder;
		}
		return queryTicketInfo(remoteOrder, order.getCinema().getCode());
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		PrintQuery printQuery = new PrintQuery(order);
		DxServiceQuery<PrintQuery> serviceQuery = createDXServiceQuery(printQuery);

		PrintReply printReply = new PrintReply();
		DxServiceReply<PrintReply> serviceReply = createDXServiceReply(printReply);

		execute(serviceQuery, serviceReply);
		TicketOrder remoteOrder = serviceReply.getReply().getTicketOrder();
		BeanUtils.copyFields(order, remoteOrder, "orderItems");
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			for (TicketOrderItem remoteOrderItem : remoteOrder.getOrderItems()) {
				if (orderItem.getTicketCode().equals(
						remoteOrderItem.getTicketCode())) {
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
		DxServiceQuery<ConfirmPrintQuery> serviceQuery = createDXServiceQuery(confirmPrintQuery);

		ConfirmPrintReply confirmPrintReply = new ConfirmPrintReply();
		DxServiceReply<ConfirmPrintReply> serviceReply = createDXServiceReply(confirmPrintReply);

		try {
			execute(serviceQuery, serviceReply);
		} catch (BusinessException e) {
			return false;
		}
		return true;
	}

	/**
	 * 查询影票信息。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @param cinemaCode
	 *            影院编码
	 * @return 返回查询后的订单信息。
	 */
	private TicketOrder queryTicketInfo(TicketOrder ticketOrder,
			String cinemaCode) {
		TicketInfoQuery ticketInfoQuery = new TicketInfoQuery(ticketOrder,
				cinemaCode);
		DxServiceQuery<TicketInfoQuery> serviceQuery = createDXServiceQuery(ticketInfoQuery);

		TicketInfoReply ticketInfoReply = new TicketInfoReply();
		DxServiceReply<TicketInfoReply> serviceReply = createDXServiceReply(ticketInfoReply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getTicketOrder(ticketOrder);
	}

	/**
	 * 调用鼎新网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 */
	protected void execute(DxServiceQuery<?> serviceQuery,
			DxServiceReply<?> serviceReply) {
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
	private <T extends DxQuery> DxServiceQuery<T> createDXServiceQuery(T query) {
		return new DxServiceQuery<T>(query, settings.getUsername(),
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
	private <T extends DxReply> DxServiceReply<T> createDXServiceReply(T reply) {
		return new DxServiceReply<T>(reply);
	}
}