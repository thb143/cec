package cn.mopon.cec.core.access.ticket.mtx;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询影院基础信息请求对象。
 */
@XStreamAlias("GetCinema")
public class CinemaQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 影院编码 */
	@XStreamOmitField
	private String code;
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
	 * @param code
	 *            影院编码
	 */
	public CinemaQuery(String code) {
		this.code = code;
		this.param = tokenId + token;
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setTokenId(String tokenId) {
		this.tokenIds = tokenId;
	}

	public void setVerifyInfo(String verifyInfo) {
		this.verifyInfo = verifyInfo;
	}
}