package cn.mopon.cec.core.access.member;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.MemberAccessType;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.Provider;

public class HLNMemberCardServiceTest {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	@SuppressWarnings("unused")
	private static final String CARD_CODE = "00001919";
	private MemberSettings settings;
	private MemberAccessService cardAccessService;

	@Before
	public void init() {
		settings = new MemberSettings();
		settings.setUrl("http://192.168.9.59:9080/scts.service/webservice/pay?wsdl");
		settings.setUsername("999001");
		settings.setPassword("123456");
		settings.setAccessType(new MemberAccessType());
		settings.getAccessType().setProvider(Provider.NG);
		cardAccessService = MemberAccessServiceFactory
				.getMemberService(settings);
	}

	@Test
	public void discountPriceQueryTest() {
		MemberCard memberCard = new MemberCard();
		memberCard.setCardCode("999001");
		TicketOrder ticketOrder = new TicketOrder();
		TicketOrderItem items1 = new TicketOrderItem();
		items1.setCinemaPrice(33.0);
		items1.setSeatCode("0300000100100101");
		TicketOrderItem items2 = new TicketOrderItem();
		items2.setCinemaPrice(33.0);
		items2.setSeatCode("0300000100100301");
		List<TicketOrderItem> orderItems = new ArrayList<TicketOrderItem>();
		orderItems.add(items1);
		// orderItems.add(items2);
		ticketOrder.setOrderItems(orderItems);
		ticketOrder.setTicketCount(orderItems.size());
		ticketOrder.setShowCode("1410161000000011");
		cardAccessService.getDiscountPrice(ticketOrder, memberCard);
	}

	// @Test
	public void memberCardPayTest() {
		MemberCard memberCard = new MemberCard();
		memberCard.setCardCode("999001");
		memberCard.setPassword("123456");
		TicketOrder ticketOrder = new TicketOrder();
		TicketOrderItem items1 = new TicketOrderItem();
		items1.setServiceFee(0.0);
		items1.setCinemaPrice(102.0);
		items1.setSalePrice(91.8);
		items1.setSeatCode("0300000101001001");
		TicketOrderItem items2 = new TicketOrderItem();
		items2.setServiceFee(0.0);
		items2.setCinemaPrice(102.0);
		items2.setSeatCode("0300000100100301");
		items2.setSalePrice(91.8);
		List<TicketOrderItem> orderItems = new ArrayList<TicketOrderItem>();
		orderItems.add(items1);
		// orderItems.add(items2);
		ticketOrder.setOrderItems(orderItems);
		ticketOrder.setTicketCount(orderItems.size());
		ticketOrder.setShowCode("0514101000100802");
		ticketOrder.setCinemaOrderCode("66453383131939059233");
		cardAccessService.memberCardPay(ticketOrder, memberCard);
	}

	// @Test
	public void memberCardInfoTest() {
		MemberCard memberCard = new MemberCard();
		memberCard.setCardCode("999001");
		cardAccessService.getMemberCardInfo(memberCard);
	}
}
