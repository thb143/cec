package cn.mopon.cec.core.access.member.vo;

/**
 * 会员卡信息。
 */
public class MemberCard {
	/** 影院编码 */
	private String cinemaCode;
	/** 会员卡号 */
	private String cardCode;
	/** 芯片号 */
	private String chipCode;
	/** 会员卡密码 */
	private String password;
	/** 账户余额 */
	private double balance = 0d;
	/** 账户积分 */
	private double score = 0d;
	/** 账户级别名称 */
	private String accLevelName;
	/** 过期时间 */
	private String expirationTime;
	/** 折扣价 */
	private double discountPrice = 0d;
	/** 卡状态 */
	private String status;
	// /** 账户积分 */
	// private Integer integral = 0;
	/** 卡政策名称 */
	private String memberPolicy;
	/** 卡媒体 */
	private String cardMedia;
	/** 累积充值金额 */
	private double sumBalance = 0d;
	/** 累积积分 */
	private Integer sumIntegral = 0;
	/** 会员名称 */
	private String memberName;
	/** 会员性别 */
	private String memberSex;
	/** 会员手机 */
	private String memberPhone;
	/** 会员生日 */
	private String memberBirthday;
	/** 会员年费扣除政策 */
	private String memberAnnualFee;
	/** 有效期 */
	private String validity;
	/** 发卡时间 */
	private String sendCardTime;
	/** 激活时间 */
	private String activationTime;

	/**
	 * 构造方法。
	 */
	public MemberCard() {

	}

	/**
	 * 构造方法。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @param cardCode
	 *            会员卡号
	 * @param cardPass
	 *            会员卡密码
	 */
	public MemberCard(String cinemaCode, String cardCode, String cardPass) {
		this.cinemaCode = cinemaCode;
		this.cardCode = cardCode;
		this.password = cardPass;
	}

	/**
	 * 构造方法。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @param schipCode
	 *            会员卡芯片号
	 */
	public MemberCard(String cinemaCode, String schipCode) {
		this.cinemaCode = cinemaCode;
		this.chipCode = schipCode;
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

	public String getChipCode() {
		return chipCode;
	}

	public void setChipCode(String chipCode) {
		this.chipCode = chipCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemberPolicy() {
		return memberPolicy;
	}

	public void setMemberPolicy(String memberPolicy) {
		this.memberPolicy = memberPolicy;
	}

	public String getCardMedia() {
		return cardMedia;
	}

	public void setCardMedia(String cardMedia) {
		this.cardMedia = cardMedia;
	}

	public double getSumBalance() {
		return sumBalance;
	}

	public void setSumBalance(double sumBalance) {
		this.sumBalance = sumBalance;
	}

	public Integer getSumIntegral() {
		return sumIntegral;
	}

	public void setSumIntegral(Integer sumIntegral) {
		this.sumIntegral = sumIntegral;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(String memberSex) {
		this.memberSex = memberSex;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemberBirthday() {
		return memberBirthday;
	}

	public void setMemberBirthday(String memberBirthday) {
		this.memberBirthday = memberBirthday;
	}

	public String getMemberAnnualFee() {
		return memberAnnualFee;
	}

	public void setMemberAnnualFee(String memberAnnualFee) {
		this.memberAnnualFee = memberAnnualFee;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getSendCardTime() {
		return sendCardTime;
	}

	public void setSendCardTime(String sendCardTime) {
		this.sendCardTime = sendCardTime;
	}

	public String getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(String activationTime) {
		this.activationTime = activationTime;
	}

}
