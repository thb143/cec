package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Hall;
import coo.core.model.SearchModel;

/**
 * 影院分组展示模型。
 */
public class CinemaModel extends SearchModel {
	/** 影院名称 */
	private String name;
	/** 特殊定价策略ID */
	private String specialPolicyId;
	/** 所选择影厅 */
	private List<Hall> halls = new ArrayList<>();
	
	/** 卡类定价规则ID */
	private String ruleId;
	/**
	 * 获取影院影厅文本摘要。
	 * 
	 * @return 返回影院影厅文本摘要。
	 */
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append("：");
		for (Hall hall : halls) {
			builder.append(hall.getFullName());
			builder.append(" ");
		}
		return builder.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialPolicyId() {
		return specialPolicyId;
	}

	public void setSpecialPolicyId(String specialPolicyId) {
		this.specialPolicyId = specialPolicyId;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
}
