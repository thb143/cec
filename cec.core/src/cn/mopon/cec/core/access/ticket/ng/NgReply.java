package cn.mopon.cec.core.access.ticket.ng;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * 查询对象响应基类。
 * 
 */
public abstract class NgReply {
	@XStreamAsAttribute
	@XStreamAlias("Id")
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("Status")
	private ReplyStatus status;
	@XStreamAsAttribute
	@XStreamAlias("ErrorCode")
	private String errorCode;
	@XStreamAsAttribute
	@XStreamAlias("ErrorMessage")
	private String errorMessage;

	/**
	 * 获取XStream对象。
	 * 
	 * @return 返回XStream对象。
	 */
	abstract XStream getXstream();

	/**
	 * 该方法在解析完响应消息后调用，用于对返回结果进行二次处理，在有需要的子类中覆盖该方法来实现。
	 */
	protected void processResult() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ReplyStatus getStatus() {
		return status;
	}

	public void setStatus(ReplyStatus status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * 响应对象返回状态枚举类。
	 */
	protected enum ReplyStatus {
		Success, Failure;
	}
}
