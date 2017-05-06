package cn.mopon.cec.core.access.sms;

import cn.mopon.cec.core.access.sms.enums.SmsType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.constants.Chars;
import coo.base.util.CryptoUtils;

/**
 * 短信请求。
 */
@XStreamAlias("sendSMS")
public class SmsQuery extends BaseQuery {
	/** 请求来源 */
	private String clientCode;
	/** 手机号码 */
	private String mobileNo;
	/** 内容 */
	private String content;
	/** 时间戳 */
	private String timeStamp;
	/** 密文 */
	private String secretMsg;

	/**
	 * 构造方法。
	 */
	public SmsQuery() {
		type = SmsType.SMS;
	}

	@Override
	public String genSecretMsg(String secKey) {
		StringBuilder sb = new StringBuilder();
		sb.append(clientCode).append(Chars.BAR);
		sb.append(mobileNo).append(Chars.BAR);
		sb.append(content).append(Chars.BAR);
		sb.append(timeStamp).append(Chars.BAR);
		sb.append(secKey);
		return CryptoUtils.md5(sb.toString());
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSecretMsg() {
		return secretMsg;
	}

	public void setSecretMsg(String secretMsg) {
		this.secretMsg = secretMsg;
	}

}
