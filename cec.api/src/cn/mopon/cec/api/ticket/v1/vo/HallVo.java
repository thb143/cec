package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.Hall;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.BeanUtils;

/**
 * 影厅。
 */
@XStreamAlias("hall")
public class HallVo {
	/** 编码 */
	private String code = "";
	/** 名称 */
	private String name = "";
	/** 座位数量 */
	private Integer seatCount = 0;

	/**
	 * 构造方法。
	 * 
	 * @param hall
	 *            影厅
	 */
	public HallVo(Hall hall) {
		BeanUtils.copyFields(hall, this);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeatCount() {
		return seatCount;
	}

	public void setSeatCount(Integer seatCount) {
		this.seatCount = seatCount;
	}
}