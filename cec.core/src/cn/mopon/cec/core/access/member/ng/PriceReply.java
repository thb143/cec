package cn.mopon.cec.core.access.member.ng;

import cn.mopon.cec.core.access.member.ng.vo.OrderDiscount;

/**
 * 价格查询响应对象。
 */
public class PriceReply extends NgReply {
	/** 价格vo */
	private OrderDiscount data;

	public OrderDiscount getData() {
		return data;
	}

	public void setData(OrderDiscount data) {
		this.data = data;
	}

}
