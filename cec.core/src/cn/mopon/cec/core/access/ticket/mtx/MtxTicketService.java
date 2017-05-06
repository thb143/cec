package cn.mopon.cec.core.access.ticket.mtx;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import cn.mopon.cec.core.access.ticket.AbstractTicketService;
import cn.mopon.cec.core.access.ticket.converter.HttpEntityUtils;
import cn.mopon.cec.core.access.ticket.mtx.vo.ShowVo;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;

/**
 * 满天星网络售票接口实现。
 */
public class MtxTicketService extends AbstractTicketService {
	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票设置
	 */
	public MtxTicketService(TicketSettings settings) {
		super(settings);
	}

	@Override
	public Cinema getCinema(String cinemaCode) {
		CinemaQuery cinemaQuery = new CinemaQuery(cinemaCode);
		MtxServiceQuery<CinemaQuery> serviceQuery = createMTXServiceQuery(cinemaQuery);
		CinemaReply cinemaReply = new CinemaReply();
		MtxServiceReply<CinemaReply> serviceReply = createMTXServiceReply(cinemaReply);
		execute(serviceQuery, serviceReply);
		Cinema cinema = serviceReply.getReply().getCinema(cinemaCode);
		List<Hall> halls = getHall(cinemaCode);
		cinema.setHallCount(halls.size());
		cinema.setHalls(halls);
		return cinema;
	}

	@Override
	public List<Seat> getHallSeats(Hall hall) {
		SeatQuery seatQuery = new SeatQuery(hall);
		MtxServiceQuery<SeatQuery> serviceQuery = createMTXServiceQuery(seatQuery);
		SeatReply seatReply = new SeatReply();
		MtxServiceReply<SeatReply> serviceReply = createMTXServiceReply(seatReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getHall().getSeats();
	}

	@Override
	public List<Show> getShows(Cinema cinema, Date startDate, Date endDate) {
		SessionQuery sessionQuery = new SessionQuery(cinema);
		MtxServiceQuery<SessionQuery> serviceQuery = createMTXServiceQuery(sessionQuery);
		SessionReply sessionReply = new SessionReply();
		MtxServiceReply<SessionReply> serviceReply = createMTXServiceReply(sessionReply);
		execute(serviceQuery, serviceReply);

		return serviceReply.getReply().getShows(startDate, endDate);
	}

	@Override
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		SessionSeatQuery sessionSeatQuery = new SessionSeatQuery(channelShow);
		MtxServiceQuery<SessionSeatQuery> serviceQuery = createMTXServiceQuery(sessionSeatQuery);
		SessionSeatReply sessionReply = new SessionSeatReply();
		MtxServiceReply<SessionSeatReply> serviceReply = createMTXServiceReply(sessionReply);
		execute(serviceQuery, serviceReply, "json");
		return serviceReply.getReply().getShowSeats(channelShow);
	}

	@Override
	public TicketOrder lockSeat(TicketOrder order) {
		LockSeatQuery lockSeatQuery = new LockSeatQuery(order, settings);
		MtxServiceQuery<LockSeatQuery> serviceQuery = createPeculiarMTXServiceQuery(lockSeatQuery);
		LockSeatReply lockSeatReply = new LockSeatReply();
		MtxServiceReply<LockSeatReply> serveciReply = createMTXServiceReply(lockSeatReply);
		execute(serviceQuery, serveciReply);
		return serveciReply.getReply().getOrder();
	}

	@Override
	public void releaseSeat(TicketOrder order) {
		ReleaseSeatQuery releaseSeatQuery = new ReleaseSeatQuery(order);
		MtxServiceQuery<ReleaseSeatQuery> serviceQuery = createMTXServiceQuery(releaseSeatQuery);
		ReleaseSeatReply releaseSeatReply = new ReleaseSeatReply();
		MtxServiceReply<ReleaseSeatReply> serveciReply = createMTXServiceReply(releaseSeatReply);
		execute(serviceQuery, serveciReply);
	}

	@Override
	public TicketOrder submitOrder(TicketOrder order) {
		// 确认订单前，调用修改订单价格，修改票面价。
		modifyPay(order);
		SubmitOrderQuery submitOrderQuery = new SubmitOrderQuery(order,
				settings);
		MtxServiceQuery<SubmitOrderQuery> serviceQuery = createPeculiarMTXServiceQuery(submitOrderQuery);
		SubmitOrderReply submitOrderReply = new SubmitOrderReply();
		MtxServiceReply<SubmitOrderReply> serveciReply = createMTXServiceReply(submitOrderReply);
		execute(serviceQuery, serveciReply);
		TicketOrder externalOrder = serveciReply.getReply().getOrder(order);
		confirmOrder(externalOrder);
		return externalOrder;
	}

	@Override
	public Boolean refundTicket(TicketOrder order) {
		RefundTicketQuery refundTicketQuery = new RefundTicketQuery(order);
		MtxServiceQuery<RefundTicketQuery> serviceQuery = createMTXServiceQuery(refundTicketQuery);
		RefundTicketReply refundTicketReply = new RefundTicketReply();
		MtxServiceReply<RefundTicketReply> serveciReply = createMTXServiceReply(refundTicketReply);
		execute(serviceQuery, serveciReply);
		return serveciReply.getReply().getRefunded();
	}

