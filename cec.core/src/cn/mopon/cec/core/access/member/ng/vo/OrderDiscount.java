package cn.mopon.cec.core.access.member.ng.vo;

import java.util.List;

/**
 * 会员价折扣vo。
 */
public class OrderDiscount {
	/** 订单编码 */
	private String orderNo;
	/** 订单总价格 */
	private Double totalSales;
	/** 座位信息 */
	private List<NgSeatInfo> data;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(Double totalSales) {
		this.totalSales = totalSales;
	}

	public List<NgSeatInfo> getData() {
		return data;
	}

	public void setData(List<NgSeatInfo> data) {
		this.data = data;
	}

}
