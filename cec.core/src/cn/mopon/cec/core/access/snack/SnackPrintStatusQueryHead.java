package cn.mopon.cec.core.access.snack;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAliasType;

import coo.base.util.CryptoUtils;
import coo.base.util.DateUtils;

/**
 * 查看卖品打印状态请求报文头。
 * 
 */
@XStreamAliasType("head")
public class SnackPrintStatusQueryHead {
	/** 交易ID */
	private String tradeId;
	/** 报文请求时间,格式yyyyMMddHHmmss */
	private String timeStamp;
	/** 校验码,校验数据包是否有效,=MD5(timestamp+key),其中key由平台分配（客户端密码） */
	private String validCode;
	/** 客户端的应用ID，用于辨认客户端（客户端编号） */
	private String appkey;

	/**
	 * 构造方法。
	 * 
	 * @param appkey
	 *            客户端编号
	 * @param password
	 *            客户端密码
	 * @param tradeId
	 *            交易ID
	 */
	public SnackPrintStatusQueryHead(String appkey, String password,
			String tradeId) {
		this.appkey = appkey;
		this.timeStamp = DateUtils.format(new Date(), DateUtils.SECOND_N);
		this.validCode = CryptoUtils.md5(timeStamp + password);
		this.tradeId = tradeId;
	}

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

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
}