	@Override
	public TicketOrder queryOrder(TicketOrder order) {
		OrderQuery orderQuery = new OrderQuery(order);
		MtxServiceQuery<OrderQuery> serviceQuery = createMTXServiceQuery(orderQuery);
		OrderReply orderReply = new OrderReply();
		MtxServiceReply<OrderReply> serveciReply = createMTXServiceReply(orderReply);
		execute(serviceQuery, serveciReply);
		return serveciReply.getReply().getOrder();
	}

	@Override
	public TicketOrder queryPrint(TicketOrder order) {
		PrintQuery printQuery = new PrintQuery(order);
		MtxServiceQuery<PrintQuery> serviceQuery = createMTXServiceQuery(printQuery);
		PrintReply printReply = new PrintReply();
		MtxServiceReply<PrintReply> serveciReply = createMTXServiceReply(printReply);
		execute(serviceQuery, serveciReply);
		return serveciReply.getReply().getOrder(order);
	}

	@Override
	public boolean confirmPrint(TicketOrder order) {
		ConfirmPrintQuery confirmPrintQuery = new ConfirmPrintQuery(order);
		MtxServiceQuery<ConfirmPrintQuery> serviceQuery = createMTXServiceQuery(confirmPrintQuery);
		ConfirmPrintReply confirmPrintReply = new ConfirmPrintReply();
		MtxServiceReply<ConfirmPrintReply> serveciReply = createMTXServiceReply(confirmPrintReply);
		try {
			execute(serviceQuery, serveciReply);
		} catch (BusinessException e) {
			return false;
		}
		return true;
	}

	/**
	 * 修改订单价格。
	 * 
	 * @param order
	 *            订单
	 */
	private void modifyPay(TicketOrder order) {
		ModifyPayQuery modifyPayQuery = new ModifyPayQuery(order);
		MtxServiceQuery<ModifyPayQuery> serviceQuery = createMTXServiceQuery(modifyPayQuery);
		ModifyPayReply modifyPayReply = new ModifyPayReply();
		MtxServiceReply<ModifyPayReply> serveciReply = createMTXServiceReply(modifyPayReply);
		execute(serviceQuery, serveciReply);
	}

	/**
	 * 根据影片编码,影院编码读取上映场次排期。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回排期列表。
	 */
	public List<ShowVo> getFeatureInfo(TicketOrder order) {
		GetFeatureInfoQuery query = new GetFeatureInfoQuery(order.getCinema()
				.getCode(), order.getFilmCode(), DateUtils.format(
				order.getShowTime(), DateUtils.DAY));
		MtxServiceQuery<GetFeatureInfoQuery> serviceQuery = createMTXServiceQuery(query);

		GetFeatureInfoReply reply = new GetFeatureInfoReply();
		MtxServiceReply<GetFeatureInfoReply> serviceReply = createMTXServiceReply(reply);

		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getShows();
	}

	/**
	 * 调用漫天星网络售票服务。
	 * 
	 * @param serviceQuery
	 *            服务请求对象
	 * @param serviceReply
	 *            服务响应对象
	 * @param type
	 *            接口请求类别
	 */
	private void execute(MtxServiceQuery<?> serviceQuery,
			MtxServiceReply<?> serviceReply, String... type) {
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

			if (CollectionUtils.isNotEmpty(type)) {
				serviceReply.genReplyJson(replyString);
			} else {
				serviceReply.genReply(replyString);
			}
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
	private <T extends MtxQuery> MtxServiceQuery<T> createMTXServiceQuery(
			T query) {
		return new MtxServiceQuery<T>(query, settings.getUsername(),
				settings.getPassword());
	}

	/**
	 * 创建特殊服务请求对象。
	 * 
	 * @param <T>
	 *            请求对象类型
	 * @param query
	 *            请求对象
	 * @return 返回服务请求对象。
	 */
	private <T extends MtxQuery> MtxServiceQuery<T> createPeculiarMTXServiceQuery(
			T query) {
		return new MtxServiceQuery<T>(query);
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
	private <T extends MtxReply> MtxServiceReply<T> createMTXServiceReply(
			T reply) {
		return new MtxServiceReply<T>(reply);
	}

	/**
	 * 轮询确认订单。
	 * 
	 * @param externalOrder
	 *            外部订单
	 */
	private void confirmOrder(TicketOrder externalOrder) {
		for (int i = 0; i < 5; i++) {
			try {
				TicketOrder ticketOrder = queryOrder(externalOrder);
				if (ticketOrder.getStatus() == TicketOrderStatus.SUCCESS) {
					externalOrder.setStatus(TicketOrderStatus.SUCCESS);
					return;
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			}
		}
		throw new BusinessException("轮询确认订单出票失败。");
	}

	/**
	 * 根据影院编码获取影厅信息。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @return 返回影厅信息
	 */
	private List<Hall> getHall(String cinemaCode) {
		HallQuery hallQuery = new HallQuery(cinemaCode);
		MtxServiceQuery<HallQuery> serviceQuery = createMTXServiceQuery(hallQuery);
		HallReply hallReply = new HallReply();
		MtxServiceReply<HallReply> serviceReply = createMTXServiceReply(hallReply);
		execute(serviceQuery, serviceReply);
		return serviceReply.getReply().getHalls();
	}
}