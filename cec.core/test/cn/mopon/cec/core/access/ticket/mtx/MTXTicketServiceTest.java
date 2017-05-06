package cn.mopon.cec.core.access.ticket.mtx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketAccessType;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.enums.TicketAccessAdapter;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.util.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ticketServiceTestContext.xml" })
public class MTXTicketServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String CINEMA_CODE = "gzxxx";
	private TicketSettings settings;
	private MtxTicketService ticketAccessService;

	@Before
	public void init() {
		TicketAccessType ticketAccessType = new TicketAccessType();
		ticketAccessType.setAdapter(TicketAccessAdapter.MTX);
		ticketAccessType.setConnectTimeout(5);
		ticketAccessType.setSocketTimeout(10);
		settings = new TicketSettings();
		settings.setUrl("http://ticket.mvtapi.com:8500/ticketapi/services/ticketapi");
		settings.setUsername("TEST");
		settings.setPassword("12345678");
		settings.setAccessType(ticketAccessType);
		// settings.setLowestPrice(15D);
		// ticketAccessService = TicketAccessServiceFactory
		// .getTicketService(settings);
		ticketAccessService = new MtxTicketService(settings);
	}

	// @Test
	public void testModifyPay() throws Exception {
		TicketOrder order = new TicketOrder();
		order.setCinemaOrderCode("46246199");
		order.setCode("1410231000000016");
		TicketOrderItem item = new TicketOrderItem();
		item.setSubmitPrice(45.00);
		order.getOrderItems().add(item);
		// ticketAccessService.modifyPay(order);
	}

	// 获取影院
	// @Test
	public void testGetCinema() throws Exception {
		Cinema cinema = ticketAccessService.getCinema(CINEMA_CODE);
		log.debug("cinemaName:" + cinema.getName());
		log.debug("hallCount:" + cinema.getHallCount() + "----" + "hallName:"
				+ cinema.getHalls().get(0).getName());

	}

	// 获取影厅座位
	// @Test
	public void testGetSeat() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);

		Hall hall = new Hall();
		hall.setCode("01");
		hall.setCinema(cinema);
		List<Seat> seats = ticketAccessService.getHallSeats(hall);
		for (Seat seat : seats) {
			System.out.println("seatCode:" + seat.getCode() + "----"
					+ "hallCode:" + seat.getHall().getCode());
		}
	}

	// 获取排期信息
	@Test
	public void testGetShows() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		cinema.setTicketSettings(settings);
		try {
			List<Show> shows = ticketAccessService.getShows(cinema,
					DateUtils.parse("2014-11-28"),
					DateUtils.parse("2014-12-11"));
			System.out.println(shows.size());
			for (Show show : shows) {
				System.out.println("showCode:"
						+ show.getCode()
						+ "==是否连场"
						+ show.getThrough()
						+ "==showTime:"
						+ DateUtils.format(show.getShowTime(), DateUtils.SECOND
								+ "==影厅编码:" + show.getHall().getCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 获取排期座位状态
	// @Test
	public void testGetSessionSeat() throws Exception {
		ChannelShow show = new ChannelShow();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setShowCode("75429229");
		List<ShowSeat> seats = ticketAccessService.getShowSeats(show,
				SellStatus.ENABLED);
		for (ShowSeat seat : seats) {
			System.out.println("status:" + seat.getStatus().getText()
					+ "==seatCode:" + seat.getCode());
		}
	}

	// 锁定座位
	// @Test
	public void testLockSeat() throws Exception {
		String mobile = "15112664766", hallCode = "04", showCode = "75429229", channelCode = "0009", seatCode1 = "05010919";
		TicketOrder order = new TicketOrder();
		order.setCinemaAmount(1d);
		String code = getFixLenthString(13);

		order.setCode(code);
		order.setMobile(mobile);
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		hall.setCode(hallCode);
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setCode(showCode);
		show.setHall(hall);
		// order.setGoods(ticketChannel);
		// ticketChannel.setProduct(ticket);
		Channel channel = new Channel();
		channel.setCode(channelCode);
		// ticketChannel.setChannel(channel);
		// ticket.setShow(show);
		// order.setGoods(ticketChannel);
		List<TicketOrderItem> ticketOrderItems = new ArrayList<TicketOrderItem>();
		TicketOrderItem ticketOrderItem1 = new TicketOrderItem();
		ticketOrderItem1.setSeatCode(seatCode1);
		// TODO 重写
		// ticketOrderItem1.setSalePrice(order.getSalePrice());
		ticketOrderItem1.setCircuitFee(0d);
		TicketOrderItem ticketOrderItem2 = new TicketOrderItem();
		ticketOrderItem2.setSeatCode("05010914");
		// TODO 重写
		// ticketOrderItem2.setSalePrice(order.getSalePrice());
		ticketOrderItem2.setCircuitFee(0d);
		ticketOrderItems.add(ticketOrderItem1);
		// ticketOrderItems.add(ticketOrderItem2);
		order.setOrderItems(ticketOrderItems);
		TicketOrder order1 = ticketAccessService.lockSeat(order);
		System.out.println("内部订单号:" + code);
		System.out.println("影院订单号:" + order1.getCinemaOrderCode());
	}

	// 确认订单
	// @Test
	public void testSubmitOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCode("ZY00001100010");
		ticketOrder.setMobile("13888888888");
		ticketOrder.setCinemaOrderCode("46631528");
		ChannelShow show = new ChannelShow();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		show.setShowCode("82609939");
		show.setCinema(cinema);
		show.setCode("141000001601");
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("03010211");
		item.setSalePrice(80.0);
		item.setSubmitPrice(80.0);
		items.add(item);
		ticketOrder.setOrderItems(items);
		try {
			TicketOrder order = ticketAccessService.submitOrder(ticketOrder);
			for (TicketOrderItem orderItems : order.getOrderItems()) {
				System.out.println("ticketCode:" + orderItems.getTicketCode());
				System.out.println("seatCode:" + orderItems.getSeatCode());
			}
			System.out.println("cinemaOrderCode:" + order.getCinemaOrderCode()
					+ "==verifyCode:" + order.getVoucher().getVerifyCode()
					+ "==ticketCode:"
					+ order.getOrderItems().get(0).getTicketCode()
					+ "==ticketOrderStatus:" + order.getStatus().getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 释放座位
	// @Test
	public void testReleaseSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		ticketOrder.setCinemaOrderCode("38879554");
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		ticketAccessService.releaseSeat(ticketOrder);
	}

	// 查询订单状态
	// @Test
	public void testQueryOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		ticketOrder.setCinemaOrderCode("118293947322");
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		TicketOrder order = ticketAccessService.queryOrder(ticketOrder);
		System.out.println("cinemaOrderCode" + order.getCinemaOrderCode());
		System.out.println("printCode:" + order.getVoucher().getPrintCode());
		System.out.println("使用状态:" + order.getVoucher().getStatus().getText());
	}

	// 退票
	// @Test
	public void testRefundTicket() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode("38885004");
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);

		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCode("1000414052205404");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		TicketVoucher voucher = new TicketVoucher();
		ticketOrder.getVoucher().setPrintCode("2014052215421379");
		ticketOrder.getVoucher().setVerifyCode("80B7432EEE4C5847");
		ticketOrder.setVoucher(voucher);
		Boolean bool = ticketAccessService.refundTicket(ticketOrder);
		System.out.println(bool);
	}

	/**
	 * 获取指定长度的随便数。
	 * 
	 * @param strLength
	 *            长度
	 * @return 返回随机数。
	 */
	public String getFixLenthString(int strLength) {
		Random rm = new Random();
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		String fixLenthString = String.valueOf(pross);
		return fixLenthString.substring(2, strLength + 1);
	}
}
