package cn.mopon.cec.core.access.ticket.mtx;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.access.ticket.mtx.vo.ShowSeatVo;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;

import com.thoughtworks.xstream.XStream;

import coo.base.util.CollectionUtils;

/**
 * 查询放映计划座位售出状态响应对象。
 */
public class SessionSeatReply extends MtxReply {
	private List<ShowSeatVo> planSiteStates;

	/**
	 * 获取排期座位。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回不可售状态的排期座位列表。
	 */
	public List<ShowSeat> getShowSeats(ChannelShow channelShow) {
		List<ShowSeat> showSeats = new ArrayList<ShowSeat>();
		List<Seat> seats = channelShow.getShow().getHall().getSeats();
		if (CollectionUtils.isNotEmpty(planSiteStates)) {
			for (Seat seat : seats) {
				for (ShowSeatVo seatVo : planSiteStates) {
					if (seat.getCode().equals(seatVo.getSeatNo())
							&& !"0".equals(seatVo.getSeatState())) {
						ShowSeat showSeat = ShowSeat.createBySeat(seat);
						showSeat.setStatus(SellStatus.DISABLED);
						showSeats.add(showSeat);
					}
				}
			}
		}
		return showSeats;
	}

	@Override
	XStream getXstream() {
		return null;
	}

	public List<ShowSeatVo> getPlanSiteStates() {
		return planSiteStates;
	}

	public void setPlanSiteStates(List<ShowSeatVo> planSiteStates) {
		this.planSiteStates = planSiteStates;
	}

}