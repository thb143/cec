package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 
 * 会员卡信息响应对象。
 */

public class MemberDetailByChipReply extends MTXReply {
	/** 会员卡 */
	private MemberCard card;

	/**
	 * 构造方法。
	 */
	public MemberDetailByChipReply() {
		xstream.alias("CardInfo", MemberDetailByChipReply.class, MTXReply.class);
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}

}
