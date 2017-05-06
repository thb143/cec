package cn.mopon.cec.core.access.ticket.std;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Node;

import cn.mopon.cec.core.access.ticket.converter.XsltUtils;
import cn.mopon.cec.core.access.ticket.std.StdReply.StdReplyStatus;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.exception.BusinessException;
import coo.core.xstream.DateConverter;

/**
 * 国标服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
@XStreamAlias("OnlineTicketingServiceReply")
public class StdServiceReply<T extends StdReply> {
	@XStreamAsAttribute
	@XStreamAlias("Version")
	private String version;
	@XStreamAsAttribute
	@XStreamAlias("Datetime")
	@XStreamConverter(value = DateConverter.class, strings = { "yyyy-MM-dd'T'HH:mm:ss" })
	private Date datetime;
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象
	 */
	public StdServiceReply(T reply) {
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
		replyXml = getNestedReplyXml(replyXml);
		verifySign();

		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(replyXml, this);

		if (reply.getStatus() == StdReplyStatus.Failure) {
			throw new BusinessException(reply.getErrorMessage());
		}
		reply.processResult();
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
		try (InputStream replyIn = new ByteArrayInputStream(replyXml.getBytes());) {
			SOAPMessage replyMessage = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(null,
					replyIn);
			SOAPBody soapBody = replyMessage.getSOAPBody();
			Node node = soapBody.getElementsByTagName("m:msg").item(0);
			replyXml = node.getFirstChild().getNodeValue();
			return XsltUtils.transform(replyXml, getClass(), reply.getClass());
		}
	}

	/**
	 * 验证签名。
	 */
	private void verifySign() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}