package cn.mopon.cec.core.access.ticket.mtx;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Hall;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 查询影厅基础信息响应对象。
 */

@XStreamAlias("GetHallResult")
public class HallReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("hall")
	private List<Hall> halls = new ArrayList<Hall>();

	static {
		xstream = new GenericXStream();
		xstream.alias("GetHallResult", HallReply.class);
		xstream.alias("GetHallResult", MtxReply.class, HallReply.class);

		xstream.alias("hall", Hall.class);
		xstream.alias("ResultCode", String.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

}
