package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询座位请求对象。
 */
public class SeatsQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;
	/** 影厅编码 */
	@NotBlank(message = "影厅编码不能为空。")
	private String hallCode;

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}
}