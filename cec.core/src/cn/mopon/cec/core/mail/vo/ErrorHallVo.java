package cn.mopon.cec.core.mail.vo;

import cn.mopon.cec.core.entity.Hall;

/**
 * 异常影厅VO。
 */
public class ErrorHallVo {
	private String hallCode;
	private String hallName;
	private int seatCount;

	/**
	 * 构造方法。
	 * 
	 * @param hall
	 *            影厅
	 */
	public ErrorHallVo(Hall hall) {
		this.hallCode = hall.getCode();
		this.hallName = hall.getName();
		this.seatCount = 0;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public int getSeatCount() {
		return seatCount;
	}

	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
}