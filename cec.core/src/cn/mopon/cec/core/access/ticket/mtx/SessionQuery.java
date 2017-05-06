package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.Cinema;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询电影院放映计划信息请求对象。
 */
@XStreamAlias("GetCinemaAllPlan")
public class SessionQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 影院编码 */
	@XStreamAlias("pCinemaID")
	private String cinemaCode;
	/** 令牌ID */
	@XStreamAlias("pTokenID")
	protected String tokenIds;
	/** 检验信息 */
	@XStreamAlias("pVerifyInfo")
	protected String verifyInfo;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 */
	public SessionQuery(Cinema cinema) {
		this.cinemaCode = cinema.getCode();
		this.param = cinemaCode + tokenId + token;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
