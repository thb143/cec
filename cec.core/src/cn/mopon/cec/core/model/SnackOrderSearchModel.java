package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.TicketOrderStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 卖品订单搜索模型。
 */
public class SnackOrderSearchModel extends DateRangeSearchModel {
	/** 消费状态 */
	private TicketOrderStatus consumeStatus;
	/** 渠道编号 */
	private String channelCode;
	/** 卡类号 */
	private String benefitCardTypeCode;
	/** 订单类型 */
	private String orderType;
	/** 卖品状态 */
	private String SnackStatus;

	public TicketOrderStatus getConsumeStatus() {
		return consumeStatus;
	}

	public void setConsumeStatus(TicketOrderStatus consumeStatus) {
		this.consumeStatus = consumeStatus;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getBenefitCardTypeCode() {
		return benefitCardTypeCode;
	}

	public void setBenefitCardTypeCode(String benefitCardTypeCode) {
		this.benefitCardTypeCode = benefitCardTypeCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSnackStatus() {
		return SnackStatus;
	}

	public void setSnackStatus(String snackStatus) {
		SnackStatus = snackStatus;
	}

}