package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import coo.base.util.NumberUtils;
import coo.core.model.UuidEntity;

/**
 * 卖品订单明细。
 */
@Entity
@Table(name = "CEC_SnackOrderItem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnackOrderItem extends UuidEntity {
	/** 关联订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId")
	private SnackOrder snackOrder;
	/** 关联卖品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "snackId")
	private Snack snack;
	/** 关联渠道结算规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "snackRuleId")
	private BenefitCardTypeSnackRule snackRule;
	/** 销售价 */
	private Double salePrice = 0D;
	/** 标准价 */
	private Double stdPrice = 0D;
	/** 影院结算价 */
	private Double cinemaPrice = 0D;
	/** 渠道结算价 */
	private Double channelPrice = 0D;
	/** 接入费 */
	private Double connectFee = 0D;
	/** 渠道费 */
	private Double channelFee = 0D;
	/** 优惠金额 */
	private Double discountPrice = 0D;
	/** 数量 */
	private int count = 0;

	/**
	 * 创建选座票订单卖品明细。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 * @param snackInfos
	 *            卖品信息
	 * @return 返回选座票订单卖品明细。
	 */
	public static SnackOrderItem create(SnackChannel snackChannel,
			String snackInfos) {
		SnackOrderItem orderItem = new SnackOrderItem();
		orderItem.setSnack(snackChannel.getSnack());
		orderItem.setStdPrice(snackChannel.getSnack().getStdPrice());
		orderItem.setCinemaPrice(snackChannel.getSnack().getSubmitPrice());
		orderItem.setChannelPrice(snackChannel.getSettlePrice());
		orderItem.setConnectFee(snackChannel.getConnectFee());
		String[] snackItems = snackInfos.split(":");
		orderItem.setSalePrice(NumberUtils.halfUp(Double
				.parseDouble(snackItems[1])));
		orderItem.setCount(Integer.parseInt(snackItems[2]));
		orderItem.setChannelFee(NumberUtils.sub(orderItem.getSalePrice(),
				orderItem.getChannelPrice()));
		return orderItem;
	}

	/**
	 * 获取规则名称。
	 * 
	 * @return 返回规则名称。
	 */
	public String getRuleName() {
		return getSnackRule().getName();
	}

	/**
	 * 获取规则颜色。
	 * 
	 * @return 返回规则颜色。
	 */
	public String getRuleColor() {
		return getSnackRule().getEnabled().getColor();
	}

	/**
	 * 获取规则摘要。
	 * 
	 * @return 返回影院结算规则摘要。
	 */
	public String getRuleSummary() {
		return getSnackRule().getSummary();
	}

	public SnackOrder getSnackOrder() {
		return snackOrder;
	}

	public void setSnackOrder(SnackOrder snackOrder) {
		this.snackOrder = snackOrder;
	}

	public Snack getSnack() {
		return snack;
	}

	public void setSnack(Snack snack) {
		this.snack = snack;
	}

	public BenefitCardTypeSnackRule getSnackRule() {
		return snackRule;
	}

	public void setSnackRule(BenefitCardTypeSnackRule snackRule) {
		this.snackRule = snackRule;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getStdPrice() {
		return stdPrice;
	}

	public void setStdPrice(Double stdPrice) {
		this.stdPrice = stdPrice;
	}

	public Double getCinemaPrice() {
		return cinemaPrice;
	}

	public void setCinemaPrice(Double cinemaPrice) {
		this.cinemaPrice = cinemaPrice;
	}

	public Double getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(Double channelPrice) {
		this.channelPrice = channelPrice;
	}

	public Double getConnectFee() {
		return connectFee;
	}

	public void setConnectFee(Double connectFee) {
		this.connectFee = connectFee;
	}

	public Double getChannelFee() {
		return channelFee;
	}

	public void setChannelFee(Double channelFee) {
		this.channelFee = channelFee;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}