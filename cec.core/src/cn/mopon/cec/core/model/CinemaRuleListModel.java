package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.CinemaRule;
import cn.mopon.cec.core.enums.ShowType;

/**
 * 影院结算规则列表模型。
 */
public class CinemaRuleListModel {
	private List<SettleRuleModel> items = new ArrayList<>();

	/**
	 * 构造方法。
	 * 
	 */
	public CinemaRuleListModel() {
	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院。
	 */
	public CinemaRuleListModel(Cinema cinema) {
		if (cinema.getTicketSettings() != null) {
			for (ShowType showtype : cinema.getTicketSettings().getShowTypes()) {
				SettleRuleModel cinemaRuleModel = new SettleRuleModel();
				cinemaRuleModel.setShowType(showtype);
				items.add(cinemaRuleModel);
			}
		}
	}

	/**
	 * 添加影院结算规则模型。
	 * 
	 * @param cinemaRule
	 *            影院结算规则
	 */
	public void add(CinemaRule cinemaRule) {
		SettleRuleModel cinemaRuleModel = getCinemaRuleModel(cinemaRule);
		cinemaRuleModel.getRules().add(cinemaRule);
	}

	/**
	 * 获取影院结算规则模型。
	 * 
	 * @param cinemaRule
	 *            结算规则
	 * @return 影院结算规则模型。
	 */
	public SettleRuleModel getCinemaRuleModel(CinemaRule cinemaRule) {
		for (SettleRuleModel rule : items) {
			if (rule.getShowType() == cinemaRule.getShowType()) {
				return rule;
			}
		}
		SettleRuleModel settleRuleModel = new SettleRuleModel();
		settleRuleModel.setShowType(cinemaRule.getShowType());
		items.add(settleRuleModel);
		return settleRuleModel;
	}

	public List<SettleRuleModel> getItems() {
		return items;
	}

	public void setItems(List<SettleRuleModel> items) {
		this.items = items;
	}
}
