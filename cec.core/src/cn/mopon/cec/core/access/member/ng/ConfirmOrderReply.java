package cn.mopon.cec.core.access.member.ng;

import cn.mopon.cec.core.access.member.ng.vo.OrderVo;

/**
 * 确认订单响应对象。
 */
public class ConfirmOrderReply extends NgReply {
	/** 订单vo */
	private OrderVo data;

	public OrderVo getData() {
		return data;
	}

	public void setData(OrderVo data) {
		this.data = data;
	}

}
