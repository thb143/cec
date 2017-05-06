package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.ShowVo;
import cn.mopon.cec.core.entity.ChannelShow;

/**
 * 查询场次列表响应对象。
 */
public class ShowsReply extends ApiReply {
	/** 场次列表 */
	private List<ShowVo> shows = new ArrayList<ShowVo>();

	/**
	 * 构造方法。
	 * 
	 * @param channelShows
	 *            渠道场次列表
	 */
	public ShowsReply(List<ChannelShow> channelShows) {
		for (ChannelShow channelShow : channelShows) {
			shows.add(new ShowVo(channelShow));
		}
	}

	public List<ShowVo> getShows() {
		return shows;
	}

	public void setShows(List<ShowVo> shows) {
		this.shows = shows;
	}
}