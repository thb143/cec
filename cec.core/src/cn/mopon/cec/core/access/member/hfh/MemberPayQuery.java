package cn.mopon.cec.core.access.member.hfh;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.constants.Chars;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;
import coo.base.util.StringUtils;

/**
 * 会员卡消费扣款请求对象。
 */
public class MemberPayQuery extends HFHQuery {
	private static final String TICKETTYPE = "成人票";

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @param memberCard
	 *            会员卡信息
	 */
	public MemberPayQuery(TicketOrder ticketOrder, MemberCard memberCard) {
		super.setMethod("fixMemberCardOrder");
		String showTime = DateUtils.format(ticketOrder.getShowTime(),
				DateUtils.MINUTE);
		String date = DateUtils
				.format(ticketOrder.getShowTime(), DateUtils.DAY);
		String time = StringUtils.substringAfter(showTime, " ");
		basicNameValuePair("orderNo", ticketOrder.getCinemaOrderCode());
		basicNameValuePair("ticketCount",
				String.valueOf(ticketOrder.getTicketCount()));
		basicNameValuePair("cinemaId", ticketOrder.getCinema().getCode());
		basicNameValuePair("hallId", ticketOrder.getHall().getCode());
		basicNameValuePair("filmId", ticketOrder.getFilmCode());
		basicNameValuePair("showSeqNo", ticketOrder.getShowCode().split("_")[0]);
		basicNameValuePair("showDate", date);
		basicNameValuePair("showTime", time.replaceFirst(":", ""));
		basicNameValuePair("ticketTypeList",
				getTicketType(ticketOrder.getTicketCount()));
		basicNameValuePair("priceList",
				StringUtils.join(getPrices(ticketOrder), "|"));
		basicNameValuePair("feeList", getFees(ticketOrder));
		basicNameValuePair("cardFacadeCd", memberCard.getCardCode());
		basicNameValuePair("cardPass", memberCard.getPassword());
		basicNameValuePair("disTicketCount",
				String.valueOf(ticketOrder.getTicketCount()));
		basicNameValuePair("randKey", getFixLenthString(8));
		basicNameValuePair("checkValue", getCheckValue(nvps));
	}

	/**
	 * 影票名称。
	 * 
	 * @param ticketCount
	 *            订票数量
	 * @return 影票名称
	 */
	private String getTicketType(int ticketCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ticketCount; i++) {
			if (sb.length() > 0) {
				sb.append(Chars.BAR).append(TICKETTYPE);
			} else {
				sb.append(TICKETTYPE);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取座位票价列表。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回座位票价列表。
	 */
	public List<String> getPrices(TicketOrder ticketOrder) {
		List<String> prices = new ArrayList<String>();
		for (TicketOrderItem item : ticketOrder.getOrderItems()) {
			prices.add(item.getSubmitPrice().toString());
		}
		return prices;
	}

	/**
	 * 获取座位手续费列表。
	 * 
	 * @param ticketOrder
	 *            选座票订单
	 * @return 返回座位手续费列表。
	 */
	private String getFees(TicketOrder ticketOrder) {
		List<String> fees = new ArrayList<String>();
		for (TicketOrderItem order : ticketOrder.getOrderItems()) {
			// 销售价 - 票房价 = 会员手续费
			fees.add(String.valueOf(NumberUtils.sub(order.getSalePrice(),
					order.getSubmitPrice())));
		}
		return StringUtils.join(fees, Chars.BAR);
	}
}
