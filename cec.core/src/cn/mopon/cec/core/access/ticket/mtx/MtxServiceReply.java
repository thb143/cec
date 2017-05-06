package cn.mopon.cec.core.access.ticket.mtx;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Node;

import cn.mopon.cec.core.access.ticket.converter.XsltUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;
import coo.core.jackson.GenericObjectMapper;

/**
 * 满天星服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class MtxServiceReply<T extends MtxReply> {
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            服务响应对象
	 */
	public MtxServiceReply(T reply) {
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
		replyXml = XsltUtils.transform(getNestedReplyXml(replyXml), getClass(),
				reply.getClass());
		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(replyXml, reply);
		if (!(reply instanceof RefundTicketReply)
				&& !"0".equals(reply.getResultCode())
				&& !"1".equals(reply.getResultCode())) {
			throw new BusinessException(reply.getResultCode());
		}
	}

	/**
	 * 生成响应对象。
	 * 
	 * @param replyJson
	 *            响应Json
	 * @throws Exception
	 *             抛出异常
	 */
	@SuppressWarnings("unchecked")
	public void genReplyJson(String replyJson) throws Exception {
		replyJson = getNestedReplyXml(replyJson);
		ObjectMapper mapper = new GenericObjectMapper();
		reply = (T) mapper.readValue(replyJson, reply.getClass());
		if (!"0".equals(reply.getResultCode())) {
			throw new BusinessException(reply.getResultCode());
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
		try (InputStream replyIn = new ByteArrayInputStream(
				replyXml.getBytes(Encoding.UTF_8));) {
			SOAPMessage replyMessage = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_1_PROTOCOL).createMessage(null,
					replyIn);
			Node node = replyMessage.getSOAPBody()
					.getElementsByTagName("ns:return").item(0);
			return node.getFirstChild().getNodeValue();
		}
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}