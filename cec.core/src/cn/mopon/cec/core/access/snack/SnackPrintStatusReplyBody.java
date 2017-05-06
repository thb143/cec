package cn.mopon.cec.core.access.snack;

/**
 * 查看卖品打印状态响应报文体。
 */
public class SnackPrintStatusReplyBody {
	/** 错误编号(0—未打印 1：已打印)，当为“未打印”时，可以退票 */
	private String errCode;
	/** 错误信息 */
	private String errMsg;

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