package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.SeatType;
import coo.core.model.UuidEntity;

/**
 * 座位。
 */
@Entity
@Table(name = "CEC_Seat")
@Cache(region = "Seat", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Seat extends UuidEntity {
	/** 关联影厅 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hallId")
	private Hall hall;
	/** 编码 */
	private String code;
	/** 分组编码 */
	private String groupCode;
	/** 行号 */
	private String rowNum;
	/** 列号 */
	private String colNum;
	/** 横坐标 */
	private Integer xCoord;
	/** 纵坐标 */
	private Integer yCoord;
	/** 类型 */
	@Type(type = "IEnum")
	private SeatType type = SeatType.NORMAL;
	/** 状态 */
	@Type(type = "IEnum")
	private EnabledStatus status = EnabledStatus.ENABLED;
	/** 情侣座编码 */
	private String loveCode;

	/**
	 * 判断座位关键信息是否相同。
	 * 
	 * @param other
	 *            其它座位
	 * @return 如果关键信息相同返回true，否则返回false。
	 */
	public Boolean equalsTo(Seat other) {
		return new EqualsBuilder().append(getCode(), other.getCode())
				.append(getGroupCode(), other.getGroupCode())
				.append(getRowNum(), other.getRowNum())
				.append(getColNum(), other.getColNum())
				.append(getXCoord(), other.getXCoord())
				.append(getYCoord(), other.getYCoord())
				.append(getType(), other.getType())
				.append(getStatus(), other.getStatus()).isEquals();
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getColNum() {
		return colNum;
	}

	public void setColNum(String colNum) {
		this.colNum = colNum;
	}

	public Integer getXCoord() {
		return xCoord;
	}

	public void setXCoord(Integer xCoord) {
		this.xCoord = xCoord;
	}

	public Integer getYCoord() {
		return yCoord;
	}

	public void setYCoord(Integer yCoord) {
		this.yCoord = yCoord;
	}

	public SeatType getType() {
		return type;
	}

	public void setType(SeatType type) {
		this.type = type;
	}

	public EnabledStatus getStatus() {
		return status;
	}

	public void setStatus(EnabledStatus status) {
		this.status = status;
	}

	public String getLoveCode() {
		return loveCode;
	}

	public void setLoveCode(String loveCode) {
		this.loveCode = loveCode;
	}
}
