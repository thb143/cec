package cn.mopon.cec.core.access.member.hfh;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.access.member.enums.MemberReplyError;
import coo.base.exception.BusinessException;

/**
 * 火凤凰(幸福蓝海)服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class HFHServiceReply<T extends HFHReply> {
	private Logger log = LoggerFactory.getLogger(getClass());
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象
	 */
	public HFHServiceReply(T reply) {
		this.reply = reply;
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
		InputStream xsltInputStream = HFHServiceReply.class
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
		if ("51501".equals(reply.getResult())
				|| "51699".equals(reply.getResult())) {
			log.info("调用接口发生异常：[{}]", reply.getMessage());
			throw new BusinessException(
					MemberReplyError.MEMBER_NOT_EXIST.getValue());
		} else if ("-20002".equals(reply.getResult())) {
			log.info("调用接口发生异常：[{}]", reply.getMessage());
			throw new BusinessException(
					MemberReplyError.MEMBER_LACK_BALANCE.getValue());
		} else if (!"0".equals(reply.getResult())) {
			log.info("调用接口发生异常：[{}]", reply.getMessages());
			throw new BusinessException(reply.getResult());
		}
		return strXml;
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}
}
