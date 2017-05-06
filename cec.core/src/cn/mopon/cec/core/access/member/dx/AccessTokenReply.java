package cn.mopon.cec.core.access.member.dx;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AccessToken响应对象。
 */
public class AccessTokenReply extends DXReply {
	@JsonProperty("access_token")
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
