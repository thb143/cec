package cn.mopon.cec.core.access.ticket.mtx;

import java.util.List;

import cn.mopon.cec.core.access.ticket.mtx.vo.ShowVo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 根据影片编码,影院编码读取上映场次排期响应对象。
 */
public class GetFeatureInfoReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("shows")
	private List<ShowVo> shows;

	static {
		xstream = new GenericXStream();
		xstream.alias("data", GetFeatureInfoReply.class);
		xstream.alias("data", MtxReply.class, GetFeatureInfoReply.class);
		xstream.alias("Show", ShowVo.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public List<ShowVo> getShows() {
		return shows;
	}

	public void setShows(List<ShowVo> shows) {
		this.shows = shows;
	}

}
