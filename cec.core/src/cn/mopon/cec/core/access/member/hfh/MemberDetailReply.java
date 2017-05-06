package cn.mopon.cec.core.access.member.hfh;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡详细信息响应对象。
 */
public class MemberDetailReply extends HFHReply {
	private MemberCard memberCard;

	/**
	 * 构造方法。
	 */
	public MemberDetailReply() {
		xstream.alias("data", MemberDetailReply.class);
		xstream.alias("data", HFHReply.class, MemberDetailReply.class);
	}

	public MemberCard getMemberCard() {
		return memberCard;
	}
}
