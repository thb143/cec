package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.entity.BenefitCard;
import cn.mopon.cec.core.enums.BenefitCardStatus;

import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;
import coo.core.xstream.IEnumConverter;

/**
 * 查询卡信息响应对象。
 */
public class BenefitCardReply extends ApiReply {
	/** 手机号码 */
	private String mobile = "";
	/** 卡类编号 */
	private String typeCode = "";
	/** 卡类名称 */
	private String typeName = "";
	/** 渠道开卡订单号 */
	private String channelOrderCode = "";
	/** 有效开始日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date startDate;
	/** 有效结束日期 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date endDate;
	/** 开卡金额 */
	private Double initAmount = 0D;
	/** 总优惠次数 */
	private Integer totalDiscountCount = 0;
	/** 总可用优惠次数 */
	private Integer availableDiscountCount = 0;
	/** 当天可用优惠次数 */
	private Integer dailyAvaliableDiscountCount = 0;
	/** 卡状态（1：正常，2：冻结，3：过期） */
	@XStreamConverter(value = IEnumConverter.class, types = BenefitCardStatus.class)
	private BenefitCardStatus status;
	/** 开卡日期 */
	@XStreamConverter(value = DateConverter.class)
	private Date createDate;
	/** 首次消费影院编码 */
	private String firstCinemaCode = "";
	/** 首次消费影院名称 */
	private String firstCinemaName = "";
	/** 会员生日 */
	@XStreamConverter(value = DateConverter.class, strings = { DateUtils.DAY })
	private Date birthday;

	/**
	 * 构造方法。
	 * 
	 * @param benefitCard
	 *            权益卡
	 */
	public BenefitCardReply(BenefitCard benefitCard) {
		super();
		mobile = benefitCard.getUser().getMobile();
		typeCode = benefitCard.getType().getCode();
		typeName = benefitCard.getType().getName();
		channelOrderCode = benefitCard.getChannelOrderCode();
		startDate = benefitCard.getStartDate();
		endDate = benefitCard.getEndDate();
		initAmount = benefitCard.getInitAmount();
		totalDiscountCount = benefitCard.getTotalDiscountCount();
		availableDiscountCount = benefitCard.getAvailableDiscountCount();
		dailyAvaliableDiscountCount = benefitCard.getType()
				.getDailyDiscountCount() - benefitCard.getDailyDiscountCount();
		if (dailyAvaliableDiscountCount < 0) {
			dailyAvaliableDiscountCount = 0;
		}
		status = benefitCard.getStatus();
		createDate = benefitCard.getCreateDate();
		if (benefitCard.getFirstCinema() != null) {
			firstCinemaCode = benefitCard.getFirstCinema().getCode();
			firstCinemaName = benefitCard.getFirstCinema().getName();
		}
		birthday = benefitCard.getBirthday();
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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getChannelOrderCode() {
		return channelOrderCode;
	}

	public void setChannelOrderCode(String channelOrderCode) {
		this.channelOrderCode = channelOrderCode;
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

	public Double getInitAmount() {
		return initAmount;
	}

	public void setInitAmount(Double initAmount) {
		this.initAmount = initAmount;
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

	public Integer getDailyAvaliableDiscountCount() {
		return dailyAvaliableDiscountCount;
	}

	public void setDailyAvaliableDiscountCount(
			Integer dailyAvaliableDiscountCount) {
		this.dailyAvaliableDiscountCount = dailyAvaliableDiscountCount;
	}

	public BenefitCardStatus getStatus() {
		return status;
	}

	public void setStatus(BenefitCardStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFirstCinemaCode() {
		return firstCinemaCode;
	}

	public void setFirstCinemaCode(String firstCinemaCode) {
		this.firstCinemaCode = firstCinemaCode;
	}

	public String getFirstCinemaName() {
		return firstCinemaName;
	}

	public void setFirstCinemaName(String firstCinemaName) {
		this.firstCinemaName = firstCinemaName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}