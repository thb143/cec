package cn.mopon.cec.core.access.ticket.yxt;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.entity.Hall;
import cn.mopon.cec.core.enums.HallType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 影厅响应信息。
 */
public class HallReply extends YxtReply {
	private static XStream xstream;
	@XStreamAlias("halls")
	private List<Hall> halls = new ArrayList<Hall>();

	static {
		xstream = new GenericXStream();
		xstream.alias("data", HallReply.class);
		xstream.alias("data", YxtReply.class, HallReply.class);
		xstream.alias("hall", Hall.class);
		xstream.registerLocalConverter(Hall.class, "type", new IEnumConverter(
				HallType.class));
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
