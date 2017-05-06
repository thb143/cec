package cn.mopon.cec.core.access.member.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.base.util.CryptoUtils;
import coo.core.xstream.GenericXStream;

/**
 * 请求对象基类。
 */
public abstract class MTXQuery {
	@XStreamOmitField
	protected XStream xstream = new GenericXStream();
	/** 合作商代码 */
	@XStreamAlias("partnerCode")
	protected String pid;
	/** 影院代码 */
	@XStreamAlias("placeNo")
	protected String cinameCode;
	/** 合作商流水号 */
	protected String partnerId = "";
	@XStreamOmitField
	protected String param;

	/**
	 * 获取接口访问的验证信息。
	 * 
	 * @param param
	 *            参数
	 * @return 返回验证信息字符串。
	 */
	public String getVerifyInfo(String param) {
		return CryptoUtils
				.md5((this.pid + this.cinameCode + this.partnerId + param)
						.toLowerCase()).toLowerCase().substring(8, 24);
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCinameCode() {
		return cinameCode;
	}

	public void setCinameCode(String cinameCode) {
		this.cinameCode = cinameCode;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
}
