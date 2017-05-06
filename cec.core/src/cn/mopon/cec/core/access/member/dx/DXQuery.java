package cn.mopon.cec.core.access.member.dx;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

/**
 * 鼎新查询对象基类。
 */
public class DXQuery {
	/** 访问路径 */
	private String action;
	/** 签名 */
	private String sign = "_sig";
	/** 参数列表 */
	protected List<NameValuePair> params = new ArrayList<NameValuePair>();

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}

}
