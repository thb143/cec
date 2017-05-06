package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.BenefitCardStatus;
import coo.base.util.DateUtils;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 权益卡。
 */
@Entity
@Table(name = "CEC_BenefitCard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "BenefitCard")
public class BenefitCard extends UuidEntity {
	/** 关联类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private BenefitCardType type;
	/** 关联用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	@IndexedEmbedded(includePaths = { "id", "mobile", "channel.id" })
	private BenefitCardUser user;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Channel channel;
	/** 卡号 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 渠道开卡订单号 */
	@Field(analyze = Analyze.NO)
	private String channelOrderCode;
	/** 有效期开始日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date startDate;
	/** 有效期结束日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date endDate;
	/** 续费次数 */
	private Integer rechargeCount = 0;
	/** 开卡金额 */
	private Double initAmount;
	/** 总优惠次数 */
	private Integer totalDiscountCount;
	/** 剩余优惠次数 */
	private Integer availableDiscountCount = 0;
	/** 当天已使用的优惠次数 */
	@Transient
	private Integer dailyDiscountCount;
	/** 已优惠金额 */
	private Double discountAmount = 0.0D;
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private BenefitCardStatus status = BenefitCardStatus.NORMAL;
	/** 首次消费影院 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "firstCinemaId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Cinema firstCinema;
	/** 出生日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date birthday;
	/** 乐观锁版本号 */
	@Version
	private Integer version = 0;
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date createDate;

	/**
	 * 判断权益卡是否有效，状态为正常，在有效期内。
	 * 
	 * @return 返回权益卡是否有效。
	 */
	public Boolean isValid() {
		return getChannel().getOpened() && status == BenefitCardStatus.NORMAL
				&& startDate.before(new Date())
				&& endDate.after(DateUtils.getPrevDay());
	}

	/**
	 * 判断权益卡是否过期。
	 * 
	 * @return 过期返回true，未过期返回false。
	 */
	public Boolean isExpire() {
		return DateUtils.getToday().after(endDate);
	}

	public BenefitCardType getType() {
		return type;
	}

	public void setType(BenefitCardType type) {
		this.type = type;
	}

	public BenefitCardUser getUser() {
		return user;
	}

	public void setUser(BenefitCardUser user) {
		this.user = user;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getRechargeCount() {
		return rechargeCount;
	}

	public void setRechargeCount(Integer rechargeCount) {
		this.rechargeCount = rechargeCount;
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

	public Integer getDailyDiscountCount() {
		return dailyDiscountCount;
	}

	public void setDailyDiscountCount(Integer dailyDiscountCount) {
		this.dailyDiscountCount = dailyDiscountCount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BenefitCardStatus getStatus() {
		return status;
	}

	public void setStatus(BenefitCardStatus status) {
		this.status = status;
	}

	public Cinema getFirstCinema() {
		return firstCinema;
	}

	public void setFirstCinema(Cinema firstCinema) {
		this.firstCinema = firstCinema;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}