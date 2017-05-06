package cn.mopon.cec.core.access.ticket.hfh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketAccessType;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.enums.TicketAccessAdapter;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.util.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ticketServiceTestContext.xml" })
public class HFHTicketServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String CINEMA_CODE = "44135701_1781";
	private TicketSettings settings;
	private TicketAccessService ticketAccessService;

	@Before
	public void init() {
		TicketAccessType ticketAccessType = new TicketAccessType();
		ticketAccessType.setAdapter(TicketAccessAdapter.HFH);
		settings = new TicketSettings();
		settings.setUrl("http://211.147.239.214:9600/DI_DataSender_For_BOM.asmx");
		// settings.setUrl("http://211.147.239.210:8080/v4csharp/DI_DataSender_For_BOM.asmx");
		settings.setUsername("MG_ZHONGYING");
		settings.setPassword("ZhongYingFilm");
		settings.setAccessType(ticketAccessType);
		ticketAccessService = TicketAccessServiceFactory
				.getTicketService(settings);
	}

	// 获取影院
	// @Test
	public void testGetCinema() throws Exception {
		Cinema cinema = ticketAccessService.getCinema(CINEMA_CODE);
		log.debug(cinema.getName() + ":" + cinema.getCode());
		log.debug("hallType:" + cinema.getHalls().get(0));
	}

	// 获取影厅座位
	// @Test
	public void testGetSeat() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);

		Hall hall = new Hall();
		hall.setCode("66666666");
		hall.setCinema(cinema);
		List<Seat> seats = ticketAccessService.getHallSeats(hall);
		for (Seat seat : seats) {
			System.out.println(seat.getCode() + "-------------"
					+ seat.getLoveCode());
		}
	}

	// 获取排期信息
	@Test
	public void testGetShows() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);

		Date startDate = DateUtils.parse("2014-11-29");
		Date endDate = DateUtils.parse("2014-11-20");

		List<Show> shows = ticketAccessService.getShows(cinema, startDate,
				endDate);
		for (Show show : shows) {
			System.out.println(show.getCode() + "==" + show.getThrough()
					+ "showTime:"
					+ DateUtils.format(show.getShowTime(), DateUtils.SECOND));
		}
	}

	// 获取排期座位售出状态
	// @Test
	public void testGetSessionSeat() throws Exception {
		ChannelShow show = new ChannelShow();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		hall.setCode("5");
		show.setCinema(cinema);
		show.setHall(hall);
		show.setFilmCode("05120095201301");
		show.setShowCode("99920140402067222_1");
		show.setShowTime(DateUtils.parse("2014-11-29 11:45"));
		List<ShowSeat> seats = ticketAccessService.getShowSeats(show,
				SellStatus.ENABLED);
		for (ShowSeat seat : seats) {
			System.out.println("status:" + seat.getStatus().getText()
					+ "seatCode:" + seat.getCode());
		}
	}

	// @Test
	public void testLockSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		hall.setCode("1");
		show.setHall(hall);

		show.setFilmCode("001103332012CN");

		show.setShowTime(DateUtils.parse("2014-08-18 23:50:00"));
		show.setCode("99920140402067975_5");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);

		ticketOrder.setCinemaOrderCode("Y10010001884");
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("2_11");
		item.setSalePrice(30.0);
		item.setCircuitFee(2.00);
		item.setSeatGroupCode("01");
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.lockSeat(ticketOrder);
		System.out.println(order.getCinemaOrderCode());
	}

	// @Test
	public void testReleaseSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		ticketOrder.setCinemaOrderCode("Y10010001790");
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

	// @Test
	public void testSubmitOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode("590458003646");
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		hall.setCode("1");
		show.setHall(hall);
		show.setCode("99920140402063478_6");
		show.setFilmCode("001103332012CN");
		show.setShowTime(DateUtils.parse("2014-08-16 03:00:00"));
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		ticketOrder.setMobile("15178945612");
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSalePrice(180.0);
		item.setCircuitFee(5.0);
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.submitOrder(ticketOrder);
		System.out.println("cinemaOrderCode" + order.getCinemaOrderCode());
		System.out.println("printCode:" + order.getVoucher().getPrintCode());
		List<TicketOrderItem> orderitems = order.getOrderItems();
		for (TicketOrderItem orderItems : orderitems) {
			System.out.println("ticketCode:" + orderItems.getTicketCode());
		}

	}

	// @Test
	public void testQueryOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ChannelShow channelShow = new ChannelShow();
		ticketOrder.setCinemaOrderCode("071000000045");
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		channelShow.setCinema(cinema);
		ticketOrder.setTicketCount(2);
		TicketOrder order = ticketAccessService.queryOrder(ticketOrder);
		System.out.println("cinemaOrderCode" + order.getCinemaOrderCode());
		System.out.println("printCode:" + order.getVoucher().getPrintCode());
		System.out.println("使用状态:" + order.getVoucher().getStatus().getText());
	}
}
