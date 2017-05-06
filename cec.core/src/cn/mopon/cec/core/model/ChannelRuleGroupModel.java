package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelRule;
import cn.mopon.cec.core.entity.ChannelRuleGroup;
import cn.mopon.cec.core.enums.RuleStatus;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.base.util.CollectionUtils;

/**
 * 规则分组模型。
 */
public class ChannelRuleGroupModel {
	/** 规则分组 */
	private ChannelRuleGroup group;
	/** 影院结算规则列表 */
	private List<SettleRuleModel> items = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 * @param group
	 *            规则分组
	 * @param ruleStatus
	 *            规则状态
	 */
	public ChannelRuleGroupModel(ChannelRuleGroup group, RuleStatus ruleStatus) {
		this.group = group;
		for (ShowType showType : group.getCinema().getTicketSettings()
				.getShowTypes()) {
			SettleRuleModel item = new SettleRuleModel();
			item.setShowType(showType);
			for (ChannelRule rule : group.getRules()) {
				if (ruleStatus != null) {
					if (rule.getShowType() == showType
							&& ruleStatus == rule.getStatus()
							&& rule.getValid() == ValidStatus.UNVALID) {
						item.getRules().add(rule);
					}
				} else {
					if (rule.getShowType() == showType) {
						item.getRules().add(rule);
					}
				}
			}
			items.add(item);
		}
		if (ruleStatus != null) {
			removeRuleModel();
		}
	}

	/**
	 * 判断分组下是否包含规则。
	 * 
	 * @return 如果有规则 返回true；否则false
	 */
	public boolean hasRules() {
		for (SettleRuleModel model : items) {
			if (CollectionUtils.isNotEmpty(model.getRules())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 移除不包含规则的放映类型。
	 */
	private void removeRuleModel() {
		Iterator<SettleRuleModel> iterator = items.iterator();
		while (iterator.hasNext()) {
			SettleRuleModel model = iterator.next();
			if (CollectionUtils.isEmpty(model.getRules())) {
				iterator.remove();
			}
		}
	}

	public List<SettleRuleModel> getItems() {
		return items;
	}

	public void setItems(List<SettleRuleModel> items) {
		this.items = items;
	}

	public ChannelRuleGroup getGroup() {
		return group;
	}

	public void setGroup(ChannelRuleGroup group) {
		this.group = group;
	}
}
