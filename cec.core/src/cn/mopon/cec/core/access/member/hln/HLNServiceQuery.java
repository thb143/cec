package cn.mopon.cec.core.access.member.hln;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

import coo.base.constants.Encoding;

/**
 * 国标服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class HLNServiceQuery<T extends HLNQuery> {
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 */
	public HLNServiceQuery(T query) {
		this.query = query;
	}

	/**
	 * 获取服务请求的SOAP消息。
	 * 
	 * @return 返回服务请求的SOAP消息。
	 * @throws Exception
	 *             异常信息。
	 */
	public SOAPMessage getSoapMessage() throws Exception {
		SOAPMessage soapMessage = MessageFactory.newInstance(
				SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
		SOAPEnvelope soapEnv = soapMessage.getSOAPPart().getEnvelope();
		soapEnv.removeNamespaceDeclaration(soapEnv.getPrefix());
		soapEnv.setPrefix("soap");
		soapEnv.getHeader().detachNode();
		soapEnv.getBody().setPrefix("soap");
		soapEnv.getBody().addDocument(genQueryDocument());
		SOAPElement rootNode = (SOAPElement) soapEnv.getBody().getFirstChild();
		QName msgNode = rootNode.createQName("m", "xmlns");
		rootNode.addAttribute(msgNode, "http://www.crifst.ac.cn/2013/OTSAPI");
		rootNode.setPrefix("soap");
		return soapMessage;
	}

	/**
	 * 生成请求的XML文档对象。
	 * 
	 * @return 返回请求的XML文档对象。
	 * @throws Exception
	 *             异常信息。
	 */
	private Document genQueryDocument() throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(query
				.getXstream().toXML(this.getQuery()).getBytes(Encoding.UTF_8));
		return documentBuilder.parse(xmlInputStream);
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}
}