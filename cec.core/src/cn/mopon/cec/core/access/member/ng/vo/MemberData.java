package cn.mopon.cec.core.access.member.ng.vo;

import java.util.List;

/**
 * 查询订单data。
 */
public class MemberData {
	/** 会员订单vo */
	private List<MemberOrder> orders;

	public List<MemberOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<MemberOrder> data) {
		this.orders = data;
	}
}
