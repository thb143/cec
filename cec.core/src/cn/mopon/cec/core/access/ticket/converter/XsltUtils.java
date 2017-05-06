package cn.mopon.cec.core.access.ticket.converter;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * XSLT工具类。
 */
public class XsltUtils {
	/**
	 * 转换XML。
	 * 
	 * @param origXml
	 *            原XML
	 * @param serviceReplyClass
	 *            ServiceReply类，用于加载XSLT文件
	 * @param replyClass
	 *            Reply类，用于获取对应的XSLT文件名称
	 * @return 返回转换后的XML。
	 * @throws Exception
	 *             抛出异常。
	 */
	public static String transform(String origXml, Class<?> serviceReplyClass,
			Class<?> replyClass) throws Exception {
		String xsltPath = "xslt/" + replyClass.getSimpleName() + ".xslt";
		try (InputStream xslt = serviceReplyClass.getResourceAsStream(xsltPath);
				StringReader reader = new StringReader(origXml);
				StringWriter writer = new StringWriter();) {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer(new StreamSource(xslt));
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.transform(new StreamSource(reader), new StreamResult(
					writer));
			return cleanXml(writer.getBuffer().toString());
		}
	}

	/**
	 * 清理XML字符串格式。
	 * 
	 * @param xml
	 *            XML字符串
	 * @return 返回清理后的XML字符串。
	 */
	public static String cleanXml(String xml) {
		return xml.replaceAll("\n|\r|\t", "").replaceAll(">\\s*<", "><");
	}
}