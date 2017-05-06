package cn.mopon.cec.core.model;

import org.joda.time.LocalDate;

import cn.mopon.cec.core.enums.ShowType;

/**
 * 影片最低价模型项。
 */
@SuppressWarnings("serial")
public class FilmMinPriceItem extends PriceRuleModel {
	/** 开始日期 */
	private LocalDate startDate;
	/** 结束日期 */
	private LocalDate endDate;

	/**
	 * 根据放映类型获取最低价。
	 * 
	 * @param showType
	 *            放映类型
	 * @return 返回对应的最低价。
	 */
	public Double getMinPriceByShowType(ShowType showType) {
		double minPrice = 0;
		switch (showType) {
		case NORMAL2D:
			minPrice = getNormal2d();
			break;
		case NORMAL3D:
			minPrice = getNormal3d();
			break;
		case MAX2D:
			minPrice = getMax2d();
			break;
		case MAX3D:
			minPrice = getMax3d();
			break;
		case DMAX:
			minPrice = getDmax();
			break;
		default:
			break;
		}
		return minPrice;
	}

	/**
	 * 获取摘要。
	 * 
	 * @return 返回摘要信息。
	 */
	public String getSummary() {
		return getStartDate() + "~" + getEndDate() + "  2D:" + getNormal2d()
				+ ";3D:" + getNormal3d() + ";MAX2D:" + getMax2d() + ";MAX3D:"
				+ getMax3d() + "\n";
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
