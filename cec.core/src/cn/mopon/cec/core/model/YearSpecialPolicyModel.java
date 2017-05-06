package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.SpecialPolicy;

/**
 * 特殊定价策略按年份分组模型。
 */
public class YearSpecialPolicyModel implements
		Comparable<YearSpecialPolicyModel> {
	private Integer year;
	private List<SpecialPolicy> policys = new ArrayList<SpecialPolicy>();

	/**
	 * 增加特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	public void addPolicy(SpecialPolicy policy) {
		policys.add(policy);
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<SpecialPolicy> getPolicys() {
		return policys;
	}

	public void setPolicys(List<SpecialPolicy> policys) {
		this.policys = policys;
	}

	@Override
	public int compareTo(YearSpecialPolicyModel o) {
		return year.compareTo(o.getYear()) > 0 ? -1 : 1;
	}
}