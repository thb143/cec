package cn.mopon.cec.core.access.ticket.mtx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 确认订单请求参数。
 */
@XStreamAlias("SellTicketParameter")
public class SellTicketParameter {
	@XStreamAlias("AppCode")
	private String appCode;
	@XStreamAlias("FeatureAppNo")
	private String featureAppNo;
	@XStreamAlias("SerialNum")
	private String serialNum;
	@XStreamAlias("Printpassword")
	private String printpassword;
	@XStreamAlias("Balance")
	private Integer balance;
	@XStreamAlias("PayType")
	private String payType;
	@XStreamAlias("RecvMobilePhone")
	private String recvMobilePhone;
	@XStreamAlias("SendType")
	private String sendType;
	@XStreamAlias("PayResult")
	private String payResult;
	@XStreamAlias("IsCmtsPay")
	private boolean isCmtsPay;
	@XStreamAlias("IsCmtsSendCode")
	private boolean isCmtsSendCode;
	@XStreamAlias("PayMobile")
	private String payMobile;
	@XStreamAlias("BookSign")
	private String bookSign;
	@XStreamAlias("Payed")
	private double payed;
	@XStreamAlias("SendModeID")
	private String sendModeID;
	@XStreamAlias("PaySeqNo")
	private String paySeqNo;
	/** 令牌ID */
	@XStreamAlias("TokenID")
	protected String tokenIds;
	/** 检验信息 */
	@XStreamAlias("VerifyInfo")
	protected String verifyInfo;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getFeatureAppNo() {
		return featureAppNo;
	}

	public void setFeatureAppNo(String featureAppNo) {
		this.featureAppNo = featureAppNo;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getPrintpassword() {
		return printpassword;
	}

	public void setPrintpassword(String printpassword) {
		this.printpassword = printpassword;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRecvMobilePhone() {
		return recvMobilePhone;
	}

	public void setRecvMobilePhone(String recvMobilePhone) {
		this.recvMobilePhone = recvMobilePhone;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public boolean isCmtsPay() {
		return isCmtsPay;
	}

	public void setCmtsPay(boolean isCmtsPay) {
		this.isCmtsPay = isCmtsPay;
	}

	public boolean isCmtsSendCode() {
		return isCmtsSendCode;
	}

	public void setCmtsSendCode(boolean isCmtsSendCode) {
		this.isCmtsSendCode = isCmtsSendCode;
	}

	public String getPayMobile() {
		return payMobile;
	}

	public void setPayMobile(String payMobile) {
		this.payMobile = payMobile;
	}

	public String getBookSign() {
		return bookSign;
	}

	public void setBookSign(String bookSign) {
		this.bookSign = bookSign;
	}

	public double getPayed() {
		return payed;
	}

	public void setPayed(double payed) {
		this.payed = payed;
	}

	public String getSendModeID() {
		return sendModeID;
	}

	public void setSendModeID(String sendModeID) {
		this.sendModeID = sendModeID;
	}

	public String getPaySeqNo() {
		return paySeqNo;
	}

	public void setPaySeqNo(String paySeqNo) {
		this.paySeqNo = paySeqNo;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
