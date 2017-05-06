package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡充值响应对象（芯片号）。
 */

public class MemberRechargeByChipReply extends MTXReply {
	/** 会员卡 */
	private MemberCard card;

	/**
	 * 构造方法。
	 */
	public MemberRechargeByChipReply() {
		xstream.alias("CardRecharge", MemberRechargeByChipReply.class,
				MTXReply.class);
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}
}
