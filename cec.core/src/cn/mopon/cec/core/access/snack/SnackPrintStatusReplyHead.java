package cn.mopon.cec.core.access.snack;

/**
 * 查看卖品打印状态响应报文头。
 */
public class SnackPrintStatusReplyHead {
	/** 交易ID,及接口名称 */
	private String tradeId;
	/** 报文请求时间 */
	private String timeStamp;
	/** 操作状态,0为成功 */
	private String errCode;
	/** 操作描述 */
	private String errMsg;

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}