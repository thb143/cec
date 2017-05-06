package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 响应对象基类。
 */
public abstract class MtxReply {
	@XStreamAlias("ResultCode")
	private String resultCode;

	/**
	 * 获取XStream对象。
	 * 
	 * @return 返回XStream对象。
	 */
	abstract XStream getXstream();

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}