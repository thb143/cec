package cn.mopon.cec.core.access.member.mtx;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import coo.base.exception.BusinessException;

/**
 * 满天星服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class MTXServiceReply<T extends MTXReply> {
	private Logger log = LoggerFactory.getLogger(getClass());
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            服务响应对象
	 */
	public MTXServiceReply(T reply) {
		this.reply = reply;
	}

	/**
	 * 设置SOAP消息，填充响应对象。
	 * 
	 * @param soapMessage
	 *            SOAP消息
	 * @return 返回响应消息。
	 * @throws Exception
	 *             异常信息。
	 */
	public String setSoapMessage(SOAPMessage soapMessage) throws Exception {
		String replyMessage = genReplyMessage1(soapMessage);
		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(replyMessage, this.reply);
		if (!"0".equals(reply.getCode())) {
			log.info("调用接口发生异常：[{}]", reply.getMsg());
			throw new BusinessException(reply.getCode());
		}
		return replyMessage;
	}

	/**
	 * 生成响应消息。
	 * 
	 * @param strXml
	 *            XMl消息
	 * @return 返回经过转换的响应消息。
	 * @throws Exception
	 *             异常信息。
	 */
	public String genReplyMessage(String strXml) throws Exception {
		InputStream xsltInputStream = MTXServiceReply.class
				.getResourceAsStream("xslt/" + reply.getClass().getSimpleName()
						+ ".xslt");
		StreamSource xsl = new StreamSource(xsltInputStream);
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer(xsl);
		StringWriter writer = new StringWriter();
		transformer.transform(new StreamSource(new StringReader(strXml)),
				new StreamResult(writer));
		strXml = writer.getBuffer().toString().replaceAll("\n|\r|\t", "");
		xsltInputStream.close();
		writer.close();
		reply.getXstream().ignoreUnknownElements();
		reply.getXstream().fromXML(strXml, this.getReply());
		return strXml;
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
	private String genReplyMessage1(SOAPMessage soapMessage) throws Exception {
		Node node = soapMessage.getSOAPBody().getElementsByTagName("ns:return")
				.item(0);
		String replyMessage = node.getFirstChild().getNodeValue();
		InputStream xsltInputStream = MTXServiceReply.class
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

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}
