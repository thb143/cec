package cn.mopon.cec.core.access.ticket.mtx;

import cn.mopon.cec.core.access.ticket.mtx.vo.RealCheckSeatStateParameter;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketSettings;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import coo.core.xstream.GenericXStream;

/**
 * 锁座请求对象。
 */
@XStreamAlias("LiveRealCheckSeatState")
public class LockSeatQuery extends MtxQuery {
	@XStreamOmitField
	private static XStream xstream;
	private String pXmlString;

	static {
		xstream = new GenericXStream();
	}

	/**
	 * 构造方法。
	 * 
	 * @param order
	 *            选座票订单
	 * @param settings
	 *            选座票设置
	 */
	public LockSeatQuery(TicketOrder order, TicketSettings settings) {
		RealCheckSeatStateParameter params = new RealCheckSeatStateParameter();
		params.setAppCode(settings.getUsername());
		params.setShowCode(order.getShowCode());
		params.setCode(order.getCode());
		params.setSeats(params.getSeats(order));
		// 满天星需要的付费类型说明（中影院线通 20141017 张秦提供）：
		// 1.如果订单采用会员卡支付,payType=70；
		// 2.如果分销商有对应的PayType值,则传入对应的值(见分销商表的PayCode字段)
		// 3.不满足以上条件，则传入H4（中影目前传的是H4）
		// 咨询满天星的回复（20141017 林子杰）：
		// 4.测试账号付费类型：0是非会员，70是会员；分销商的正式账号会有对应的支付类型编码
		params.setChannelNo("0");
		params.setRecvMobilePhone("13888888888");
		params.setTokenId(super.getTokenId());
		String param = params.getShowCode() + params.getCode()
				+ String.valueOf(params.getSeats().size())
				+ params.getChannelNo() + params.getRecvMobilePhone() + tokenId
				+ token;
		params.setVerifyInfo(genVerifyInfo(settings.getUsername(),
				settings.getPassword(), param));
		pXmlString = getXstream().toXML(params);
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取参数pXmlString。
	 * 
	 * @return 返回参数pXmlString。
	 */
	public String getpXmlString() {
		return pXmlString;
	}

	/**
	 * 设置pXmlString。
	 * 
	 * @param pXmlString
	 *            pXmlString
	 */
	public void setpXmlString(String pXmlString) {
		this.pXmlString = pXmlString;
	}
}
