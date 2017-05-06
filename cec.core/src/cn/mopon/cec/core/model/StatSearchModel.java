package cn.mopon.cec.core.model;

import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.TicketOrderKind;
import coo.base.util.DateUtils;
import coo.core.model.DateRangeSearchModel;

/**
 * 统计查询条件。
 */
public class StatSearchModel extends DateRangeSearchModel {
	/** 影院ID */
	private String cinemaId;
	/** 渠道ID */
	private String channelId;

	private ShowType showType;
	private TicketOrderKind kind;

	public String getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public TicketOrderKind getKind() {
		return kind;
	}

	public void setKind(TicketOrderKind kind) {
		this.kind = kind;
	}

	/**
	 * 获取字符串类型的开始时间
	 * 
	 * @return 开始时间 yyyy-MM-dd
	 */
	public String getStartDateStr() {
		if (getStartDate() == null) {
			return "";
		} else {
			return DateUtils.format(getStartDate(), DateUtils.DAY);
		}
	}

	/**
	 * 获取字符串类型的结束时间
	 * 
	 * @return 结束时间 yyyy-MM-dd
	 */
	public String getEndDateStr() {
		if (getEndDate() == null) {
			return "";
		} else {
			return DateUtils.format(getEndDate(), DateUtils.DAY);
		}
	}
}
