package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.MemberSettings;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员卡信息请求对象。
 */
@XStreamAlias("queryCard")
public class MemberDetailByChipQuery extends MTXQuery {

	/** 卡号 */
	@XStreamAlias("memoryId")
	private String cardCode;
	/** 签名 */
	@XStreamAlias("validateKey")
	private String sign;

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param settings
	 *            会员卡接入设置
	 */
	public MemberDetailByChipQuery(MemberCard memberCard, MemberSettings settings) {
		this.cinameCode = memberCard.getCinemaCode();
		this.cardCode = memberCard.getChipCode();
		this.pid = settings.getUsername();
		this.param = this.cardCode + settings.getPassword();
		this.sign = getVerifyInfo(this.param);
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
