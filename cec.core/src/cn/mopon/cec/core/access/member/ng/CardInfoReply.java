package cn.mopon.cec.core.access.member.ng;

import cn.mopon.cec.core.access.member.vo.MemberCard;

/**
 * 会员卡信息响应对象。
 */
public class CardInfoReply extends NgReply {
	/** 会员卡 */
	private MemberCard data;

	public MemberCard getData() {
		/** 当NG返回状态是4时，为正常，对应接出接口的状态为0(可用)，否则返回1(不可用) */
		if (("4").equals(data.getStatus())) {
			data.setStatus("0");
		} else {
			data.setStatus("1");
		}
		data.setAccLevelName(data.getMemberPolicy());
		return data;
	}

	public void setData(MemberCard data) {
		this.data = data;
	}

}
