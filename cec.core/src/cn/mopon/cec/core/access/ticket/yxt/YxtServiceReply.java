package cn.mopon.cec.core.access.ticket.yxt;

import cn.mopon.cec.core.access.ticket.converter.XsltUtils;
import coo.base.exception.BusinessException;

/**
 * YXT服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class YxtServiceReply<T extends YxtReply> {
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象
	 */
	public YxtServiceReply(T reply) {
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
		if (!"001".equals(reply.getCode())) {
			throw new BusinessException(reply.getMsg());
		}
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}