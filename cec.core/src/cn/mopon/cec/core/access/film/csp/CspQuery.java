package cn.mopon.cec.core.access.film.csp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

/**
 * csp 请求查询基类。
 */
public class CspQuery {
	/** 账号 */
	private String account;
	/** 密钥 */
	private String key;

	private List<NameValuePair> nvps = new ArrayList<NameValuePair>();

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<NameValuePair> getNvps() {
		return nvps;
	}

	public void setNvps(List<NameValuePair> nvps) {
		this.nvps = nvps;
	}
}