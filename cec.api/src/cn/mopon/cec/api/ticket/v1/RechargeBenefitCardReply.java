package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.entity.BenefitCardRechargeOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;

/**
 * 续费响应对象。
 */
@JsonInclude(Include.NON_NULL)
public class RechargeBenefitCardReply extends ApiReply {
	/** 续费订单号 */
	private String orderCode;
	/** 续费后有效截止日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	@JsonFormat(pattern = DateUtils.DAY, timezone = "Asia/Shanghai")
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
	 * @param rechargeOrder
	 *            续费订单
	 */
	public RechargeBenefitCardReply(BenefitCardRechargeOrder rechargeOrder) {
		this.orderCode = rechargeOrder.getCode();
		this.endDate = rechargeOrder.getEndDate();
		this.oldAvailableDiscountCount = rechargeOrder.getOldDiscountCount();
		this.totalDiscountCount = rechargeOrder.getTotalDiscountCount();
		this.availableDiscountCount = rechargeOrder.getDiscountCount();
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