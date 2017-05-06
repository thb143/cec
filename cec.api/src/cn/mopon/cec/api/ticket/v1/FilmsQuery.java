package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询影片请求对象。
 */
public class FilmsQuery extends ApiQuery {
	/** 起始日期 */
	private Date startDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}