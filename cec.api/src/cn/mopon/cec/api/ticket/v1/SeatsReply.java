package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.SeatVo;
import cn.mopon.cec.core.entity.Seat;

/**
 * 查询座位响应对象。
 */
public class SeatsReply extends ApiReply {
	/** 座位列表 */
	private List<SeatVo> seats = new ArrayList<SeatVo>();

	/**
	 * 构造方法。
	 * 
	 * @param seats
	 *            座位列表
	 */
	public SeatsReply(List<Seat> seats) {
		for (Seat seat : seats) {
			this.seats.add(new SeatVo(seat));
		}
	}

	public List<SeatVo> getSeats() {
		return seats;
	}

	public void setSeats(List<SeatVo> seatInfo) {
		this.seats = seatInfo;
	}
}