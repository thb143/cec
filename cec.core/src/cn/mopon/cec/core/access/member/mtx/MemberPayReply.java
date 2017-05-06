package cn.mopon.cec.core.access.member.mtx;

/**
 * 会员卡消费扣款响应对象。
 */
public class MemberPayReply extends MTXReply {
	/**
	 * 构造方法。
	 */
	public MemberPayReply() {
		xstream.alias("CardPay", MemberPayReply.class, MTXReply.class);
	}
}
