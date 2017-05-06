package cn.mopon.cec.core.access.member.dx;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.util.NumberUtils;

/**
 * 会员卡折扣价响应对象。
 */
public class DiscountPriceReply extends DXReply {
	/** 订单折扣后总价 */
	private Double discount;

	/**
	 * 构造方法。
	 */
	public DiscountPriceReply() {
		xstream.alias("root", DiscountPriceReply.class);
		xstream.alias("root", DXReply.class, DiscountPriceReply.class);
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	/**
	 * 拼装订单座位信息字符串（座位号：销售价，多个逗号隔开）
	 * 
	 * @param order
	 *            订单
	 * @return 座位信息字符串。
	 */
	public String getSeatInfoStr(TicketOrder order) {
		Double itemAmount = NumberUtils.div(discount, Double
				.parseDouble(Integer.toString(order.getOrderItems().size())));
		int num = 1;
		Double itemSumAmount = 0d;
		String seatInfoStr = "";
		// 销售总价除以总票数，余数加到最后一张票上
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			if (order.getOrderItems().size() == num) {
				itemAmount = NumberUtils.sub(discount, itemSumAmount);
				seatInfoStr = seatInfoStr
						+ orderItem.getSeatCode()
						+ ":"
						+ NumberUtils
								.add(itemAmount, orderItem.getCircuitFee());
				break;
			}
			seatInfoStr = seatInfoStr + orderItem.getSeatCode() + ":"
					+ NumberUtils.add(itemAmount, orderItem.getCircuitFee())
					+ ",";
			itemSumAmount = NumberUtils.add(itemSumAmount, itemAmount);
			num++;
		}
		return seatInfoStr;
	}

}
