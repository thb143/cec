package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.ShowSeatVo;
import cn.mopon.cec.core.model.ShowSeat;

/**
 * 查询场次座位响应对象。
 */
public class ShowSeatsReply extends ApiReply {
	/** 场次座位列表 */
	private List<ShowSeatVo> seats = new ArrayList<ShowSeatVo>();

	/**
	 * 构造方法。
	 * 
	 * @param showSeats
	 *            座位列表
	 */
	public ShowSeatsReply(List<ShowSeat> showSeats) {
		for (ShowSeat seat : showSeats) {
			seats.add(new ShowSeatVo(seat));
		}
	}


	public List<ShowSeatVo> getSeats() {
		return seats;
	}

	public void setSeats(List<ShowSeatVo> seats) {
		this.seats = seats;
	}
}