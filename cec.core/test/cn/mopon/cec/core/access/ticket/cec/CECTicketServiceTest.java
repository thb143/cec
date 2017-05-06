package cn.mopon.cec.core.access.ticket.cec;

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
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketAccessType;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.TicketAccessAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.util.DateUtils;
import coo.core.jackson.GenericObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ticketServiceTestContext.xml" })
public class CECTicketServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private TicketSettings settings;
	private TicketAccessService ticketAccessService;
	private String cinemaCode = "01010071";

	@Before
	public void init() {
		TicketAccessType ticketAccessType = new TicketAccessType();
		ticketAccessType.setAdapter(TicketAccessAdapter.NGC);
		settings = new TicketSettings();
		settings.setUrl("http://192.168.8.181:8082/api/ticket/v1");
		settings.setUsername("0010");
		settings.setPassword("pG2LrlbczpTUYv1n");
		settings.setAccessType(ticketAccessType);
		ticketAccessService = TicketAccessServiceFactory
				.getTicketService(settings);
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
		hall.setCode("0131133206000002");
		hall.setCinema(cinema);
		print(ticketAccessService.getHallSeats(hall));
	}

	// @Test
	public void testGetShows() throws Exception {
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		List<Show> shows = ticketAccessService.getShows(cinema, new Date(),
				DateUtils.getNextDay());
		print(shows);
	}

	// @Test
	public void testGetShowSeats() throws Exception {
		ChannelShow show = new ChannelShow();
		show.setShowCode("141000000929");
		print(ticketAccessService.getShowSeats(show));
		// print(ticketAccessService.getShowSeats(show, SellStatus.ENABLED));
	}

	// @Test
	public void testLockSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ChannelShow channelShow = new ChannelShow();
		Hall hall = new Hall();
		Cinema cinema = new Cinema();
		cinema.setCode(cinemaCode);
		hall.setCinema(cinema);
		channelShow.setHall(hall);
		channelShow.setCode("141000000938");
		ticketOrder.setChannelOrderCode("1000414052905411");
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("0300000100100201");
		item.setSeatCode("0300000100100301");
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.lockSeat(ticketOrder);
		log.debug("平台订单号[{}]", order.getCode());
		print(order);
	}

	// @Test
	public void testReleaseSeat() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCode("1410111000000020");
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
		show.setCode("141000000929");
		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		ticketOrder.setCinemaOrderCode("799642920125");
		ticketOrder.setCode("1410111000000013");
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("0300000101001001:80");
		ticketOrder.setMobile("18665984723");
		// TODO 重写
		// item.setServiceFee(3.0);
		// item.setSettlePrice(80.0);
		items.add(item);
		ticketOrder.setOrderItems(items);
		TicketOrder order = ticketAccessService.submitOrder(ticketOrder);
		System.out.println("order>>>>：" + order.getVoucher().getVerifyCode()
				+ "---" + order.getVoucher().getCode() + "---"
				+ order.getVoucher().getPrintCode());
	}

	@Test
	public void testOrder() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCode("1410111000000020");
		TicketOrder order = ticketAccessService.queryOrder(ticketOrder);
		print(order);
	}

	private void print(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new GenericObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String json = mapper.writeValueAsString(obj);
		log.debug(json);
	}
}
