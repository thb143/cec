package cn.mopon.cec.core.access.ticket.ng;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.entity.TicketVoucher;
import coo.base.util.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "ngTicketServiceTestContext.xml" })
public class NgTicketServiceTest {
	private String cinemaCode = "01010071";
	private TicketSettings settings;
	private NgTicketService ticketAccessService;

	@Before
	public void init() {
		settings = new TicketSettings();
		settings.setUrl("http://192.168.9.59:9080/scts.service/webservice/query");
		settings.setUsername("01010071");
		settings.setPassword("123456");
		ticketAccessService = new NgTicketService(settings);
	}

	// @Test
	public void getCinema() {
		Cinema cinema = ticketAccessService.getCinema(cinemaCode);
		System.out.println(cinema.getHallCount());
	}

	// @Test
	public void getSeats() {
		Cinema cinema = ticketAccessService.getCinema(cinemaCode);
		cinema.getHalls().get(0).setCinema(cinema);
		List<Seat> list = ticketAccessService.getHallSeats(cinema.getHalls()
				.get(0));
		System.out.println(list.get(0));
	}

	// @Test
	// public void getFilms() {
	// List<NgFilm> films = ticketAccessService.getFilms(
	// DateUtils.parse("2014-10-12", "yyyy-MM-dd"),
	// DateUtils.parse("2014-10-31", "yyyy-MM-dd"));
	// if (films != null && films.size() > 0) {
	// System.out.println(films.get(0).getCode());
	// }
	// }

	// @Test
	public void lockSeats() {
		TicketOrder order = new TicketOrder();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		ChannelShow show = new ChannelShow();
		show.setCinema(cinema);
		show.setShowCode("0514102700200109");
		order.setTicketCount(1);

		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("0300000200300301");

		List<TicketOrderItem> list = new ArrayList<TicketOrderItem>();
		list.add(item);
		order.setOrderItems(list);
		ticketAccessService.lockSeat(order);
		// System.out.println(o.getCode());
		// channel.setCode("0009");

	}

	// @Test
	public void orderQuery() {
		TicketOrder order = new TicketOrder();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		ChannelShow channel = new ChannelShow();
		channel.setCinema(cinema);
		order.setCinemaOrderCode("1414566363668473");
		TicketOrder query = ticketAccessService.queryOrder(order);
		System.out.println(query);
	}

	// @Test
	public void refundQuery() {
		TicketOrder order = new TicketOrder();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		ChannelShow channel = new ChannelShow();
		channel.setCinema(cinema);
		TicketVoucher vou = new TicketVoucher();
		vou.setPrintCode("1414493379822430");
		vou.setVerifyCode("7030273520382557");
		order.setVoucher(vou);
		ticketAccessService.refundTicket(order);
	}

	// @Test
	public void releaseSeatQuery() {
		TicketOrder order = new TicketOrder();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		ChannelShow channel = new ChannelShow();
		channel.setShowCode("0514102700200109");
		channel.setCinema(cinema);
		order.setCinemaOrderCode("1414547759012094");
		order.setTicketCount(1);
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("0300000200100401");
		List<TicketOrderItem> list = new ArrayList<TicketOrderItem>();
		list.add(item);
		order.setOrderItems(list);
		ticketAccessService.releaseSeat(order);
	}

	// @Test
	public void sessionQuery() {
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		List<Show> shows = ticketAccessService.getShows(cinema,
				DateUtils.parse("2014-10-20", "yyyy-MM-dd"),
				DateUtils.parse("2014-10-31", "yyyy-MM-dd"));
		System.out.println(shows.get(0).getCode());
	}

	// @Test
	public void seatQuery() {
		Cinema cinema = ticketAccessService.getCinema(cinemaCode);
		Hall hall = cinema.getHalls().get(0);
		hall.setCinema(cinema);
		ticketAccessService.getHallSeats(hall);
	}

	// @Test
	public void sessionSeatQuery() {
		Cinema cinema = ticketAccessService.getCinema(cinemaCode);
		ChannelShow show = new ChannelShow();
		show.setShowCode("0514102700200109");
		show.setCinema(cinema);
		ticketAccessService.getShowSeats(show);
	}

	// @Test
	public void submitOrderQuery() {
		Cinema cinema = ticketAccessService.getCinema(cinemaCode);
		TicketOrder order = new TicketOrder();
		order.setCinemaOrderCode("0514102700200109");
		ChannelShow show = new ChannelShow();
		show.setCinema(cinema);
		show.setShowCode("1414550767770490");
		TicketOrderItem item = new TicketOrderItem();
		item.setSalePrice(99.00);
		item.setSeatCode("0300000200100601");
		List<TicketOrderItem> list = new ArrayList<TicketOrderItem>();
		list.add(item);
		order.setOrderItems(list);
		ticketAccessService.submitOrder(order);
	}

	// @Test
	public void MemberSubmitOrderQuery() {

	}

	// @Test
	// public void printQuery() {
	// TicketOrder order = new TicketOrder();
	// TicketVoucher v = new TicketVoucher();
	// v.setPrintCode("1414493379822430");
	// v.setVerifyCode("7030273520382557");
	// ChannelShow show = new ChannelShow();
	// Cinema cinema = ticketAccessService.getCinema(cinemaCode);
	// show.setCinema(cinema);
	// order.setChannelShow(show);
	// order.setVoucher(v);
	// order = ticketAccessService.printQuery(order);
	// }

	// @Test
	// public void simpleTakeNoQuery() {
	// List<NgSimpleTakeNo> list = ticketAccessService.simpleTakeNoQuery(
	// cinemaCode, "1414566363668473");
	// System.out.println(list.get(0).getSimpleTakeNo());
	// }

	// @Test
	// public void takeTicketInfoQuery() {
	// List<NgTicketInfo> list = ticketAccessService.takeTicketInfoQuery(
	// cinemaCode, "10010252");
	// System.out.println(list.get(0));
	// }

	// @Test
	// public void ticketTypeQuery() {
	// // 0514102900400131，1414579251016945
	// System.out.println(ticketAccessService.ticketTypeQuery(cinemaCode,
	// "0514102900400131"));
	// }
}
