package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.ChannelShowType;
import cn.mopon.cec.core.enums.SubmitType;
import coo.base.util.NumberUtils;
import coo.core.model.UuidEntity;

/**
 * 权益卡结算价格。
 */
@Entity
@Table(name = "CEC_BenefitCardSettle")
@Indexed(index = "BenefitCardSettle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BenefitCardSettle extends UuidEntity {
	/** 关联规则 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ruleId")
	@IndexedEmbedded(includePaths = { "id" })
	private BenefitCardTypeRule rule;
	/** 关联渠道排期 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelShowId")
	@IndexedEmbedded(includePaths = { "id", "code", "status" })
	private ChannelShow channelShow;
	/** 渠道结算价 */
	private Double channelPrice = 0D;
	/** 报票房价 */
	private Double submitPrice = 0D;
	/** 影院结算价 */
	private Double cinemaPrice = 0D;
	/** 手续费 */
	private Double circuitFee = 0D;
	/** 补贴费 */
	private Double subsidyFee = 0D;

	/**
	 * 获取优惠金额。
	 * 
	 * @return 返回优惠金额。
	 */
	public Double getDiscountPrice() {
		return NumberUtils
				.sub(getChannelShow().getChannelPrice(), channelPrice);
	}

	/**
	 * 创建权益卡结算价格。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param rule
	 *            结算规则
	 * @return 返回结算价格对象。
	 */
	public static BenefitCardSettle create(ChannelShow channelShow,
			BenefitCardTypeRule rule) {
		BenefitCardSettle settle = new BenefitCardSettle();
		settle.setChannelShow(channelShow);
		settle.setRule(rule);
		settle.calPrice(settle, channelShow, rule);
		return settle;
	}

	/**
	 * 计算价格。
	 * 
	 * @param settle
	 *            结算价格对象
	 * @param channelShow
	 *            渠道排期
	 * @param rule
	 *            结算规则
	 */
	private void calPrice(BenefitCardSettle settle, ChannelShow channelShow,
			BenefitCardTypeRule rule) {
		settle.setChannelPrice(getChannelPrice(
				rule.getSettleRule().cal(channelShow.getChannelPrice()),
				channelShow.getMinPrice(), channelShow));
		settle.setSubmitPrice(getSubmitPrice(
				rule.getSettleRule().cal(channelShow.getSubmitPrice()),
				channelShow.getMinPrice(), channelShow));
		settle.setCinemaPrice(getCinemaPrice(channelShow.getCinemaPrice(),
				channelShow.getMinPrice(), channelShow));
		if (channelShow.getSubmitType() == SubmitType.CINEMA_PRICE) {
			// 如果渠道结算价大于影院结算价，则计为手续费，否则计为补贴费。
			if (settle.getChannelPrice() > settle.getCinemaPrice()) {
				settle.setCircuitFee(NumberUtils.sub(settle.getChannelPrice(),
						settle.getCinemaPrice()));
			} else {
				settle.setSubsidyFee(NumberUtils.sub(settle.getCinemaPrice(),
						settle.getChannelPrice()));
			}
		}
	}

	/**
	 * 返回影院结算价，影院结算价小于最低价，按最低价结算，否则按结算价结算。
	 * 
	 * @param settlePrice
	 *            结算价
	 * @param minPrice
	 *            最低价
	 * @param channelShow
	 *            渠道排期
	 * @return 返回影院结算价。
	 */
	private Double getCinemaPrice(Double settlePrice, Double minPrice,
			ChannelShow channelShow) {
		if (settlePrice < minPrice) {
			if (channelShow.getType() == ChannelShowType.NORMAL) {
				return getSettlePrice(settlePrice, minPrice, channelShow
						.getCinemaRule().getSettleRule()
						.getCinemaPriceBelowMinPrice());
			} else {
				return getSettlePrice(settlePrice, minPrice, channelShow
						.getSpecialRule().getSettleRule()
						.getCinemaPriceBelowMinPrice());
			}
		} else {
			return settlePrice;
		}
	}

	/**
	 * 计算渠道结算价，渠道结算价小于最低价，按最低价结算，否则按结算价结算。
	 * 
	 * @param settlePrice
	 *            结算价
	 * @param minPrice
	 *            最低价
	 * @param channelShow
	 *            渠道排期
	 * @return 返回渠道结算价。
	 */
	private Double getChannelPrice(Double settlePrice, Double minPrice,
			ChannelShow channelShow) {
		if (settlePrice < minPrice) {
			if (channelShow.getType() == ChannelShowType.NORMAL) {
				return getSettlePrice(settlePrice, minPrice, channelShow
						.getChannelRule().getSettleRule()
						.getChannelPriceBelowMinPrice());
			} else {
				return getSettlePrice(settlePrice, minPrice, channelShow
						.getSpecialRule().getSettleRule()
						.getChannelPriceBelowMinPrice());
			}
		} else {
			return settlePrice;
		}
	}

	/**
	 * 返回结算价，结算价小于最低价，按最低价结算，否则按结算价结算。
	 * 
	 * @param settlePrice
	 *            结算价
	 * @param minPrice
	 *            最低价
	 * @param channelShow
	 *            渠道排期
	 * @return 返回结算价。
	 */
	private Double getSubmitPrice(Double settlePrice, Double minPrice,
			ChannelShow channelShow) {
		if (settlePrice < minPrice) {
			if (channelShow.getType() == ChannelShowType.NORMAL) {
				return getSettlePrice(settlePrice, minPrice, channelShow
						.getChannelRule().getSettleRule()
						.getSumbitPriceBelowMinPrice());
			} else {
				return getSettlePrice(settlePrice, minPrice, channelShow
						.getSpecialRule().getSettleRule()
						.getSumbitPriceBelowMinPrice());
			}
		} else {
			return settlePrice;
		}
	}

	/**
	 * 计算结算价，结算价小于最低价，按最低价结算，否则按结算价结算。
	 * 
	 * @param settlePrice
	 *            结算价
	 * @param minPrice
	 *            最低价
	 * @param belowMinPrice
	 *            是否可低于最低价
	 * @return 返回结算价。
	 */
	private Double getSettlePrice(Double settlePrice, Double minPrice,
			Boolean belowMinPrice) {
		if (belowMinPrice) {
			return settlePrice;
		} else {
			return minPrice;
		}
	}

	/**
	 * 判断权益卡结算信息是否相同。
	 * 
	 * @param other
	 *            其它权益卡结算
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean equalsTo(BenefitCardSettle other) {
		EqualsBuilder builder = new EqualsBuilder()
				.append(getRule().getId(), other.getRule().getId())
				.append(getCinemaPrice(), other.getCinemaPrice())
				.append(getChannelPrice(), other.getChannelPrice())
				.append(getSubmitPrice(), other.getSubmitPrice())
				.append(getCircuitFee(), other.getCircuitFee())
				.append(getSubsidyFee(), other.getSubsidyFee());
		return builder.isEquals();
	}

	public BenefitCardTypeRule getRule() {
		return rule;
	}

	public void setRule(BenefitCardTypeRule rule) {
		this.rule = rule;
	}

	public ChannelShow getChannelShow() {
		return channelShow;
	}

	public void setChannelShow(ChannelShow channelShow) {
		this.channelShow = channelShow;
	}

	public Double getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(Double channelPrice) {
		this.channelPrice = channelPrice;
	}

	public Double getSubmitPrice() {
		return submitPrice;
	}

	public void setSubmitPrice(Double submitPrice) {
		this.submitPrice = submitPrice;
	}

	public Double getCinemaPrice() {
		return cinemaPrice;
	}

	public void setCinemaPrice(Double cinemaPrice) {
		this.cinemaPrice = cinemaPrice;
	}

	public Double getCircuitFee() {
		return circuitFee;
	}

	public void setCircuitFee(Double circuitFee) {
		this.circuitFee = circuitFee;
	}

	public Double getSubsidyFee() {
		return subsidyFee;
	}

	public void setSubsidyFee(Double subsidyFee) {
		this.subsidyFee = subsidyFee;
	}
}