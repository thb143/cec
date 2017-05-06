package cn.mopon.cec.core.access.member.hln;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员退票响应对象
 * 
 */
@XStreamAlias("data")
public class MemberRefundmentReply extends HLNReply {
	/**
	 * 构造方法。
	 */
	public MemberRefundmentReply() {
		xstream.alias("data", MemberRefundmentReply.class);
		xstream.alias("data", HLNReply.class, MemberRefundmentReply.class);
	}
}
