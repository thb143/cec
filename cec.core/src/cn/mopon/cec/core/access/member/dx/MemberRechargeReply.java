package cn.mopon.cec.core.access.member.dx;

/**
 * 会员卡充值响应对象。
 */
public class MemberRechargeReply extends DXReply {
	private String rechargeSuccess;

	/**
	 * 构造方法。
	 */
	public MemberRechargeReply() {
		xstream.alias("root", MemberDetailReply.class);
		xstream.alias("root", DXReply.class, MemberDetailReply.class);
	}

	public String getRechargeSuccess() {
		return rechargeSuccess;
	}

	public void setRechargeSuccess(String rechargeSuccess) {
		this.rechargeSuccess = rechargeSuccess;
	}

}
