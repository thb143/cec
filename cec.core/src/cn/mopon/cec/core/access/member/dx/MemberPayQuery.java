package cn.mopon.cec.core.access.member.dx;

import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.dx.vo.ShowVo;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.constants.Chars;
import coo.base.util.NumberUtils;

/**
 * 会员卡消费扣款请求对象。
 */
public class MemberPayQuery extends DXQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param userName
	 *            用户名
	 * @param accessToken
	 *            auth2.0 token
	 */
	public MemberPayQuery(TicketOrder order, String userName, String accessToken) {
		super.setAction("seat/lock-buy");
		ShowVo vo = new ShowVo(order.getShowCode());
		params.add(new BasicNameValuePair("cid", order.getCinema().getCode()));
		params.add(new BasicNameValuePair("play_id", vo.getId()));
		params.add(new BasicNameValuePair("seat", genOrderSeatAndPrice(order)));
		// seat/lock/接口返回的字符串（lockFlag）
		params.add(new BasicNameValuePair("lock_flag", order
				.getCinemaOrderCode()));
		params.add(new BasicNameValuePair("play_update_time", vo
				.getCineUpdateTime()));
		params.add(new BasicNameValuePair("partner_buy_ticket_id", order
				.getCode()));
		params.add(new BasicNameValuePair("access_token", accessToken));
		params.add(new BasicNameValuePair("pid", userName));
	}

	/**
	 * 生成订单座位。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回订单座位。
	 */
	private String genOrderSeatAndPrice(TicketOrder order) {
		StringBuilder sb = new StringBuilder();
		for (TicketOrderItem item : order.getOrderItems()) {
			if (sb.length() > 0) {
				sb.append(Chars.COMMA);
			}
			// seat_id-handle_fee-price（（不包含手续费）价格）-ticket_type（影票类型）-is_discount（0:普通票，1:会员票，2:全局会员票）
			sb.append(item.getSeatCode())
					.append("-")
					// 销售价 - 票房价 = 会员手续费
					.append(NumberUtils.sub(item.getSalePrice(),
							item.getSubmitPrice())).append("-")
					.append(item.getSubmitPrice()).append("-1-1");
		}
		return sb.toString();
	}

}
