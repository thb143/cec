package cn.mopon.cec.core.access.ticket.hfh;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import cn.mopon.cec.core.access.ticket.converter.XsltUtils;
import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;

/**
 * 火凤凰(幸福蓝海)服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class HfhServiceReply<T extends HfhReply> {
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象
	 */
	public HfhServiceReply(T reply) {
		this.reply = reply;
		reply.getXstream().alias("data", HfhServiceReply.class);
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
		replyXml = getNestedReplyXml(replyXml);
		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(replyXml, reply);
		if (!"0".equals(reply.getResult())) {
			throw new BusinessException(reply.getMessage());
		}
	}

	/**
	 * 获取内层响应消息。
	 * 
	 * @param replyXml
	 *            外层响应消息
	 * @return 返回内层响应消息。
	 * @throws Exception
	 *             抛出异常。
	 */
	private String getNestedReplyXml(String replyXml) throws Exception {
		try (ByteArrayInputStream replyIn = new ByteArrayInputStream(
				replyXml.getBytes(Encoding.UTF_8));) {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document document = builder.parse(replyIn);
			replyXml = document.getFirstChild().getTextContent();
			return XsltUtils.transform(replyXml, getClass(), reply.getClass());
		}
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}