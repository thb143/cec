package cn.mopon.cec.core.access.ticket.mtx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 令牌。
 */
public class Token {
	/** 返回结果号 */
	@XStreamAlias("resultCode")
	private String resultCode;
	/** 令牌Id */
	@XStreamAlias("tokenId")
	private String tokenId;
	/** 令牌 */
	@XStreamAlias("token")
	private String token;
	/** 令牌生命周期(单位：秒) */
	@XStreamAlias("timeOut")
	private String timeOut;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getTokenId() {

		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
}
