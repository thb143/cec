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
@Table(name = "CEC_BenefitCardConsumeOrder")
@Indexed(index = "BenefitCardConsumeOrder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitCardConsumeOrder {
	/** ID */
	@Id
	private String id;
	/** 关联订单 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@IndexedEmbedded(includePaths = { "code", "status", "payTime",
			"channel.code", "channelOrderCode" })
	private TicketOrder order;
	/** 关联权益卡 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cardId")
	@IndexedEmbedded(includePaths = { "id", "code", "type.id", "type.code",
			"user.id", "user.mobile" })
	private BenefitCard card;
	/** 关联卡类规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ruleId")
	private BenefitCardTypeRule rule;
	/** 优惠数量 */
	private int discountCount;
	/** 优惠金额 */
	private Double discountAmount;

	/**
	 * 创建权益卡消费订单。
	 * 
	 * @param order
	 *            选座票订单
	 * @param card
	 *            权益卡
	 * @param discountAmount
	 *            订单优惠金额
	 * @param rule
	 *            权益卡类规则
	 * @return 返回权益卡消费订单。
	 */
	public static BenefitCardConsumeOrder create(TicketOrder order,
			BenefitCard card, Double discountAmount, BenefitCardTypeRule rule) {
		BenefitCardConsumeOrder benefitCardConsumeOrder = new BenefitCardConsumeOrder();
		benefitCardConsumeOrder.setId(order.getId());
		benefitCardConsumeOrder.setCard(card);
		benefitCardConsumeOrder.setOrder(order);
		benefitCardConsumeOrder.setRule(rule);
		benefitCardConsumeOrder.setDiscountCount(order.getTicketCount()
				+ order.getSnackCount());
		benefitCardConsumeOrder.setDiscountAmount(discountAmount);
		return benefitCardConsumeOrder;
	}

	/**
	 * 获取规则名称。
	 * 
	 * @return 返回规则名称。
	 */
	public String getRuleName() {
		return getRule().getName();
	}

	/**
	 * 获取规则颜色。
	 * 
	 * @return 返回规则颜色。
	 */
	public String getRuleColor() {
		return getRule().getEnabled().getColor();
	}

	/**
	 * 获取规则摘要。
	 * 
	 * @return 返回影院结算规则摘要。
	 */
	public String getRuleSummary() {
		return getRule().getSummary();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TicketOrder getOrder() {
		return order;
	}

	public void setOrder(TicketOrder order) {
		this.order = order;
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

	public BenefitCardTypeRule getRule() {
		return rule;
	}

	public void setRule(BenefitCardTypeRule rule) {
		this.rule = rule;
	}

}
