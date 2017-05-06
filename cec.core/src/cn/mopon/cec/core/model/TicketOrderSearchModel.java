package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.TicketOrderStatus;
import cn.mopon.cec.core.enums.TicketOrderType;
import coo.core.model.DateRangeSearchModel;

/**
 * 订单管理模块搜索模型。
 */
public class TicketOrderSearchModel extends DateRangeSearchModel {
	private TicketOrderStatus orderStatus;
	private TicketOrderType ticketOrderType;
	private String channelCode;
	private Boolean hasSnack;

	public TicketOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(TicketOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public TicketOrderType getTicketOrderType() {
		return ticketOrderType;
	}

	public void setTicketOrderType(TicketOrderType ticketOrderType) {
		this.ticketOrderType = ticketOrderType;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Boolean getHasSnack() {
		return hasSnack;
	}

	public void setHasSnack(Boolean hasSnack) {
		this.hasSnack = hasSnack;
	}
}