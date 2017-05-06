package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.entity.BenefitCard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;

/**
 * 开卡响应对象。
 */
public class OpenBenefitCardReply extends ApiReply {
	/** 有效开始日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	@JsonFormat(pattern = DateUtils.DAY, timezone = "Asia/Shanghai")
	private Date startDate;
	/** 有效截止日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	@JsonFormat(pattern = DateUtils.DAY, timezone = "Asia/Shanghai")
	private Date endDate;
	/** 总优惠次数 */
	private Integer totalDiscountCount;

	/**
	 * 构造方法。
	 * 
	 * @param benefitCard
	 *            权益卡
	 */
	public OpenBenefitCardReply(BenefitCard benefitCard) {
		this.startDate = benefitCard.getStartDate();
		this.endDate = benefitCard.getEndDate();
		this.totalDiscountCount = benefitCard.getTotalDiscountCount();
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
}