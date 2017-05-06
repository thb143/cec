package cn.mopon.cec.core.access.member.hfh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.ticket.hfh.constants.HFHConstants;
import cn.mopon.cec.core.entity.Cinema;
import coo.base.util.CryptoUtils;

/**
 * 请求对象基类。
 */
public abstract class HFHQuery {
	/** 接入商编号 */
	private String userId;
	/** 接入商查询密码 */
	private String userPass;
	/** 接入商地址 */
	private String url;
	/** 方法名 */
	private String method;
	/** 是否需要影院参数 */
	private Boolean needCinemaParams = true;

	List<NameValuePair> nvps = new ArrayList<NameValuePair>();

	/**
	 * 生成影院参数。
	 * 
	 * @param cinema
	 *            影院
	 */
	public void genCinemaParams(Cinema cinema) {
		basicNameValuePair("cinemaLinkId",
				cinema.getMemberSettingsParams(HFHConstants.CINEMA_LINKID));
	}

	/**
	 * NameValuePair实现请求参数的封装
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	public void basicNameValuePair(String name, String value) {
		nvps.add(new BasicNameValuePair(name, value));
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

	public List<NameValuePair> getNvps() {
		return nvps;
	}

	public void setNvps(List<NameValuePair> nvps) {
		this.nvps = nvps;
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

	/**
	 * 返回加密后的参数串。
	 * 
	 * @param nvps
	 *            http请求参数列表
	 * @return 加密后参数
	 */
	public String getCheckValue(List<NameValuePair> nvps) {
		StringBuilder sb = new StringBuilder();
		for (NameValuePair nvp : nvps) {
			if (sb.length() > 0) {
				sb.append("&").append(nvp);
			} else {
				sb.append(nvp);
			}
		}
		return CryptoUtils.md5(sb.toString());
	}

	/**
	 * 获取指定长度的随便数。
	 * 
	 * @param strLength
	 *            长度
	 * @return 返回随机数。
	 */
	public String getFixLenthString(int strLength) {
		Random rm = new Random();
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		String fixLenthString = String.valueOf(pross);
		return fixLenthString.substring(2, strLength + 1);
	}
}
