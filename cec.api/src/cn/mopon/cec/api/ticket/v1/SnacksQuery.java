package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;
import cn.mopon.cec.core.enums.SnackStatus;

/***
 * 查询卖品请求对象。
 */
public class SnacksQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;
	/** 状态 */
	private SnackStatus status;

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public SnackStatus getStatus() {
		return status;
	}

	public void setStatus(SnackStatus status) {
		this.status = status;
	}
}