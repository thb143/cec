package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.Hall;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询影厅座位信息请求对象。
 */
@XStreamAlias("GetHallSite")
public class SeatQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 影院编码 */
	@XStreamAlias("pCinemaID")
	private String cinemaCode;
	/** 影院编码 */
	@XStreamAlias("pHallID")
	private String hallCode;
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
	 * @param hall
	 *            影厅
	 */
	public SeatQuery(Hall hall) {
		this.cinemaCode = hall.getCinema().getCode();
		this.hallCode = hall.getCode();
		this.param = cinemaCode + hallCode + tokenId + token;
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

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}
