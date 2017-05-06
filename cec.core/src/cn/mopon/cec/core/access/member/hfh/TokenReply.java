package cn.mopon.cec.core.access.member.hfh;

/**
 * 会员卡令牌响应对象。
 */
public class TokenReply extends HFHReply {
	private String token;

	/**
	 * 构造方法。
	 */
	public TokenReply() {
		xstream.alias("data", TokenReply.class);
		xstream.alias("data", HFHReply.class, TokenReply.class);
	}

	public String getToken() {
		return token;
	}

}