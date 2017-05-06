package cn.mopon.cec.api.ticket.v1;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;
import cn.mopon.cec.core.enums.ShelveStatus;
import coo.base.util.DateUtils;

/**
 * 查询场次列表请求对象。
 */
public class ShowsQuery extends ApiQuery {
	/** 影院编码 */
	@NotBlank(message = "影院编码不能为空。")
	private String cinemaCode;
	/** 起始日期 */
	private Date startDate;
	/** 状态 */
	private ShelveStatus status;

	public String getStartDateStr() {
		return DateUtils.format(startDate, DateUtils.DAY_N);
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public ShelveStatus getStatus() {
		return status;
	}

	public void setStatus(ShelveStatus status) {
		this.status = status;
	}
}