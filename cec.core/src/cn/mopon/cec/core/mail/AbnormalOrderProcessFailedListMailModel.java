package cn.mopon.cec.core.mail;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.User;
import cn.mopon.cec.core.mail.vo.AbnormalOrderVo;

/**
 * 异常订单处理失败通知。
 */
public class AbnormalOrderProcessFailedListMailModel extends MailModel {
	private List<AbnormalOrderVo> abnormalOrders = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param ticketOrders
	 *            选座票订单列表
	 * @param users
	 *            邮件接收用户
	 */
	public AbnormalOrderProcessFailedListMailModel(
			List<TicketOrder> ticketOrders, List<User> users) {
		super(users);
		templateName = "abnormal-order-process-failed-list-mail.ftl";
		subject = "异常订单处理失败通知";
		for (TicketOrder ticketOrder : ticketOrders) {
			abnormalOrders.add(new AbnormalOrderVo(ticketOrder));
		}
	}

	public List<AbnormalOrderVo> getAbnormalOrders() {
		return abnormalOrders;
	}

	public void setAbnormalOrders(List<AbnormalOrderVo> abnormalOrders) {
		this.abnormalOrders = abnormalOrders;
	}
}