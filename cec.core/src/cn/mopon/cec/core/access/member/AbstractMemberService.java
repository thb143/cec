package cn.mopon.cec.core.access.member;

import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.entity.MemberAccessType;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.enums.Provider;

/**
 * 票务接入接口抽象基类。
 */
public abstract class AbstractMemberService implements MemberAccessService {
	/** 日志对象 */
	protected Logger log = LoggerFactory.getLogger(getClass());
	/** 选座票查询设置对象 */
	protected MemberSettings settings;

	/**
	 * 构造方法。
	 * 
	 * @param settings
	 *            选座票接入设置对象
	 */
	public AbstractMemberService(MemberSettings settings) {
		this.settings = settings;
	}

	/**
	 * 获取请求配置。
	 * 
	 * @return 返回设置的请求配置。
	 */
	protected RequestConfig getRequestConfig() {
		MemberAccessType acceptType = settings.getAccessType();
		return RequestConfig.custom()
				.setSocketTimeout(acceptType.getSocketTimeout() * 1000)
				.setConnectTimeout(acceptType.getConnectTimeout() * 1000)
				.build();
	}

	/**
	 * 记录接口调用日志。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param replyString
	 *            响应消息
	 * @param duration
	 *            耗时
	 */
	protected void logReply(String url, Object params, String replyString,
			long duration) {
		String msg = replyString;
		Integer logLength = settings.getLogLength();
		if (logLength > 0 && logLength < replyString.length()) {
			msg = replyString.substring(0, logLength) + "...省略";
		}
		msg = String.format("%s[调用成功][耗时：%s]%n[请求地址：%s]%n[请求参数：%s]%n[响应消息：%s]",
				getLogHeader(), duration, url, params, msg);
		log.info(msg);
	}

	/**
	 * 记录接口异常日志。
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param ex
	 *            异常
	 */
	protected void logError(String url, Object params, Throwable ex) {
		String msg = String.format("%s[调用失败]%n[请求地址：%s]%n[请求参数：%s]",
				getLogHeader(), url, params);
		log.error(msg, ex);
	}

	/**
	 * 获取响应消息的日志记录内容。根据选座设置中的日志长度进行截取。
	 * 
	 * @param replyString
	 *            响应消息
	 * @return 返回响应消息的日志记录内容。
	 */
	protected String getReplyLogString(String replyString) {
		Integer logLength = settings.getLogLength();
		if (logLength > 0 && logLength < replyString.length()) {
			return replyString.substring(0, logLength) + "...省略";
		}
		return replyString;
	}

	/**
	 * 获取日志头部。
	 * 
	 * @return 返回日志头部。
	 */
	protected String getLogHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(Provider.NG);
		builder.append("-");
		builder.append(settings.getCinema().getCode());
		builder.append("-");
		builder.append(settings.getCinema().getName());
		builder.append("]");
		return builder.toString();
	}

}