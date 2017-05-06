package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡信息响应对象。
 */

public class MemberDetailReply extends MTXReply {
	/** 会员卡 */
	private MemberCard card;

	/**
	 * 构造方法。
	 */
	public MemberDetailReply() {
		xstream.alias("CardInfo", MemberDetailReply.class, MTXReply.class);
		xstream.ignoreUnknownElements();
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}

}
