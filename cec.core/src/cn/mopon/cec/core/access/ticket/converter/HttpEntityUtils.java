package cn.mopon.cec.core.access.ticket.converter;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import coo.base.constants.Encoding;

/**
 * HttpEntity工具类。
 */
public class HttpEntityUtils {
	/**
	 * 从HttpEntity对象获取内容字符串。
	 * 
	 * @param httpEntity
	 *            HttpEntity对象
	 * @return 返回内容字符串。
	 * @throws Exception
	 *             获取内容失败时抛出异常。
	 */
	public static String getString(HttpEntity httpEntity) throws Exception {
		return XsltUtils
				.cleanXml(EntityUtils.toString(httpEntity, Encoding.UTF_8));
	}
}