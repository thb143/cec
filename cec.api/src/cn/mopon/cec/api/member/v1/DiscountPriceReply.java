package cn.mopon.cec.api.member.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.member.v1.vo.SeatInfoVo;
import cn.mopon.cec.core.access.member.vo.SeatInfo;

/**
 * 会员卡折扣响应对象。
 */
public class DiscountPriceReply extends ApiReply {
	/** 座位折扣价信息 */

	private List<SeatInfoVo> seatInfos = new ArrayList<SeatInfoVo>();

	/**
	 * 构造方法。
	 * 
	 * @param seatInfoList
	 *            座位信息列表
	 */
	public DiscountPriceReply(List<SeatInfo> seatInfoList) {
		for (SeatInfo seatInfo : seatInfoList) {
			seatInfos.add(new SeatInfoVo(seatInfo));
		}
	}

	public List<SeatInfoVo> getSeatInfos() {
		return seatInfos;
	}

	public void setSeatInfos(List<SeatInfoVo> seatInfos) {
		this.seatInfos = seatInfos;
	}

}
