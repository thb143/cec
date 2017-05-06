package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 卡类可使用放映类型VO。
 */
@XStreamAlias("discountShowType")
public class ShowTypeBenefitCardVo {
	@XStreamConverter(value = IEnumConverter.class, types = ShowType.class)
	private ShowType showType;
	private Double discountPrice;

	/**
	 * 构造方法。
	 * 
	 * @param discountPrice
	 *            优惠价
	 * @param showType
	 *            放映类型
	 */
	public ShowTypeBenefitCardVo(Double discountPrice, ShowType showType) {
		this.showType = showType;
		this.discountPrice = discountPrice;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}
}