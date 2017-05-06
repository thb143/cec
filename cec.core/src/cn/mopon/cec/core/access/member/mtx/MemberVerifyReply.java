package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡验证响应对象。
 */
public class MemberVerifyReply extends MTXReply {
	/** 会员卡 */
	private MemberCard card;

	/**
	 * 构造方法。
	 */
	public MemberVerifyReply() {
		xstream.alias("LoginCardReturn", MemberVerifyReply.class, MTXReply.class);
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}

}
