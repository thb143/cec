package cn.mopon.cec.core.access.ticket.ng.vo;

import cn.mopon.cec.core.enums.SeatType;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.util.BeanUtils;
import coo.core.xstream.IEnumConverter;

/**
 * 场次座位。
 */
@XStreamAlias("seat")
public class NgShowSeat {
	/** 编码 */
	private String code = "";
	/** 分组编码 */
	private String groupCode = "";
	/** 行号 */
	private String rowNum = "";
	/** 列号 */
	private String colNum = "";
	/** 横坐标 */
	private Integer xCoord = 0;
	/** 纵坐标 */
	private Integer yCoord = 0;
	/** 类型 */
	@XStreamConverter(value = IEnumConverter.class, types = SeatType.class)
	private SeatType type;
	/** 状态 */
	@XStreamConverter(value = IEnumConverter.class, types = SellStatus.class)
	private SellStatus status;
	/** 情侣座编码 */
	private String loveCode = "";

	/**
	 * 构造方法。
	 * 
	 * @param showSeat
	 *            场次座位
	 */
	public NgShowSeat(ShowSeat showSeat) {
		BeanUtils.copyFields(showSeat, this);
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
}
