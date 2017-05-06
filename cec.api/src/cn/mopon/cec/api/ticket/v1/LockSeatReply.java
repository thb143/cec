package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 锁座响应对象。
 */
@JsonInclude(Include.NON_NULL)
public class LockSeatReply extends ApiReply {
	/** 订单号 */
	private String orderCode;
	/** 锁座时间（分钟） */
	private Integer lockTime;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getLockTime() {
		return lockTime;
	}

	public void setLockTime(Integer lockTime) {
		this.lockTime = lockTime;
	}
}