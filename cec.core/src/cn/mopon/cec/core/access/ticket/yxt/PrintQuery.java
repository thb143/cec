package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 查询订单请求对象。
 */
public class PrintQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public PrintQuery(TicketOrder order) {
		setAction("queryPrintByVerifyCode.xml");
		addParam("cinemaCode", order.getCinema().getCode());
		addParam("printCode", order.getVoucher().getPrintCode());
		addParam("verifyCode", order.getVoucher().getVerifyCode());
	}
}
