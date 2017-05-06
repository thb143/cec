package cn.mopon.cec.core.access.member.hln;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 请求对象基类。
 */
public abstract class HLNQuery {
	@XStreamOmitField
	protected XStream xstream = new GenericXStream();
	@XStreamAsAttribute
	@XStreamAlias("Id")
	private String id;

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
