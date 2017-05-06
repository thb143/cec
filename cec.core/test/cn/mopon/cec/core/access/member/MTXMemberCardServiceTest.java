package cn.mopon.cec.core.access.member;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.access.member.vo.SeatInfo;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.Provider;
import coo.base.util.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ticketServiceTestContext.xml" })
public class MTXMemberCardServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String CARD_CODE = "00001919";
	private MemberSettings settings;
	private MemberAccessService cardAccessService;

	@Before
	public void init() {
		settings = new MemberSettings();
		settings.setUrl("http://member.mvtapi.com:8310/cmtspay/services/payapi?wsdl");
		settings.setUsername("cmtsxx");
		settings.setPassword("123456");
		settings.getAccessType().setProvider(Provider.MTX);
		cardAccessService = MemberAccessServiceFactory
				.getMemberService(settings);
	}

	// 会员卡登入
	// @Test
	public void loginCard() throws Exception {
		MemberCard memberCard = new MemberCard("gzxxx", CARD_CODE, "123123");
		MemberCard card = cardAccessService.getVerifyCard(memberCard);
		System.out.println("影院编码：" + card.getCinemaCode());
		System.out.println("会员卡号：" + card.getCardCode());
		System.out.println("账户余额：" + card.getBalance());
		System.out.println("账户积分：" + card.getScore());
		System.out.println("账户级别名称：" + card.getAccLevelName());
		System.out.println("过期时间：" + card.getExpirationTime());
		System.out.println("是否可用："
				+ (card.getStatus().equals("0") ? "可用" : "不可用"));

	}

	// 会员卡充值
	// @Test
	public void cardRecharge() throws Exception {
		MemberCard memberCard = new MemberCard("gzxxx", CARD_CODE, "123123");
		Double cardRechargeReply = cardAccessService.getMemberCardRecharge(
				memberCard, "100", "");
		System.out.println("账户余额：" + cardRechargeReply);

	}

	// 获取会员卡信息（芯片号）
	// @Test
	public void queryCardByChip() throws Exception {
		MemberCard memberCard = new MemberCard("gzxxx", "5t1236wq5");
		MemberCard card = cardAccessService.getMemberCardInfoByChip(memberCard);
		log.debug("Card:" + card.getCardCode());
		System.out.println("影院编码：" + card.getCinemaCode());
		System.out.println("会员卡号：" + card.getCardCode());
		System.out.println("账户余额：" + card.getBalance());
		System.out.println("账户积分：" + card.getScore());
		System.out.println("账户级别名称：" + card.getAccLevelName());
		System.out.println("过期时间：" + card.getExpirationTime());
		System.out.println("是否可用："
				+ (card.getStatus().equals("0") ? "可用" : "不可用"));
	}

	/*
	 * // 获取会员卡信息（会员卡号） // @Test public void queryCardByCode() throws Exception
	 * { Card card = cardAccessService.queryCardByCode("gzxxx", CARD_CODE,
	 * "123123"); System.out.println("影院编码：" + card.getCinemaCode());
	 * System.out.println("会员卡号：" + card.getCardCode());
	 * System.out.println("账户余额：" + card.getBalance());
	 * System.out.println("账户积分：" + card.getScore());
	 * System.out.println("账户级别名称：" + card.getAccLevelName());
	 * System.out.println("过期时间：" + card.getExpirationTime());
	 * System.out.println("是否可用：" + (card.getStatus().equals("0") ? "可用" :
	 * "不可用")); }
	 */

	// 获取会员卡折扣
	// @Test
	public void getDiscount() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCode("953581046944");
		ticketOrder.setMobile("");
		ticketOrder.setAmount(200.3);
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		// Film film = new Film();
		// film.setCode("001103362014");
		Cinema cinema = new Cinema();
		cinema.setCode("gzxxx");
		hall.setCinema(cinema);
		hall.setCode("02");
		show.setHall(hall);
		show.setCode("76300096");
		// show.setFilm(film);
		show.setShowTime(DateUtils.parse("2014-08-31 21:40"));

		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("02010104");
		item.setSalePrice(100.0);
		items.add(item);
		TicketOrderItem item1 = new TicketOrderItem();
		item1.setSeatCode("02010105");
		item1.setSalePrice(100.0);
		items.add(item1);
		ticketOrder.setOrderItems(items);
		MemberCard memberCard = new MemberCard("gzxxx", CARD_CODE, "123123");
		List<SeatInfo> seatInfoList = cardAccessService.getDiscountPrice(
				ticketOrder, memberCard);
		System.out.println("折扣：" + seatInfoList.get(0).getTicketPrice());
	}

	// 会员卡扣款信息
	@Test
	public void cardPay() throws Exception {
		TicketOrder ticketOrder = new TicketOrder();
		ticketOrder.setCode("1410171000000032");
		ticketOrder.setMobile("");
		ticketOrder.setAmount(200.3);
		// TicketGoods ticketChannel = new TicketGoods();
		// TicketProduct ticket = new TicketProduct();
		Show show = new Show();
		Hall hall = new Hall();
		// Film film = new Film();
		// film.setCode("001103362014");
		Cinema cinema = new Cinema();
		cinema.setCode("gzxxx");
		hall.setCinema(cinema);
		hall.setCode("02");
		show.setHall(hall);
		show.setCode("76300096");
		// show.setFilm(film);

		// ticket.setShow(show);
		// ticketChannel.setProduct(ticket);
		// ticketOrder.setGoods(ticketChannel);
		ticketOrder.setTicketCount(1);
		List<TicketOrderItem> items = new ArrayList<TicketOrderItem>();
		TicketOrderItem item = new TicketOrderItem();
		item.setSeatCode("02010104");
		item.setSalePrice(100.0);
		items.add(item);
		TicketOrderItem item1 = new TicketOrderItem();
		item1.setSeatCode("02010105");
		item1.setSalePrice(100.0);
		items.add(item);

		ticketOrder.setOrderItems(items);
		MemberCard memberCard = new MemberCard("gzxxx", CARD_CODE, "123123");
		TicketOrder remoteTicketOrder = cardAccessService.memberCardPay(
				ticketOrder, memberCard);
		System.out.println("订单号：" + remoteTicketOrder.getCode());
	}
}
