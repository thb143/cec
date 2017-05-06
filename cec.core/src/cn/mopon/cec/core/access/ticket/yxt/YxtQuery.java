package cn.mopon.cec.core.access.ticket.yxt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * YXT请求查询基类。
 */
public abstract class YxtQuery {
	/** 访问路径 */
	private String action;
	/** 参数列表 */
	protected List<NameValuePair> params = new ArrayList<>();

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}

	/**
	 * 添加参数。
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void addParam(String name, String value) {
		this.params.add(new BasicNameValuePair(name, value));
	}
}
