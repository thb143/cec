package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询令牌对象。
 */
public class TokenQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 */
	public TokenQuery() {
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

}