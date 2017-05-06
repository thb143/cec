package cn.mopon.cec.core.model;

import java.io.Serializable;

/**
 * 价格规则(用于参数设置中的影片最低价以及影片设置中的最低价)。
 */
@SuppressWarnings("serial")
public class PriceRuleModel implements Serializable {
	/** 普通2D */
	private Double normal2d = 0d;
	/** 普通3D */
	private Double normal3d = 0d;
	/** MAX2D */
	private Double max2d = 0d;
	/** MAX3D */
	private Double max3d = 0d;
	/** DMAX */
	private Double dmax = 0d;

	public Double getNormal2d() {
		return normal2d;
	}

	public void setNormal2d(Double normal2d) {
		this.normal2d = normal2d;
	}

	public Double getNormal3d() {
		return normal3d;
	}

	public void setNormal3d(Double normal3d) {
		this.normal3d = normal3d;
	}

	public Double getMax2d() {
		return max2d;
	}

	public void setMax2d(Double max2d) {
		this.max2d = max2d;
	}

	public Double getMax3d() {
		return max3d;
	}

	public void setMax3d(Double max3d) {
		this.max3d = max3d;
	}

	public Double getDmax() {
		return dmax;
	}

	public void setDmax(Double dmax) {
		this.dmax = dmax;
	}

	@Override
	public String toString() {
		return "2D:" + normal2d + ",3D:" + normal3d + ",MAX2D:" + max2d
				+ ",MAX3D:" + max3d + ",DMAX:" + dmax;
	}
}
