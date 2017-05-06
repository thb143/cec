package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.entity.BenefitCard;

import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;

/**
 * 查询开卡订单响应对象。
 */
public class OpenBenefitCardOrderReply extends ApiReply {
	/** 用户手机号 */
	private String mobile;
	/** 卡类编号 */
	private String typeCode;
	/** 有效开始日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date startDate;
	/** 有效截止日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date endDate;
	/** 总优惠次数 */
	private Integer totalDiscountCount;

	/**
	 * 构造方法。
	 * 
	 * @param benefitCard
	 *            卡
	 */
	public OpenBenefitCardOrderReply(BenefitCard benefitCard) {
		mobile = benefitCard.getUser().getMobile();
		typeCode = benefitCard.getType().getCode();
		startDate = benefitCard.getStartDate();
		endDate = benefitCard.getEndDate();
		totalDiscountCount = benefitCard.getTotalDiscountCount();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
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