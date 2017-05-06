package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * 权益卡消费订单。
 */
@Entity
@Table(name = "CEC_BenefitCardConsumeSnackOrder")
@Indexed(index = "BenefitCardConsumeSnackOrder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitCardConsumeSnackOrder {
	/** ID */
	@Id
	private String id;
	/** 关联订单 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@IndexedEmbedded(includePaths = { "code", "status", "createTime",
			"channel.code", "channelOrderCode" })
	private SnackOrder snackOrder;
	/** 关联权益卡 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cardId")
	@IndexedEmbedded(includePaths = { "id", "code", "type.id", "type.code",
			"user.id", "user.mobile" })
	private BenefitCard card;
	/** 优惠数量 */
	private int discountCount;
	/** 优惠金额 */
	private Double discountAmount;

	/**
	 * 创建权益卡消费订单。
	 * 
	 * @param order
	 *            卖品订单
	 * @param card
	 *            权益卡
	 * @param discountAmount
	 *            订单优惠金额
	 * @param rule
	 *            权益卡类规则
	 * @return 返回权益卡消费订单。
	 */
	public static BenefitCardConsumeSnackOrder create(SnackOrder order,
			BenefitCard card, Double discountAmount) {
		BenefitCardConsumeSnackOrder benefitCardConsumeOrder = new BenefitCardConsumeSnackOrder();
		benefitCardConsumeOrder.setId(order.getId());
		benefitCardConsumeOrder.setCard(card);
		benefitCardConsumeOrder.setSnackOrder(order);
		benefitCardConsumeOrder.setDiscountCount(order.getSnackCount());
		benefitCardConsumeOrder.setDiscountAmount(discountAmount);
		return benefitCardConsumeOrder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SnackOrder getSnackOrder() {
		return snackOrder;
	}

	public void setSnackOrder(SnackOrder snackOrder) {
		this.snackOrder = snackOrder;
	}

	public BenefitCard getCard() {
		return card;
	}

	public void setCard(BenefitCard card) {
		this.card = card;
	}

	public int getDiscountCount() {
		return discountCount;
	}

	public void setDiscountCount(int discountCount) {
		this.discountCount = discountCount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
}