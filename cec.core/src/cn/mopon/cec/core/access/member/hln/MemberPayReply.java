package cn.mopon.cec.core.access.member.hln;


/**
 * 会员卡消费扣款响应对象。
 */
public class MemberPayReply extends HLNReply {
	/**
	 * 构造方法。
	 */
	public MemberPayReply() {
		xstream.alias("data", MemberPayReply.class);
		xstream.alias("data", HLNReply.class, MemberPayReply.class);
	}

}
