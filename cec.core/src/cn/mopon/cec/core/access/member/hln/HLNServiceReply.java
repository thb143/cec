package cn.mopon.cec.core.access.member.hln;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;

import cn.mopon.cec.core.access.member.enums.MemberReplyError;
import coo.base.exception.BusinessException;
import coo.base.util.StringUtils;

/**
 * 国标服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class HLNServiceReply<T extends HLNReply> {
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象
	 */
	public HLNServiceReply(T reply) {
		this.reply = reply;
		reply.getXstream().alias("queryPayDetailsResponse",
				HLNServiceReply.class);
	}

	/**
	 * 设置SOAP消息，填充响应对象。
	 * 
	 * @param soapMessage
	 *            SOAP消息
	 * @throws Exception
	 *             异常信息。
	 */
	public void setSoapMessage(SOAPMessage soapMessage) throws Exception {
		String replyMessage = genReplyMessage(soapMessage);
		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(replyMessage, this.reply);
		if ("7".equals(reply.getResponseCode())) {
			throw new BusinessException(
					MemberReplyError.MEMBER_LACK_BALANCE.getValue());
		} else if ("6".equals(reply.getResponseCode())) {
			throw new BusinessException(
					MemberReplyError.MEMBER_NOT_EXIST.getValue());
		} else if ("10".equals(reply.getResponseCode())) {
			throw new BusinessException(
					MemberReplyError.MEMBERCHIP_SEAT_PAY.getValue());
		} else if (!"0".equals(reply.getResponseCode())) {
			throw new BusinessException(reply.getResponseMsg());
		}
	}

	/**
	 * 生成响应消息。
	 * 
	 * @param soapMessage
	 *            SOAP消息
	 * @return 返回经过转换的响应消息。
	 * @throws Exception
	 *             异常信息。
	 */
	private String genReplyMessage(SOAPMessage soapMessage) throws Exception {
		String replyMessage = getReponseXml(soapMessage);
		InputStream xsltInputStream = HLNServiceReply.class
				.getResourceAsStream("xslt/" + reply.getClass().getSimpleName()
						+ ".xslt");
		StreamSource xsl = new StreamSource(xsltInputStream);
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer(xsl);
		StringWriter writer = new StringWriter();
		transformer.transform(new StreamSource(new StringReader(replyMessage)),
				new StreamResult(writer));

		replyMessage = writer.getBuffer().toString().replaceAll("\n|\r|\t", "");
		xsltInputStream.close();
		writer.close();
		return replyMessage;
	}

	/**
	 * 获取响应的XML。
	 * 
	 * @param soapMessage
	 *            SOAP消息
	 * @return 返回响应的XML。
	 * @throws Exception
	 *             异常信息。
	 */
	private String getReponseXml(SOAPMessage soapMessage) throws Exception {
		Node node = soapMessage.getSOAPBody().getChildNodes().item(0);
		DOMSource source = new DOMSource(node.getOwnerDocument());
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.transform(source, result);
		String responseXml = writer.getBuffer().toString();
		int start = responseXml.indexOf("Response>") + 9;
		int length = responseXml.lastIndexOf("</soap:Body>") - start;
		responseXml = StringUtils.mid(responseXml, start, length);
		responseXml = StringUtils.substringBeforeLast(responseXml, "<");
		return responseXml;
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}