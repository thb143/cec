package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.entity.BenefitCardRechargeOrder;

import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;

/**
 * 查询续费订单响应对象。
 */
public class RechargeBenefitCardOrderReply extends ApiReply {
	/** 续费订单号 */
	private String orderCode;
	/** 续费后有效截止日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date endDate;
	/** 续费前剩余优惠次数 */
	private Integer oldAvailableDiscountCount;
	/** 续费后总优惠次数 */
	private Integer totalDiscountCount;
	/** 续费后剩余优惠次数 */
	private Integer availableDiscountCount;

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            续费订单
	 */
	public RechargeBenefitCardOrderReply(BenefitCardRechargeOrder order) {
		orderCode = order.getCode();
		endDate = order.getEndDate();
		oldAvailableDiscountCount = order.getOldDiscountCount();
		totalDiscountCount = order.getTotalDiscountCount();
		availableDiscountCount = order.getDiscountCount();
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getTotalDiscountCount() {
		return totalDiscountCount;
	}

	public void setTotalDiscountCount(Integer totalDiscountCount) {
		this.totalDiscountCount = totalDiscountCount;
	}

	public Integer getAvailableDiscountCount() {
		return availableDiscountCount;
	}

	public void setAvailableDiscountCount(Integer availableDiscountCount) {
		this.availableDiscountCount = availableDiscountCount;
	}

	public Integer getOldAvailableDiscountCount() {
		return oldAvailableDiscountCount;
	}

	public void setOldAvailableDiscountCount(Integer oldAvailableDiscountCount) {
		this.oldAvailableDiscountCount = oldAvailableDiscountCount;
	}
}