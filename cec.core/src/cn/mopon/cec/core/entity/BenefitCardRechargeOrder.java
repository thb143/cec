package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.BenefitCardRechargeStatus;
import coo.core.hibernate.search.DateBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.model.UuidEntity;

/**
 * 权益卡续费记录。
 */
@Entity
@Table(name = "CEC_BenefitCardRechargeOrder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "BenefitCardRechargeOrder")
public class BenefitCardRechargeOrder extends UuidEntity {
	/** 关联权益卡 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cardId")
	@IndexedEmbedded(includePaths = { "id", "code", "user.id", "user.mobile",
			"type.code" })
	private BenefitCard card;
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id", "code", "name" })
	private Channel channel;
	/** 续费订单号 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 渠道续费订单号 */
	@Field(analyze = Analyze.NO)
	private String channelOrderCode;
	/** 续费金额 */
	private Double amount;
	/** 续费前的有效结束日期 */
	@Temporal(TemporalType.DATE)
	private Date oldEndDate;
	/** 续费后的有效结束日期 */
	@Temporal(TemporalType.DATE)
	private Date endDate;
	/** 续费后总优惠次数 */
	private Integer totalDiscountCount;
	/** 续费前剩余可用次数 */
	private Integer oldDiscountCount;
	/** 续费后剩余可用次数 */
	private Integer discountCount;
	/** 失效次数 */
	private Integer expireCount = 0;
	/** 状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private BenefitCardRechargeStatus status = BenefitCardRechargeStatus.PAID;
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date createDate;

	public BenefitCard getCard() {
		return card;
	}

	public void setCard(BenefitCard card) {
		this.card = card;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getOldEndDate() {
		return oldEndDate;
	}

	public void setOldEndDate(Date oldEndDate) {
		this.oldEndDate = oldEndDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getOldDiscountCount() {
		return oldDiscountCount;
	}

	public void setOldDiscountCount(Integer oldDiscountCount) {
		this.oldDiscountCount = oldDiscountCount;
	}

	public Integer getDiscountCount() {
		return discountCount;
	}

	public void setDiscountCount(Integer discountCount) {
		this.discountCount = discountCount;
	}

	public BenefitCardRechargeStatus getStatus() {
		return status;
	}

	public void setStatus(BenefitCardRechargeStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getTotalDiscountCount() {
		return totalDiscountCount;
	}

	public void setTotalDiscountCount(Integer totalDiscountCount) {
		this.totalDiscountCount = totalDiscountCount;
	}

	public Integer getExpireCount() {
		return expireCount;
	}

	public void setExpireCount(Integer expireCount) {
		this.expireCount = expireCount;
	}
}
