package cn.mopon.cec.core.access.member.ng;

import cn.mopon.cec.core.access.member.ng.vo.MemberData;

/**
 * 订单列表响应对象。
 */
public class OrdersReply extends NgReply {
	private MemberData data;

	public MemberData getData() {
		return data;
	}

	public void setData(MemberData data) {
		this.data = data;
	}
}
