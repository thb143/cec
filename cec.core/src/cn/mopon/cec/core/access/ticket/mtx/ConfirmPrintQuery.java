package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.entity.TicketOrder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 查询打票状态请求对象。
 */
@XStreamAlias("AppPrintTicket")
public class ConfirmPrintQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	/** 合作商方定单号 */
	@XStreamAlias("pOrderNO")
	private String code;
	/** 验证码 */
	private String pValidCode;
	/** 请求类型 */
	private String pRequestType;
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
	 * @param order
	 *            选座票订单
	 */
	public ConfirmPrintQuery(TicketOrder order) {
		this.code = order.getCinemaOrderCode();
		this.pValidCode = order.getVoucher().getVerifyCode();
		this.pRequestType = "1";
		this.param = code + pValidCode + pRequestType + tokenId + token;
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

	/**
	 * @return 请求类型。
	 */
	public String getpRequestType() {
		return pRequestType;
	}

	/**
	 * 
	 * @param pRequestType
	 *            设置请求类型。
	 */
	public void setpRequestType(String pRequestType) {
		this.pRequestType = pRequestType;
	}

	/**
	 * 
	 * @return 验证码。
	 */
	public String getpValidCode() {
		return pValidCode;
	}

	/**
	 * 
	 * @param pValidCode
	 *            设置验证码。
	 */
	public void setpValidCode(String pValidCode) {
		this.pValidCode = pValidCode;
	}
}
