package cn.mopon.cec.core.access.member.hfh;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;

/**
 * 会员卡折扣价请求对象。
 */
public class DiscountPriceQuery extends HFHQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param cardCode
	 *            会员卡号
	 */
	public DiscountPriceQuery(TicketOrder order, String cardCode) {
		super.setMethod("qryPrice");
		String showTime = DateUtils.format(order.getShowTime(),
				DateUtils.MINUTE);
		nvps.add(new BasicNameValuePair("cinemaId", order.getCinema().getCode()));
		String date = DateUtils.format(order.getShowTime(), DateUtils.DAY);
		nvps.add(new BasicNameValuePair("showDate", date));
		nvps.add(new BasicNameValuePair("filmId", order.getFilmCode()));
		String time = StringUtils.substringAfter(showTime, " ");
		nvps.add(new BasicNameValuePair("showTime", time.replaceFirst(":", "")));
		nvps.add(new BasicNameValuePair("hallId", order.getHall().getCode()));
		nvps.add(new BasicNameValuePair("sectionId", order.getOrderItems()
				.get(0).getSeatGroupCode()));
		nvps.add(new BasicNameValuePair("throughFlg", "N"));
		nvps.add(new BasicNameValuePair("throughSeq", "1"));
		nvps.add(new BasicNameValuePair("soldBySystem", "WEB"));
		nvps.add(new BasicNameValuePair("cardNum", cardCode));
	}
}
