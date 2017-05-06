package cn.mopon.cec.core.assist.settle;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.mopon.cec.core.assist.enums.SettleBasisType;
import cn.mopon.cec.core.assist.enums.SettleRuleType;
import coo.base.exception.BusinessException;
import coo.base.util.BeanUtils;
import coo.base.util.StringUtils;
import coo.core.util.IEnumUtils;

/**
 * 区间定价。
 */
@SuppressWarnings("serial")
public class RoundSettleRule extends SettleRule {
	/** 区间定价条目 */
	private List<RoundSettleRuleItem> items = new ArrayList<RoundSettleRuleItem>();

	/**
	 * 构造方法。
	 */
	public RoundSettleRule() {
		setType(SettleRuleType.ROUND);
	}

	/**
	 * 包装区间定价中的结算规则。
	 * 
	 * @param request
	 *            请求对象
	 * @return 返回包装后的区间定价结算规则。
	 */
	public static RoundSettleRule wrapRoundSettleRule(HttpServletRequest request) {
		RoundSettleRule roundSettle = new RoundSettleRule();
		for (int i = 0;; i++) {
			String ruleType = request.getParameter("settleRule.items[" + i
					+ "].settleRule.type");
			if (StringUtils.isEmpty(ruleType)) {
				break;
			}
			RoundSettleRuleItem item = new RoundSettleRuleItem();
			SettleRule rule = SettleRule.createSettleRule(IEnumUtils
					.getIEnumByValue(SettleRuleType.class, ruleType));
			item.setSettleRule(rule);
			wrapRoundSettleRuleItem(request, item, i);
			item.getSettleRule().setBasisType(
					IEnumUtils.getIEnumByValue(SettleBasisType.class,
							request.getParameter("settleRule.basisType")));
			roundSettle.getItems().add(item);
		}
		return roundSettle;
	}

	/**
	 * 包装区间定价中的结算规则明细。
	 * 
	 * @param request
	 *            请求对象
	 * @param item
	 *            结算规则明细
	 * @param index
	 *            元素下标
	 */
	private static void wrapRoundSettleRuleItem(HttpServletRequest request,
			RoundSettleRuleItem item, int index) {
		@SuppressWarnings("unchecked")
		Enumeration<String> pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String key = pNames.nextElement();
			if (key.equals("settleRule.items[" + index + "].settleRule.type")) {
				continue;
			}
			if (key.startsWith("settleRule.items[" + index + "]")) {
				BeanUtils.setField(item, key.substring(key.indexOf("]") + 2),
						Double.valueOf(request.getParameter(key)));
			}
		}
	}

	@Override
	public Double cal(Double price) {
		for (RoundSettleRuleItem item : items) {
			if (item.contains(price)) {
				return item.getSettleRule().cal(price);
			}
		}
		throw new BusinessException("区间定价规则中未找到能匹配价格[" + price + "]的价格区间。");
	}

	public List<RoundSettleRuleItem> getItems() {
		return items;
	}

	public void setItems(List<RoundSettleRuleItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getBasisType().getText());
		for (int i = 0; i < items.size(); i++) {
			if ((i + 1) % 3 == 0) {
				builder.append("\n");
			}
			builder.append(items.get(i).toString());
		}
		return builder.toString() + super.toString();
	}
}