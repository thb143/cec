package cn.mopon.cec.core.access.member.dx;

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
import cn.mopon.cec.core.access.ticket.dx.enums.DXReplyStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.exception.BusinessException;
import coo.core.jackson.GenericObjectMapper;

/**
 * 鼎新服务响应对象。
 * 
 * @param <T>
 *            服务响应类型
 */
public class DXServiceReply<T extends DXReply> {
	private Logger log = LoggerFactory.getLogger(getClass());
	private T reply;

	/**
	 * 构造方法。
	 * 
	 * @param reply
	 *            响应对象。
	 */
	public DXServiceReply(T reply) {
		this.reply = reply;
		reply.getXstream().alias("root", DXServiceReply.class);
	}

	/**
	 * 生成响应对象。
	 * 
	 * @param strXml
	 *            返回的xml内容
	 * @throws Exception
	 *             异常信息。
	 */
	public void genReplyMessageByXml(String strXml) throws Exception {
		long startTime = System.currentTimeMillis();
		InputStream xsltInputStream = DXServiceReply.class
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
		reply.getXstream().fromXML(strXml, reply);
		if (reply.getReplyStatus() == DXReplyStatus.FAIL
				|| "306700".equals(reply.getErrorCode())) {
			if ("510573".equals(reply.getErrorCode())) {
				throw new BusinessException(
						MemberReplyError.MEMBER_LACK_BALANCE.getValue());
			}
			log.info("调用接口发生异常：[{}]", reply.getErrorMessage());
			throw new BusinessException(reply.getErrorMessage());
		}
		long endTime = System.currentTimeMillis();
		log.info("解析响应消息[耗时:{}][内容：{}]", endTime - startTime, strXml.toString());
	}

	/**
	 * 生成响应对象。
	 * 
	 * @param json
	 *            返回的json内容
	 * @throws Exception
	 *             异常信息。
	 */
	@SuppressWarnings("unchecked")
	public void genReplyMessageByJson(String json) throws Exception {
		long startTime = System.currentTimeMillis();
		ObjectMapper mapper = new GenericObjectMapper();
		reply = (T) mapper.readValue(json, reply.getClass());
		long endTime = System.currentTimeMillis();
		log.info("解析响应消息[耗时:{}][内容：{}]", endTime - startTime, json.toString());
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}

}
