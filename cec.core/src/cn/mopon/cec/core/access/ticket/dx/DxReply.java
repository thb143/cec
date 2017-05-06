package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.access.ticket.dx.enums.DXReplyStatus;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 鼎新响应对象基类。
 */
public abstract class DxReply {
	/** 返回状态 */
	@XStreamConverter(value = IEnumConverter.class, types = DXReplyStatus.class)
	private DXReplyStatus status;
	/** 错误消息 */
	private String errorMessage;
	/** 错误码 */
	private String errorCode;

	/**
	 * 获取XStream对象。
	 * 
	 * @return 返回XStream对象。
	 */
	abstract XStream getXstream();

	public DXReplyStatus getStatus() {
		return status;
	}

	public void setStatus(DXReplyStatus status) {
		this.status = status;
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
}