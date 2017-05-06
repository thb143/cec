package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.entity.TicketOrder;

/**
 * 查询订单请求对象。
 */
public class ConfirmPrintQuery extends YxtQuery {
	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 */
	public ConfirmPrintQuery(TicketOrder order) {
		setAction("confirmPrint.xml");
		addParam("cinemaCode", order.getCinema().getCode());
		addParam("orderCode", order.getCinemaOrderCode());
		addParam("printCode", order.getVoucher().getPrintCode());
		addParam("verifyCode", order.getVoucher().getVerifyCode());
	}
}
