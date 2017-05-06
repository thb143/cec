package cn.mopon.cec.core.access.member.dx.vo;

import java.util.Date;

import coo.base.util.DateUtils;

/**
 * 放映计划VO。
 */
public class ShowVo {
	/** 排期编码 */
	private String id;
	/** 影片编码 */
	private String cineMovieNum;
	/** 影厅编码 */
	private String hallId;
	/** 放映时间 */
	private Date startTime;
	/** 标准价 */
	private Double marketPrice;
	/** 最低价 */
	private Double lowestPrice;
	/** 最后更新时间 */
	private String cineUpdateTime;

	/**
	 * 构造方法。
	 * 
	 * @param showCode
	 *            排期编码
	 */
	public ShowVo(String showCode) {
		id = showCode.substring(0, showCode.length() - 14);
		String updateTimeString = showCode.substring(showCode.length() - 14);
		if ("00000000000000".equals(updateTimeString)) {
			cineUpdateTime = "0000-00-00 00:00:00";
		} else {
			Date updateTime = DateUtils.parse(updateTimeString,
					DateUtils.SECOND_N);
			cineUpdateTime = DateUtils.format(updateTime, DateUtils.SECOND);
		}
	}

	/**
	 * 生成排期编码。
	 * 
	 * @return 返回生成后的排期编码
	 */
	public String genShowCode() {
		String showCode = "";
		if ("0000-00-00 00:00:00".equals(cineUpdateTime)) {
			showCode = id + "00000000000000";
		} else {
			Date updateTime = DateUtils.parse(cineUpdateTime, DateUtils.SECOND);
			showCode = id + DateUtils.format(updateTime, DateUtils.SECOND_N);
		}
		return showCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCineMovieNum() {
		return cineMovieNum;
	}

	public void setCineMovieNum(String cineMovieNum) {
		this.cineMovieNum = cineMovieNum;
	}

	public String getHallId() {
		return hallId;
	}

	public void setHallId(String hallId) {
		this.hallId = hallId;
	}

	public Double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public Double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(Double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getCineUpdateTime() {
		return cineUpdateTime;
	}

	public void setCineUpdateTime(String cineUpdateTime) {
		this.cineUpdateTime = cineUpdateTime;
	}

}
