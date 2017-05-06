package cn.mopon.cec.api.ticket.v1.vo;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 卡类可使用影厅VO。
 */
@XStreamAlias("hall")
public class HallBenefitCardVo {
	private String hallCode;
	private String hallName;
	private List<ShowTypeBenefitCardVo> discountShowTypes = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param hallCode
	 *            影厅编码
	 * @param hallName
	 *            影厅名称
	 */
	public HallBenefitCardVo(String hallCode, String hallName) {
		this.hallCode = hallCode;
		this.hallName = hallName;
	}

	/**
	 * 添加影厅列表。
	 * 
	 * @param hallVo
	 *            影厅VO
	 */
	public void addShowType(ShowType showType, Double discountPrice) {
		ShowTypeBenefitCardVo showTypeBenefitCardVo = new ShowTypeBenefitCardVo(
				discountPrice, showType);
		List<ShowType> showTypes = new ArrayList<>();
		for (ShowTypeBenefitCardVo vo : getDiscountShowTypes()) {
			showTypes.add(vo.getShowType());
		}
		if (!showTypes.contains(showType)) {
			discountShowTypes.add(showTypeBenefitCardVo);
		}
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public List<ShowTypeBenefitCardVo> getDiscountShowTypes() {
		return discountShowTypes;
	}

	public void setDiscountShowTypes(
			List<ShowTypeBenefitCardVo> discountShowTypes) {
		this.discountShowTypes = discountShowTypes;
	}
}