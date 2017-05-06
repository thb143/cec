package cn.mopon.cec.core.access.ticket.mtx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.base.exception.UncheckedException;
import coo.base.util.CryptoUtils;

/**
 * 请求对象基类。
 */
public abstract class MtxQuery {
	/** 应用编码 */
	@XStreamAlias("pAppCode")
	protected String appCode;
	/** 令牌ID */
	@XStreamOmitField
	protected String tokenId = "1829";
	/** 检验信息 */
	@XStreamOmitField
	protected String verifyInfo;
	/** 验证密钥 */
	@XStreamOmitField
	protected String checkKey;
	/** 令牌 */
	@XStreamOmitField
	protected String token = "abcdef";
	/** 参数 */
	@XStreamOmitField
	protected String param;

	/**
	 * 获取XStream对象。
	 * 
	 * @return 返回XStream对象。
	 */
	abstract XStream getXstream();

	/**
	 * 生成检验信息。
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param params
	 *            参数
	 * @return 返回检验信息。
	 */
	protected String genVerifyInfo(String username, String password,
			String params) {
		try {
			return CryptoUtils.md5(
					URLEncoder.encode(username + params + password, "UTF-8")
							.toLowerCase()).substring(8, 24);
		} catch (UnsupportedEncodingException e) {
			throw new UncheckedException("生成满天星接口检验信息发生异常。", e);
		}
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getVerifyInfo() {
		return verifyInfo;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}

	public String getCheckKey() {
		return checkKey;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}