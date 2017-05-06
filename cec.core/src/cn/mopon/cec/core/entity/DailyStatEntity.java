package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;

import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.model.NormalOrderStat;
import cn.mopon.cec.core.model.RevokeOrderStat;
import cn.mopon.cec.core.model.ShowTypeStat;
import coo.core.hibernate.search.DateBridge;
import coo.core.model.UuidEntity;

/**
 * 日统计基类。
 */
@MappedSuperclass
public class DailyStatEntity extends UuidEntity {
	/** 放映类型统计 */
	@Type(type = "JsonList")
	protected List<ShowTypeStat> showTypeStat = new ArrayList<ShowTypeStat>();
	/** 正常订单统计 */
	@Type(type = "Json")
	protected NormalOrderStat normalOrderStat = new NormalOrderStat();
	/** 退票订单统计 */
	@Type(type = "Json")
	protected RevokeOrderStat refundOrderStat = new RevokeOrderStat();
	/** 统计日期 */
	@Temporal(TemporalType.DATE)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	protected Date statDate;

	public List<ShowTypeStat> getShowTypeStat() {
		return showTypeStat;
	}

	public void setShowTypeStat(List<ShowTypeStat> showTypeStat) {
		this.showTypeStat = showTypeStat;
	}

	public NormalOrderStat getNormalOrderStat() {
		return normalOrderStat;
	}

	public void setNormalOrderStat(NormalOrderStat normalOrderStat) {
		this.normalOrderStat = normalOrderStat;
	}

	public RevokeOrderStat getRefundOrderStat() {
		return refundOrderStat;
	}

	public void setRefundOrderStat(RevokeOrderStat refundOrderStat) {
		this.refundOrderStat = refundOrderStat;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	/**
	 * 获取指定放映类型的的数据统计。
	 * 
	 * @param showType
	 *            放映类型
	 * @return 放映类型的统计。
	 */
	public ShowTypeStat getShowTypeStat(ShowType showType) {
		for (ShowTypeStat stat : showTypeStat) {
			if (stat.getShowType() == showType) {
				return stat;
			}
		}
		return new ShowTypeStat(showType);
	}

	/**
	 * 获取2d 放映类型的统计信息。
	 * 
	 * @return 2d 放映类型的统计信息。
	 */
	public ShowTypeStat getShowTypeSeat2D() {
		return getShowTypeStat(ShowType.NORMAL2D);
	}

	/**
	 * 获取3d 放映类型的统计信息。
	 * 
	 * @return 3d 放映类型的统计信息。
	 */
	public ShowTypeStat getShowTypeSeat3D() {
		return getShowTypeStat(ShowType.NORMAL3D);
	}

	/**
	 * 获取max2d 放映类型的统计信息。
	 * 
	 * @return max2d 放映类型的统计信息。
	 */
	public ShowTypeStat getShowTypeSeatMAX2D() {
		return getShowTypeStat(ShowType.MAX2D);
	}

	/**
	 * 获取max3d 放映类型的统计信息。
	 * 
	 * @return max3d 放映类型的统计信息。
	 */
	public ShowTypeStat getShowTypeSeatMAX3D() {
		return getShowTypeStat(ShowType.MAX3D);
	}

	/**
	 * 获取dmax 放映类型的统计信息。
	 * 
	 * @return dmax 放映类型的统计信息。
	 */
	public ShowTypeStat getShowTypeSeatDMAX() {
		return getShowTypeStat(ShowType.DMAX);
	}

}
