package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询影厅列表请求对象。
 */
public class HallsQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}
}