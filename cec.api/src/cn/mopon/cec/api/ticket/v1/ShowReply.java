package cn.mopon.cec.api.ticket.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.ShowVo;
import cn.mopon.cec.core.entity.ChannelShow;

/**
 * 查询场次响应对象。
 */
public class ShowReply extends ApiReply {
	/** 场次 */
	private ShowVo show;

	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道场次
	 */
	public ShowReply(ChannelShow channelShow) {
		this.show = new ShowVo(channelShow);
	}

	public ShowVo getShow() {
		return show;
	}

	public void setShow(ShowVo show) {
		this.show = show;
	}
}