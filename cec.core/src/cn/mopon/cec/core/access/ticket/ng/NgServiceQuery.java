package cn.mopon.cec.core.access.ticket.ng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.base.constants.Encoding;
import coo.base.util.CryptoUtils;
import coo.core.xstream.DateConverter;

/**
 * NG服务请求对象。
 * 
 * @param <T>
 *            服务请求类型
 */
@XStreamAlias("OnlineTicketingServiceQuery")
public class NgServiceQuery<T extends NgQuery> {
	/** 版本号 */
	@XStreamAsAttribute
	@XStreamAlias("Version")
	private String version = "1.0";
	/** 请求时间 */
	@XStreamAsAttribute
	@XStreamAlias("Datetime")
	@XStreamConverter(value = DateConverter.class, strings = { "yyyy-MM-dd'T'HH:mm:ss" })
	private Date datetime = new Date();
	/** 用户名 */
	@XStreamAsAttribute
	@XStreamAlias("Username")
	private String username;
	/** 密码 */
	@XStreamAsAttribute
	@XStreamAlias("Password")
	private String password;
	private T query;

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
	public NgServiceQuery(T query, String username, String password) {
		this.query = query;
		this.username = username;
		this.password = CryptoUtils.md5(password);
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
				SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
		SOAPEnvelope soapEnv = soapMessage.getSOAPPart().getEnvelope();
		soapEnv.removeNamespaceDeclaration(soapEnv.getPrefix());
		soapEnv.setPrefix("env");
		soapEnv.getHeader().detachNode();
		soapEnv.getBody().setPrefix("env");

		Name queryNode = soapEnv.createName("query", "m",
				"http://www.crifst.ac.cn/2013/OTSAPI");
		SOAPElement queryElement = soapEnv.getBody().addBodyElement(queryNode);
		QName msgNode = queryElement.createQName("msg", "m");
		SOAPElement msgElement = queryElement.addChildElement(msgNode);
		msgElement.addTextNode(genMsgText());

		try (ByteArrayOutputStream msgOut = new ByteArrayOutputStream();) {
			soapMessage.writeTo(msgOut);
			return msgOut.toString(Encoding.UTF_8);
		}
	}

	/**
	 * 生成消息文本。
	 * 
	 * @return 返回消息文本。
	 * @throws Exception
	 *             抛出异常。
	 */
	private String genMsgText() throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(genDocument()), new StreamResult(
				writer));
		String queryMessage = writer.getBuffer().toString()
				.replaceAll("\n|\r", "");
		writer.close();
		return queryMessage;
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
				.getXstream().toXML(this).getBytes(Encoding.UTF_8));
		Document document = documentBuilder.parse(xmlInputStream);
		return sigDocument(document);
	}

	/**
	 * 签名请求的文档对象。
	 * 
	 * @param document
	 *            请求的文档对象
	 * @return 返回签名后的请求文档对象。
	 * @throws Exception
	 *             抛出异常。
	 */
	private Document sigDocument(Document document) throws Exception {
		DOMSignContext sigContext = new DOMSignContext(
				KeyPairProvider.getPrivateKey(), document.getDocumentElement());
		sigContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");
		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance();
		Reference ref = sigFactory.newReference("", sigFactory.newDigestMethod(
				DigestMethod.SHA1, null), Collections.singletonList(sigFactory
				.newTransform(Transform.ENVELOPED,
						(TransformParameterSpec) null)), null, query.getId());

		SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory
				.newCanonicalizationMethod(
						CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
						(C14NMethodParameterSpec) null), sigFactory
				.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
				Collections.singletonList(ref));
		XMLSignature sig = sigFactory.newXMLSignature(signedInfo, null);
		sig.sign(sigContext);
		return document;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
	}
}