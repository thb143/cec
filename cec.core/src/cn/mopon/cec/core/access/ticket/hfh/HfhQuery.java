package cn.mopon.cec.core.access.ticket.hfh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.ticket.hfh.constants.HFHConstants;
import cn.mopon.cec.core.entity.Cinema;
import coo.base.util.CryptoUtils;
import coo.base.util.DateUtils;
import coo.base.util.StringUtils;

/**
 * 请求对象基类。
 */
public abstract class HfhQuery {
	/** 接入商编号 */
	private String userId;
	/** 接入商查询密码 */
	private String userPass;
	/** 接入商地址 */
	private String url;
	/** 方法名 */
	private String method;
	/** 是否需要影院参数 */
	private Boolean needCinemaParams = false;
	/** 是否需要验证信息 */
	private Boolean needCheckValue = false;
	/** 是否需要随机数 */
	private Boolean needRandKey = false;
	/** 参数列表 */
	private List<NameValuePair> nvps = new ArrayList<NameValuePair>();

	/**
	 * 新增请求参数。
	 * 
	 * @param name
	 *            参数名
	 * @param value
	 *            参数值
	 */
	public void addParam(String name, String value) {
		nvps.add(new BasicNameValuePair(name, value));
	}

	/**
	 * 生成影院参数。
	 * 
	 * @param cinema
	 *            影院
	 */
	public void genCinemaParams(Cinema cinema) {
		addParam("cinemaId", cinema.getCode());
		addParam("cinemaLinkId",
				cinema.getTicketSettingsParams(HFHConstants.CINEMA_LINKID));
	}

	/**
	 * 生成场次参数。
	 * 
	 * @param showCode
	 *            排期编码
	 * @param showTime
	 *            放映时间
	 */
	public void genShowParams(String showCode, Date showTime) {
		addParam("showSeqNo", showCode.split("_")[1]);
		String showDateValue = DateUtils.format(showTime, DateUtils.DAY);
		String showTimeValue = DateUtils.format(showTime, "HHmm");
		showTimeValue = Integer.valueOf(showTimeValue).toString();
		addParam("showDate", showDateValue);
		addParam("showTime", showTimeValue);
	}

	/**
	 * 生成随机数。
	 */
	public void genRandKey() {
		addParam("randKey", CryptoUtils.genRandomCode("0123456789", 8));
	}

	/**
	 * 生成验证信息。
	 */
	public void genCheckValue() {
		List<String> params = new ArrayList<>();
		for (NameValuePair nvp : nvps) {
			params.add(nvp.toString());
		}
		addParam("checkValue", CryptoUtils.md5(StringUtils.join(params, "&")));
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Boolean getNeedCinemaParams() {
		return needCinemaParams;
	}

	public void setNeedCinemaParams(Boolean needCinemaParams) {
		this.needCinemaParams = needCinemaParams;
	}

	public Boolean getNeedCheckValue() {
		return needCheckValue;
	}

	public void setNeedCheckValue(Boolean needCheckValue) {
		this.needCheckValue = needCheckValue;
	}

	public Boolean getNeedRandKey() {
		return needRandKey;
	}

	public void setNeedRandKey(Boolean needRandKey) {
		this.needRandKey = needRandKey;
	}

	public List<NameValuePair> getNvps() {
		return nvps;
	}

	public void setNvps(List<NameValuePair> nvps) {
		this.nvps = nvps;
	}
}