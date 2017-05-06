package cn.mopon.cec.core.model;

import coo.core.model.SearchModel;

/**
 * 排期订单搜索条件。
 */
public class ShowOrderSearchModel extends SearchModel {
	/** 排期编码 */
	private String showCode;

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}
}