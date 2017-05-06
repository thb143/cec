package cn.mopon.cec.core.model;

import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.ShowType;
import coo.base.util.NumberUtils;

/**
 * 放映类型。
 */
public class ShowTypeStat {
	/** 放映类型 */
	@Type(type = "IEnum")
	private ShowType showType = ShowType.NORMAL2D;
	/** 销售数量 */
	private Integer saleCount = 0;
	/** 退票数量 */
	private Integer refundCount = 0;
	/** 渠道结算价 */
	private Double channelAmount = 0d;
	/** 影院结算价 **/
	private Double cinemaAmount = 0d;
	/** 退款金额 */
	private Double refundAmount = 0d;
	/** 退款结算金额 **/
	private Double refundSettlePrice = 0d;
	/** 销售订单数量 */
	private Integer saleOrderCount = 0;
	/** 退票订单数量 */
	private Integer revokeOrderCount = 0;

	/**
	 * 构造方法
	 */
	public ShowTypeStat() {
	}

	/**
	 * 构造方法
	 * 
	 * @param showType
	 *            放映类型
	 */
	public ShowTypeStat(ShowType showType) {
		this.showType = showType;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public Integer getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

	public Double getChannelAmount() {
		return channelAmount;
	}

	public void setChannelAmount(Double salePrice) {
		this.channelAmount = salePrice;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getSaleOrderCount() {
		return saleOrderCount;
	}

	public void setSaleOrderCount(Integer saleOrderCount) {
		this.saleOrderCount = saleOrderCount;
	}

	public Integer getRevokeOrderCount() {
		return revokeOrderCount;
	}

	public void setRevokeOrderCount(Integer refundOrderCount) {
		this.revokeOrderCount = refundOrderCount;
	}

	public Double getCinemaAmount() {
		return cinemaAmount;
	}

	public void setCinemaAmount(Double settlePrice) {
		this.cinemaAmount = settlePrice;
	}

	public Double getRefundSettlePrice() {
		return refundSettlePrice;
	}

	public void setRefundSettlePrice(Double refundSettlePrice) {
		this.refundSettlePrice = refundSettlePrice;
	}

	/**
	 * 累加另一个对象的值
	 * 
	 * @param stat
	 *            另一个统计对象
	 */
	public void addShowTypeStat(ShowTypeStat stat) {
		saleCount = saleCount + stat.getSaleCount();
		refundCount = refundCount + stat.getRefundCount();
		channelAmount = NumberUtils.add(channelAmount, stat.getChannelAmount());
		refundAmount = NumberUtils.add(refundAmount, stat.getRefundAmount());
		refundSettlePrice = NumberUtils.add(refundSettlePrice,
				stat.getRefundSettlePrice());
		cinemaAmount = NumberUtils.add(cinemaAmount, stat.getCinemaAmount());
		saleOrderCount = saleOrderCount + stat.getSaleOrderCount();
		revokeOrderCount = revokeOrderCount + stat.getRevokeOrderCount();
	}
}
