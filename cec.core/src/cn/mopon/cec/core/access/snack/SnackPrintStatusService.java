package cn.mopon.cec.core.access.snack;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.mopon.cec.core.access.ticket.converter.HttpEntityUtils;
import cn.mopon.cec.core.entity.SnackOrder;
import coo.base.exception.UncheckedException;
import coo.base.util.StringUtils;

/**
 * 查看卖品打印状态组件。
 */
@Service
public class SnackPrintStatusService {
	/** 接口访问地址 */
	@Value("${push.url:http://172.16.34.3:8664/Interface/SCECInterface.ashx}")
	private String url;
	/** 交易ID */
	private String tradeId = "IsPrintSellPro";

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 查询卖品打印状态。
	 * 
	 * @param order
	 *            卖品订单
	 * @return 未打印，返回true，否则返回false。
	 */
	public Boolean querySnackPrintStatus(SnackOrder order) {
		try {
			SnackPrintStatusQuery query = new SnackPrintStatusQuery(order
					.getChannel().getCode(), order.getChannel().getSecKey(),
					tradeId, order);
			SnackPrintStatusReply reply = execute(query);
			// 操作成功，且为未打印时，返回true
			if ("0".equals(reply.getHead().getErrCode())
					&& "0".equals(reply.getBody().getErrCode())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			log.error("卖品打印状态查询接口异常。", e);
			return false;
		}
	}

	/**
	 * 调用服务。
	 * 
	 * @param query
	 *            服务请求对象
	 * @return 返回服务响应对象。
	 * @throws Exception
	 *             抛出异常。
	 */
	private SnackPrintStatusReply execute(SnackPrintStatusQuery query)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(getRequestConfig());
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String param = query.convertXml(query);
		try {
			nvps.add(new BasicNameValuePair("param", param));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			long startTime = System.currentTimeMillis();
			HttpEntity replyEntity = httpclient.execute(httpPost).getEntity();
			long endTime = System.currentTimeMillis();
			String replayString = HttpEntityUtils.getString(replyEntity);
			logReply(url, cleanXml(param), replayString, endTime - startTime);
			return genReply(replayString);
		} catch (Exception e) {
			logError(url, cleanXml(param), e);
			throw new UncheckedException("卖品打印状态查询接口异常。", e);
		}
	}

	/**
	 * 生成响应对象。
	 * 
	 * @param replyXml
	 *            响应XML
	 * @return 返回响应对象。
	 */
	private SnackPrintStatusReply genReply(String replyXml) {
		try {
			SnackPrintStatusReply reply = new SnackPrintStatusReply();
			reply.getXstream().ignoreUnknownElements();
			reply.getXstream().fromXML(replyXml, reply);
			return reply;
		} catch (Exception e) {
			throw new UncheckedException("解析xml异常。", e);
		}
	}

	/**
	 * 获取请求配置。
	 * 
	 * @return 返回设置的请求配置。
	 */
	private RequestConfig getRequestConfig() {
		return RequestConfig.custom().setSocketTimeout(30000)
				.setConnectTimeout(30000).build();
	}

	/**
	 * 清理XML字符串格式。
	 * 
	 * @param xml
	 *            XML字符串
	 * @return 返回清理后的XML字符串。
	 */
	private String cleanXml(String xml) {
		return StringUtils.isEmpty(xml) ? "" : xml.replaceAll("\n|\r|\t| ", "");
	}

	/**
	 * 记录接口调用日志。
	 * 
	 * @param url
	 *            请求地址
	 * @param queryString
	 *            请求参数
	 * @param replyString
	 *            响应消息
	 * @param duration
	 *            耗时
	 */
	private void logReply(String url, String queryString, String replyString,
			long duration) {
		String msg = String.format(
				"卖品打印状态查询接口[调用成功][耗时：%s]%n[请求地址：%s]%n[请求参数：%s]%n[响应消息：%s]",
				duration, url, queryString, replyString);
		log.info(msg);
	}

	/**
	 * 记录接口异常日志。
	 * 
	 * @param url
	 *            请求地址
	 * @param queryString
	 *            请求参数
	 * @param ex
	 *            异常
	 */
	private void logError(String url, String queryString, Throwable ex) {
		String msg = String.format("卖品打印状态查询接口[调用失败]%n[请求地址：%s]%n[请求参数：%s]",
				url, queryString);
		log.error(msg, ex);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
}