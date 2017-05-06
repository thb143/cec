package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.HallVo;
import cn.mopon.cec.core.entity.Hall;

/**
 * 查询影厅列表响应对象。
 */
public class HallsReply extends ApiReply {
	/** 影厅列表 */
	private List<HallVo> halls = new ArrayList<HallVo>();

	/**
	 * 构造方法。
	 * 
	 * @param halls
	 *            影厅列表
	 */
	public HallsReply(List<Hall> halls) {
		for (Hall hall : halls) {
			this.halls.add(new HallVo(hall));
		}
	}

	public List<HallVo> getHalls() {
		return halls;
	}

	public void setHalls(List<HallVo> halls) {
		this.halls = halls;
	}
}
