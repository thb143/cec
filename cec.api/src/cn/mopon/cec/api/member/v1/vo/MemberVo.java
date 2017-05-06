package cn.mopon.cec.api.member.v1.vo;

import cn.mopon.cec.core.access.member.vo.MemberCard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import coo.base.util.BeanUtils;

/**
 * 会员卡信息。
 */
@JsonInclude(Include.NON_NULL)
public class MemberVo {
	/** 影院编码 */
	private String cinemaCode;
	/** 会员卡号 */
	private String cardCode;
	/** 账户余额 */
	private double balance;
	/** 账户积分 */
	private double score;
	/** 账户级别名称 */
	private String accLevelName;
	/** 过期时间 */
	private String expirationTime;
	/** 是否可用 */
	private String status;

	/**
	 * 构造方法。
	 * 
	 * @param card
	 *            会员卡
	 */
	public MemberVo(MemberCard card) {
		BeanUtils.copyFields(card, this);
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getAccLevelName() {
		return accLevelName;
	}

	public void setAccLevelName(String accLevelName) {
		this.accLevelName = accLevelName;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
