package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import cn.mopon.cec.core.entity.SpecialPolicy;

/**
 * 特殊定价策略按年份分组列表模型。
 */
public class YearSpecialPolicyListModel {
	private List<YearSpecialPolicyModel> items = new ArrayList<YearSpecialPolicyModel>();

	/**
	 * 增加特殊定价策略。
	 * 
	 * @param policy
	 *            特殊定价策略
	 */
	public void addPolicy(SpecialPolicy policy) {
		DateTime dateTime = new DateTime(policy.getStartDate());
		YearSpecialPolicyModel model = getYearSpecialPolicyModel(dateTime
				.getYear());
		model.addPolicy(policy);
	}

	public List<YearSpecialPolicyModel> getItems() {
		return items;
	}

	public void setItems(List<YearSpecialPolicyModel> items) {
		this.items = items;
	}

	/**
	 * 获取特殊定价策略按年份分组模型。
	 * 
	 * @param year
	 *            年份
	 * @return 返回特殊定价策略按年份分组模型。
	 */
	private YearSpecialPolicyModel getYearSpecialPolicyModel(Integer year) {
		for (YearSpecialPolicyModel model : items) {
			if (model.getYear().equals(year)) {
				return model;
			}
		}
		YearSpecialPolicyModel model = new YearSpecialPolicyModel();
		model.setYear(year);
		items.add(model);
		Collections.sort(items);
		return model;
	}
}