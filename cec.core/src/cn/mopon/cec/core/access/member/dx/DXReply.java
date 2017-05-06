package cn.mopon.cec.core.access.member.dx;

import cn.mopon.cec.core.access.ticket.dx.enums.DXReplyStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 鼎新响应对象基类。
 */
public class DXReply {
	/** 返回状态 */
	@XStreamConverter(value = IEnumConverter.class, types = DXReplyStatus.class)
	@XStreamAlias("status")
	private DXReplyStatus replyStatus;
	/** 错误消息 */
	private String errorMessage;
	/** 错误码 */
	private String errorCode;
	/** 转成Xml格式 */
	@XStreamOmitField
	private boolean isXml = true;
	protected XStream xstream = new GenericXStream();

	public DXReplyStatus getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(DXReplyStatus replyStatus) {
		this.replyStatus = replyStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isXml() {
		return isXml;
	}

	public void setXml(boolean isXml) {
		this.isXml = isXml;
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

}
