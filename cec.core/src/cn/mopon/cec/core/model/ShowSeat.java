package cn.mopon.cec.core.model;

import java.io.Serializable;

import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.enums.SeatType;
import cn.mopon.cec.core.enums.SellStatus;

/**
 * 排期座位。
 */
public class ShowSeat implements Serializable, Comparable<ShowSeat> {
	private static final long serialVersionUID = -7241657488248746243L;
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
	private SeatType type;
	/** 状态 */
	private SellStatus status;
	/** 情侣座编码 */
	private String loveCode;

	/**
	 * 创建排期座位。
	 * 
	 * @param seat
	 *            座位
	 * @return 返回排期座位。
	 */
	public static ShowSeat createBySeat(Seat seat) {
		ShowSeat showSeat = new ShowSeat();
		showSeat.setCode(seat.getCode());
		showSeat.setGroupCode(seat.getGroupCode());
		showSeat.setRowNum(seat.getRowNum());
		showSeat.setColNum(seat.getColNum());
		showSeat.setXCoord(seat.getXCoord());
		showSeat.setYCoord(seat.getYCoord());
		showSeat.setType(seat.getType());
		showSeat.setLoveCode(seat.getLoveCode());
		showSeat.setStatus(SellStatus.ENABLED);
		return showSeat;
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

	public SellStatus getStatus() {
		return status;
	}

	public void setStatus(SellStatus status) {
		this.status = status;
	}

	public String getLoveCode() {
		return loveCode;
	}

	public void setLoveCode(String loveCode) {
		this.loveCode = loveCode;
	}

	@Override
	public int compareTo(ShowSeat o) {
		return this.getCode().compareTo(o.getCode());
	}
}
