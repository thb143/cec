package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.SnackOrderVo;
import cn.mopon.cec.core.entity.SnackOrder;

/**
 * 查询卖品订单响应对象。
 */
public class SnackOrderReply extends ApiReply {
	/** 卖品订单 */
	private SnackOrderVo order;

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrder
	 *            订单
	 */
	public SnackOrderReply(SnackOrder snackOrder) {
		order = new SnackOrderVo(snackOrder);
	}

	public SnackOrderVo getOrder() {
		return order;
	}

	public void setOrder(SnackOrderVo order) {
		this.order = order;
	}
}