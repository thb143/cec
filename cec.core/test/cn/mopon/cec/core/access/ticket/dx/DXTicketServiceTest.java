package cn.mopon.cec.core.access.ticket.dx;

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

import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.util.DateUtils;
import coo.core.jackson.GenericObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ticketServiceTestContext.xml" })
public class DXTicketServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private TicketSettings settings;
	private DxTicketService ticketAccessService;
	private String cinemaCode = "4";

	@Before
	public void init() {
		settings = new TicketSettings();
		settings.setUrl("http://api.platform.yinghezhong.com");
		settings.setUsername("10128");
		settings.setPassword("zy.89bud20dniw");
		// settings.setProvider(ProviderType.DX);
		ticketAccessService = new DxTicketService(settings);
		// ticketAccessService = TicketAccessServiceFactory
		// .getTicketService(settings);
	}

	// @Test
	public void testGetCinema() throws Exception {
		Cinema cinema = ticketAccessService.getCinema(cinemaCode);
		print(cinema);
	}

	// @Test
	public void testGetHallSeats() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		Hall hall = new Hall();
		hall.setCode("2");
		hall.setCinema(cinema);
		print(ticketAccessService.getHallSeats(hall));
	}

	@Test
	public void testGetShows() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		print(ticketAccessService.getShows(cinema, new Date(),
				DateUtils.getNextDay(2)));
	}

	// @Test
	public void testGetShowSeats() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		Hall hall = new Hall();
		hall.setCinema(cinema);
		ChannelShow show = new ChannelShow();
		show.setHall(hall);
		show.setShowCode("6654820140813112701");
		print(ticketAccessService.getShowSeats(show));
	}

	// @Test
	public void testLockSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCode("6654820140813112701");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("178");
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.lockSeat(ticketOrder);
		print(order);
	}

	// @Test
	public void testReleaseSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCode("6654820140813112701");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		ticketOrder.setCinemaOrderCode("798122389913");
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("174");
		items.add(item);
		ticketOrder.setOrderItems(items);
		ticketAccessService.releaseSeat(ticketOrder);
	}

	// @Test
	public void testSubmitOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		hall.setCinema(cinema);
		show.setHall(hall);
		show.setCode("6654820140813112701");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		ticketOrder.setCinemaOrderCode("799642920124");
		ticketOrder.setCode("20140814");
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("178");
		// TODO 重写
		// item.setServiceFee(3.0);
		// item.setSettlePrice(80.0);
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.submitOrder(ticketOrder);
		print(order);
	}

	private void print(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new GenericObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String json = mapper.writeValueAsString(obj);
		log.debug(json);
	}
}
