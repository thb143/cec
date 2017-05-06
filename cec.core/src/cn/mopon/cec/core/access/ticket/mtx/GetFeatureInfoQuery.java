package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 根据影片编码,影院编码读取上映场次排期请求对象。
 */
@XStreamAlias("GetFeatureInfo")
public class GetFeatureInfoQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	@XStreamAlias("pCinemaID")
	private String cinemaCode;
	@XStreamAlias("pFilmNo")
	private String filmCode;
	@XStreamAlias("pPlanDate")
	private String showDate;
	/** 令牌ID */
	@XStreamAlias("pTokenID")
	protected String tokenIds = "1829";
	/** 检验信息 */
	@XStreamAlias("pVerifyInfo")
	protected String verifyInfo;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造函数。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @param filmCode
	 *            影片编码
	 * @param showDate
	 *            排期时间
	 */
	public GetFeatureInfoQuery(String cinemaCode, String filmCode,
			String showDate) {
		this.cinemaCode = cinemaCode;
		this.filmCode = filmCode;
		this.showDate = showDate;
		this.param = this.cinemaCode + this.filmCode + this.showDate
				+ this.tokenId + this.token;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCinemaCode() {
		return cinemaCode;
	}

	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public String getVerifyInfo() {
		return verifyInfo;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
}