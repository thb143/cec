package cn.mopon.cec.core.model;

import cn.mopon.cec.core.assist.fee.FeeRule;
import cn.mopon.cec.core.assist.fee.NoneFeeRule;

/**
 * 选座票定价规则渠道模型。
 */
public class TicketRuleChannelModel {
	/** 是否选中 */
	private boolean checked = false;
	/** 规则渠道名称 */
	private String name;
	/** 规则编号 */
	private String id;
	/** 手续费规则 */
	private FeeRule freeRule = new NoneFeeRule();

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FeeRule getFreeRule() {
		return freeRule;
	}

	public void setFreeRule(FeeRule freeRule) {
		this.freeRule = freeRule;
	}
}
