package cn.mopon.cec.core.access.member.hfh;

/**
 * 会员卡充值响应对象。
 */
public class MemberRechargeReply extends HFHReply {
	private Double balance;

	/**
	 * 构造方法。
	 */
	public MemberRechargeReply() {
		xstream.alias("data", MemberRechargeReply.class);
		xstream.alias("data", HFHReply.class, MemberRechargeReply.class);
	}

	public Double getBalance() {
		return balance;
	}

}
