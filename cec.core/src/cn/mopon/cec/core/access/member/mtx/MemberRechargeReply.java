package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡充值响应对象。
 */

public class MemberRechargeReply extends MTXReply {
	/** 会员卡 */
	private MemberCard card;

	/**
	 * 构造方法。
	 */
	public MemberRechargeReply() {
		xstream.alias("CardRecharge", MemberRechargeReply.class, MTXReply.class);
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}
}
