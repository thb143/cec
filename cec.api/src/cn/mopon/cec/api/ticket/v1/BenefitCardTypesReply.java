package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.BenefitCardTypesVo;
import cn.mopon.cec.core.entity.BenefitCardType;

/**
 * 查询卡类响应对象。
 */
public class BenefitCardTypesReply extends ApiReply {

	/** 卡类列表 */
	private List<BenefitCardTypesVo> benefitCardTypes = new ArrayList<BenefitCardTypesVo>();

	/**
	 * 构造方法。
	 * 
	 * @param benefitCardTypes
	 *            卡类列表
	 */
	public BenefitCardTypesReply(List<BenefitCardType> benefitCardTypes) {
		for (BenefitCardType benefitCardType : benefitCardTypes) {
			this.benefitCardTypes.add(new BenefitCardTypesVo(benefitCardType));
		}
	}

	public List<BenefitCardTypesVo> getBenefitCardTypes() {
		return benefitCardTypes;
	}

	public void setBenefitCardTypes(List<BenefitCardTypesVo> benefitCardTypes) {
		this.benefitCardTypes = benefitCardTypes;
	}
}