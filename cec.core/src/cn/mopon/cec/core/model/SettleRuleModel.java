package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.SettleRuleEntity;
import cn.mopon.cec.core.enums.ShowType;

/**
 * 影院结算规则模型。
 */
public class SettleRuleModel implements Comparable<SettleRuleModel> {
	/** 放映类型 */
	private ShowType showType;
	/** 结算规则 */
	private List<SettleRuleEntity> rules = new ArrayList<>();

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public List<SettleRuleEntity> getRules() {
		return rules;
	}

	public void setRules(List<SettleRuleEntity> rules) {
		this.rules = rules;
	}

	@Override
	public int compareTo(SettleRuleModel o) {
		return Integer.valueOf(showType.getValue())
				- Integer.valueOf(o.getShowType().getValue());
	}
}
