package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.access.ticket.mtx.vo.Token;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.core.xstream.GenericXStream;

/**
 * 查询令牌响应对象。
 */
@XStreamAlias("TokenResult")
public class TokenReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("Token")
	private Token token;

	static {
		xstream = new GenericXStream();
		xstream.alias("TokenResult", TokenReply.class);
		xstream.alias("TokenResult", MtxReply.class, MtxReply.class);
		xstream.alias("Token", Token.class);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
}
