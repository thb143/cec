package cn.mopon.cec.core.access.ticket.hfh;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import coo.core.xstream.GenericXStream;

/**
 * 锁座响应信息。
 */
@XStreamAlias("data")
public class LockSeatReply extends HfhReply {
	private static XStream xstream;
	private static TicketOrder order = new TicketOrder();

	static {
		xstream = new GenericXStream();
		xstream.alias("data", LockSeatReply.class);
		xstream.alias("data", HfhReply.class, LockSeatReply.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取订单。
	 * 
	 * @param remoteOrder
	 *            地面订单
	 * @return 返回订单对象。
	 */
	public TicketOrder getOrder(TicketOrder remoteOrder) {
		order.setCinemaOrderCode(remoteOrder.getCinemaOrderCode());
		return order;
	}

}