package cn.mopon.cec.core.access.ticket.dx;

import cn.mopon.cec.core.access.ticket.converter.XsltUtils;
import cn.mopon.cec.core.access.ticket.dx.enums.DXReplyStatus;
import coo.base.exception.BusinessException;

/**
 * 鼎新服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class DxServiceReply<T extends DxReply> {
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象。
	 */
	public DxServiceReply(T reply) {
		this.reply = reply;
	}

	/**
	 * 生成响应对象。
	 * 
	 * @param replyXml
	 *            响应XML
	 * @throws Exception
	 *             抛出异常
	 */
	public void genReply(String replyXml) throws Exception {
		replyXml = XsltUtils.transform(replyXml, getClass(), reply.getClass());
		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(replyXml, reply);
		if (reply.getStatus() == DXReplyStatus.FAIL) {
			throw new BusinessException(reply.getErrorMessage());
		}
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}