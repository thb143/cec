package cn.mopon.cec.core.access.member.mtx;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import cn.mopon.cec.core.entity.MemberSettings;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 会员卡信息请求对象。
 */
@XStreamAlias("loginCard")
public class MemberDetailQuery extends MTXQuery {
	/** 卡号 */
	@XStreamAlias("cardId")
	private String cardCode;
	/** 手机号 */
	private String mobilePhone = "";
	/** 会员密码 */
	private String password;
	/** 签名 */
	@XStreamAlias("validateKey")
	protected String sign;

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡信息
	 * @param settings
	 *            会员卡接入设置
	 */
	public MemberDetailQuery(MemberCard memberCard, MemberSettings settings) {
		this.pid = settings.getUsername();
		this.cardCode = memberCard.getCardCode();
		this.password = memberCard.getPassword();
		this.cinameCode = memberCard.getCinemaCode();
		this.param = this.cardCode + this.password + settings.getPassword();
		this.sign = getVerifyInfo(this.param);
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}