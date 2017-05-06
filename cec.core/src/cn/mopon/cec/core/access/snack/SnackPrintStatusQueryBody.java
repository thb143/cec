package cn.mopon.cec.core.access.snack;

import cn.mopon.cec.core.entity.SnackOrder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;

/**
 * 查看卖品打印状态请求报文体。
 * 
 */
@XStreamAliasType("body")
public class SnackPrintStatusQueryBody {
	/** 平台订单号 */
	@XStreamAlias("OrderNo")
	private String orderNo;

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            卖品订单
	 * @throws Exception
	 *             异常信息
	 */
	public SnackPrintStatusQueryBody(SnackOrder order) throws Exception {
		this.orderNo = order.getCode();
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}