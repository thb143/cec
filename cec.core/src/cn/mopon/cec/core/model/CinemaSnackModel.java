package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Snack;
import coo.core.model.SearchModel;

/**
 * 影院卖品分组展示模型。
 */
public class CinemaSnackModel extends SearchModel {
	/** 影院名称 */
	private String name;
	/** 所选择卖品 */
	private List<Snack> snacks = new ArrayList<>();
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
		for (Snack snack : snacks) {
			builder.append(snack.getType().getName());
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

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public List<Snack> getSnacks() {
		return snacks;
	}

	public void setSnacks(List<Snack> snacks) {
		this.snacks = snacks;
	}

}
