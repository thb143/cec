package cn.mopon.cec.core.access.ticket.mtx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

import cn.mopon.cec.core.access.ticket.converter.XsltUtils;
import coo.base.constants.Encoding;
import coo.base.exception.UncheckedException;
import coo.base.util.CryptoUtils;

/**
 * 满天星服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
public class MtxServiceQuery<T extends MtxQuery> {
	private T query;

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 */
	public MtxServiceQuery(T query) {
		this.query = query;
	}

	/**
	 * 构造方法。
	 * 
	 * @param query
	 *            请求对象
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public MtxServiceQuery(T query, String username, String password) {
		this.query = query;
		this.query.appCode = username;
		this.query.checkKey = password;
		this.query.setTokenId(this.query.tokenId);
		this.query.setVerifyInfo(getVerifyInfo());
	}

	/**
	 * 获取接口访问的验证信息。
	 * 
	 * @return 返回验证信息字符串。
	 */
	private String getVerifyInfo() {
		try {
			return CryptoUtils.md5(
					URLEncoder.encode(
							this.query.appCode + this.query.param
									+ this.query.checkKey, "UTF-8")
							.toLowerCase()).substring(8, 24);
		} catch (UnsupportedEncodingException e) {
			throw new UncheckedException("生成满天星接口检验信息发生异常。", e);
		}
	}

	/**
	 * 生成请求消息。
	 * 
	 * @return 返回请求消息。
	 * @throws Exception
	 *             抛出异常。
	 */
	public String genQuery() throws Exception {
		SOAPMessage soapMessage = MessageFactory.newInstance(
				SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
		SOAPEnvelope soapEnv = soapMessage.getSOAPPart().getEnvelope();
		soapEnv.removeNamespaceDeclaration(soapEnv.getPrefix());
		soapEnv.addNamespaceDeclaration("xsd",
				"http://www.w3.org/2001/XMLSchema");
		soapEnv.addNamespaceDeclaration("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		soapEnv.setPrefix("soapenv");
		soapEnv.getHeader().detachNode();
		soapEnv.getBody().setPrefix("soapenv");
		soapEnv.getBody().addDocument(genDocument());
		SOAPElement rootNode = (SOAPElement) soapEnv.getBody().getFirstChild();
		rootNode.addNamespaceDeclaration("ns1",
				"http://webservice.main.cmts.cn");
		rootNode.setPrefix("ns1");
		try (ByteArrayOutputStream msgOut = new ByteArrayOutputStream();) {
			soapMessage.writeTo(msgOut);
			return XsltUtils.cleanXml(msgOut.toString(Encoding.UTF_8));
		}
	}

	/**
	 * 生成请求的XML文档对象。
	 * 
	 * @return 返回请求的XML文档对象。
	 * @throws Exception
	 *             抛出异常。
	 */
	private Document genDocument() throws Exception {
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