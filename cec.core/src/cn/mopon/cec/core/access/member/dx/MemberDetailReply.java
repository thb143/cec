package cn.mopon.cec.core.access.member.dx;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡详细信息响应对象。
 */
public class MemberDetailReply extends DXReply {
	private MemberCard memberCard;

	/**
	 * 构造方法。
	 */
	public MemberDetailReply() {
		xstream.alias("root", MemberDetailReply.class);
		xstream.alias("root", DXReply.class, MemberDetailReply.class);
		xstream.alias("data", MemberCard.class);
	}

	public MemberCard getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(MemberCard memberCard) {
		this.memberCard = memberCard;
	}
}
