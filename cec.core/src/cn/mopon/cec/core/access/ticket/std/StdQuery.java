package cn.mopon.cec.core.access.ticket.std;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 请求对象基类。
 */
public abstract class StdQuery {
	@XStreamAsAttribute
	@XStreamAlias("Id")
	private String id;

	/**
	 * 获取XStream对象。
	 * 
	 * @return 返回XStream对象。
	 */
	abstract XStream getXstream();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
