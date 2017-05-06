package cn.mopon.cec.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.service.TicketOrderService;
import coo.core.util.SpringUtils;

/**
 * 选座票订单同步任务。
 */
public class TicketOrderSyncTask implements Callable<Void> {
	private Logger log = LoggerFactory.getLogger(getClass());
	private String orderId;

	/**
	 * 构造方法。
	 * 
	 * @param orderId
	 *            订单ID
	 */
	public TicketOrderSyncTask(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public Void call() throws Exception {
		try {
			TicketOrderService ticketOrderService = SpringUtils
					.getBean("ticketOrderService");
			ticketOrderService.proccessAbnormalOrder(orderId);
		} catch (Exception e) {
			log.error("定时处理异常订单时发生异常。", e);
		}
		return null;
	}
}