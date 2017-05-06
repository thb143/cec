package cn.mopon.cec.core.access.ticket.std;

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
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Film;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketAccessType;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.entity.TicketVoucher;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.util.DateUtils;
import coo.core.jackson.GenericObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "stdTicketServiceTestContext.xml" })
public class StdTicketServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String CINEMA_CODE = "01010071";
	private TicketSettings settings;
	private TicketAccessService ticketAccessService;

	@Before
	public void init() {
		settings = new TicketSettings();
		settings.setUrl("http://192.168.9.59:9080/scts.service/webservice/query");
		settings.setUsername("01010071");
		settings.setPassword("123456");
		TicketAccessType accessType = new TicketAccessType();
		accessType.setConnectTimeout(5);
		accessType.setSocketTimeout(10);
		settings.setAccessType(accessType);
		ticketAccessService = new StdTicketService(settings);
	}

	// 获取影院
	// @Test
	public void testGetCinema() throws Exception {
		Cinema cinema = ticketAccessService.getCinema(CINEMA_CODE);
		print(cinema);
	}

	// @Test
	public void testGenCinemaXml() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode("test");

		Hall hall = new Hall();
		cinema.getHalls().add(hall);

		CinemaReply reply = new CinemaReply();
		log.debug(reply.getXstream().toXML(cinema));
	}

	// 获取影厅座位
	// @Test
	public void testGetSeat() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);

		Hall hall = new Hall();
		hall.setCode("0131133207000003");
		hall.setCinema(cinema);
		List<Seat> seats = ticketAccessService.getHallSeats(hall);
		print(seats);
	}

	// 获取排期信息
	// @Test
	public void testGetShows() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);

		Date startDate = DateUtils.parse("2014-10-14");
		Date endDate = DateUtils.parse("2014-10-30");

		List<Show> shows = ticketAccessService.getShows(cinema, startDate,
				endDate);
		print(shows);
	}

	// 获取排期座位售出状态
	// @Test
	public void testGetSessionSeat() throws Exception {
		ChannelShow show = new ChannelShow();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCinema(cinema);
		show.setShowCode("0514101300200504");
		List<ShowSeat> seats = ticketAccessService.getShowSeats(show,
				SellStatus.DISABLED);
		print(seats);
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
		show.setHall(hall);
		show.setCode("1000414052905411");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("1_1_3_1066000000");
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.lockSeat(ticketOrder);
		print(order);
	}

	// @Test
	public void testReleaseSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode("B1F3A8D41E7E0EBB");
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCode("1000414052905411");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("1_1_6_1066000000");
		items.add(item);
		ticketOrder.setOrderItems(items);
		ticketAccessService.releaseSeat(ticketOrder);
		print(ticketOrder);
	}

	@Test
	public void testSubmitOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode("1414403981059620");
		ChannelShow show = new ChannelShow();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCinema(cinema);
		show.setCode("141000001590");
		show.setShowCode("0514102700400108");
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("0300000400100101");
		item.setSalePrice(38.0);
		items.add(item);
		ticketOrder.setOrderItems(items);
		try {
			TicketOrder order = ticketAccessService.submitOrder(ticketOrder);
			System.out.println(order.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void testRefundTicket() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode("598C3B02181E4DE4");
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
		ticketAccessService.refundTicket(ticketOrder);
	}

	// @Test
	public void testOrderQuery() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCinemaOrderCode("A5CBD33BDB6FFA3D");
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(CINEMA_CODE);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCode("1000414060305417");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);

		TicketOrder order = ticketAccessService.queryOrder(ticketOrder);
		print(order);
	}

	// @Test
	public void testGenShowsXml() throws Exception {
		List<Show> shows = new ArrayList<Show>();

		Show show = new Show();
		show.setCode("show code");

		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		hall.setCinema(cinema);
		show.setHall(hall);
		Film film = new Film();
		show.setFilm(film);

		shows.add(show);
		shows.add(show);
		shows.add(show);

		SessionReply reply = new SessionReply();
		log.debug(reply.getXstream().toXML(shows));
	}

	private void print(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new GenericObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String json = mapper.writeValueAsString(obj);
		log.debug(json);
	}
}
