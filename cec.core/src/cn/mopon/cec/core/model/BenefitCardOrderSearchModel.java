package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.BenefitCardRechargeStatus;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import coo.core.model.DateRangeSearchModel;

/**
 * 权益卡订单搜索模型。
 */
public class BenefitCardOrderSearchModel extends DateRangeSearchModel {
	/** 消费状态 */
	private TicketOrderStatus consumeStatus;
	/** 续费状态 */
	private BenefitCardRechargeStatus rechargeStatus;
	/** 渠道编号 */
	private String channelCode;
	/** 卡类号 */
	private String benefitCardTypeCode;
	/** 订单类型 */
	private String orderType;

	public TicketOrderStatus getConsumeStatus() {
		return consumeStatus;
	}

	public void setConsumeStatus(TicketOrderStatus consumeStatus) {
		this.consumeStatus = consumeStatus;
	}

	public BenefitCardRechargeStatus getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(BenefitCardRechargeStatus rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
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
}