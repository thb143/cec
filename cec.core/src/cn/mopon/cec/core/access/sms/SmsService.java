package cn.mopon.cec.core.access.sms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import coo.base.constants.Encoding;
import coo.base.exception.BusinessException;
import coo.base.exception.UncheckedException;
import coo.base.util.CryptoUtils;
import coo.base.util.DateUtils;
import coo.base.util.FileUtils;

/**
 * 短信发送服务类。
 */
@Service
public class SmsService {
	private Logger log = LoggerFactory.getLogger(getClass());
	/** 短信服务地址 */
	@Value("${sms.url}")
	private String url;
	/** 客户端编码 */
	@Value("${sms.clientCode}")
	private String clientCode;
	/** 加密秘钥 */
	@Value("${sms.secKey}")
	private String secKey;

	/**
	 * 发送短信。
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            短信内容
	 */
	public void sendSMS(String mobile, String content) {
		SmsQuery query = new SmsQuery();
		query.setClientCode(clientCode);
		query.setContent(content);
		query.setMobileNo(mobile);
		query.setTimeStamp(DateUtils.format(new Date(), DateUtils.SECOND_N));
		query.setSecretMsg(query.genSecretMsg(secKey));
		execute(query);
	}

	/**
	 * 发送彩信。
	 * 
	 * @param mobile
	 *            手机号码
	 * @param content
	 *            彩信内容
	 * @param title
	 *            标题
	 * @param imgFilePath
	 *            图片地址
	 */
	public void sendMMS(String mobile, String content, String title,
			String imgFilePath) {
		MmsQuery query = new MmsQuery();
		query.setClientCode(clientCode);
		query.setMobileNo(mobile);
		query.setTitle(title);
		query.setContent(content);
		query.setPicEncoder(genImgEncode(imgFilePath));
		query.setSuffixName(imgFilePath.substring(imgFilePath.lastIndexOf(".") + 1));
		query.setTimeStamp(DateUtils.format(new Date(), DateUtils.SECOND_N));
		query.setSecretMsg(query.genSecretMsg(secKey));
		execute(query);
	}

	/**
	 * 调用短信网关。
	 * 
	 * @param query
	 *            查询对象
	 */
	private void execute(BaseQuery query) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			HttpEntity queryEntity = genQueryEntity(query);
			httpPost.setEntity(queryEntity);
			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpClient.execute(httpPost).getEntity();
			long endTime = System.currentTimeMillis();
			log.info("短信接口调用[耗时:{}]", endTime - startTime);
			parseReplyEntity(replyEntity);
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new UncheckedException("调用短信接口发生异常。", e);
		}
	}

	/**
	 * 生成http请求对象。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回http请求对象。
	 */
	private HttpEntity genQueryEntity(BaseQuery query) {
		try {
			SOAPMessage soapMessage = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
			SOAPEnvelope soapEnv = soapMessage.getSOAPPart().getEnvelope();
			soapEnv.removeNamespaceDeclaration(soapEnv.getPrefix());
			soapEnv.setPrefix("soapenv");
			soapEnv.getHeader().detachNode();
			soapEnv.getBody().setPrefix("soapenv");
			soapEnv.getBody().addDocument(genQueryDocument(query));
			SOAPElement bodyElement = (SOAPElement) soapEnv.getBody()
					.getChildElements().next();
			bodyElement.setPrefix("nsl");
			bodyElement.addNamespaceDeclaration("nsl",
					"http://webservice.mts.mopon.com");
			long startTime = System.currentTimeMillis();
			ByteArrayOutputStream msgOut = new ByteArrayOutputStream();
			soapMessage.writeTo(msgOut);
			HttpEntity queryEntity = new ByteArrayEntity(msgOut.toByteArray());
			long endTime = System.currentTimeMillis();
			log.info("生成请求消息[耗时:{}][内容：{}]", endTime - startTime,
					msgOut.toString());
			return queryEntity;
		} catch (Exception e) {
			throw new UncheckedException("生成短信请求消息时发生异常。", e);
		}
	}

	/**
	 * 解析返回对象。
	 * 
	 * @param replyEntity
	 *            http返回对象
	 */
	private void parseReplyEntity(HttpEntity replyEntity) {
		String returnCode = "1000";
		try {
			SOAPMessage replyMessage = MessageFactory.newInstance(
					SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(null,
					replyEntity.getContent());
			Node returnNode = replyMessage.getSOAPBody()
					.getElementsByTagName("ns:return").item(0);
			returnCode = returnNode.getFirstChild().getNodeValue();
		} catch (Exception e) {
			throw new BusinessException("解析短信响应消息时发生异常。", e);
		}
		if (!"1000".equals(returnCode)) {
			log.error("发送短信失败，错误状态码为[{}]", returnCode);
			throw new BusinessException("发送短信失败，错误状态码[" + returnCode + "]。");
		}
	}

	/**
	 * 生成请求xml的doc文档。
	 * 
	 * @param query
	 *            请求对象
	 * @return 返回请求的doc文档。
	 */
	private Document genQueryDocument(BaseQuery query) {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(
					query.getXstream().toXML(query).getBytes(Encoding.UTF_8));
			return documentBuilder.parse(xmlInputStream);
		} catch (Exception e) {
			throw new UncheckedException("转换成文档对象时发生异常。", e);
		}
	}

	/**
	 * 获取图片编码后的字符串。
	 * 
	 * @param imgFilePath
	 *            图片路径
	 * @return 返回图片编码后的字符串。
	 */
	private String genImgEncode(String imgFilePath) {
		File imgFile = new File(imgFilePath);
		byte[] imgBytes = FileUtils.toByteArray(imgFile);
		return new String(CryptoUtils.encodeBase64(imgBytes));
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
}