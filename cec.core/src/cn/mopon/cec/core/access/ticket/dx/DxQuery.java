package cn.mopon.cec.core.access.ticket.dx;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import coo.base.constants.Chars;
import coo.base.util.DateUtils;

/**
 * 鼎新查询对象基类。
 */
public class DxQuery {
	/** 访问路径 */
	private String action;
	/** 参数列表 */
	protected List<NameValuePair> params = new ArrayList<NameValuePair>();

	/**
	 * 获取外部排期编号。
	 * 
	 * @param showCode
	 *            内部排期编号
	 * @return 返回外部排期编号。
	 */
	public String getPlayId(String showCode) {
		return showCode.substring(0, showCode.length() - 14);
	}

	/**
	 * 获取排期更新时间。
	 * 
	 * @param showCode
	 *            排期编号
	 * @return 返回排期更新时间。
	 */
	public String getUpdateTime(String showCode) {
		String updateTimeString = showCode.substring(showCode.length() - 14);
		return updateTimeString.matches("[0]{14}") ? "0000-00-00 00:00:00"
				: DateUtils.format(
						DateUtils.parse(updateTimeString, DateUtils.SECOND_N),
						DateUtils.SECOND);
	}

	/**
	 * 获取订单座位。
	 * 
	 * @param order
	 *            选座票订单
	 * @return 返回订单座位。
	 */
	public String getOrderSeat(TicketOrder order) {
		StringBuilder sb = new StringBuilder();
		for (TicketOrderItem item : order.getOrderItems()) {
			if (sb.length() > 0) {
				sb.append(Chars.COMMA);
			}
			sb.append(item.getSeatCode());
		}
		return sb.toString();
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}
}
