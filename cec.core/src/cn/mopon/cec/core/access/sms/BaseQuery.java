package cn.mopon.cec.core.access.sms;

import cn.mopon.cec.core.access.sms.enums.SmsType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 短信请求基类。
 */
public abstract class BaseQuery {
	@XStreamOmitField
	protected XStream xstream = new GenericXStream();
	@XStreamOmitField
	protected SmsType type;

	/**
	 * 生成短信密文。
	 * 
	 * @param secKey
	 *            秘钥
	 * @return 返回生成后的密文。
	 */
	public abstract String genSecretMsg(String secKey);

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

	public SmsType getType() {
		return type;
	}

	public void setType(SmsType type) {
		this.type = type;
	}
}
