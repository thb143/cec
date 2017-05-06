package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.ChannelShow;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 获取对应排期的座位图的状态。
 */
@XStreamAlias("GetJsonSiteState")
public class SessionSeatQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 排期应用号 */
	@XStreamAlias("pFeatureAppNo")
	private String showCode;
	/** 检验信息 */
	@XStreamAlias("pVerifyInfo")
	protected String verifyInfo;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	public SessionSeatQuery(ChannelShow channelShow) {
		this.showCode = channelShow.getShowCode();
		this.param = showCode;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getShowCode() {
		return showCode;
	}

	public void setShowCode(String showCode) {
		this.showCode = showCode;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}