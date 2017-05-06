package cn.mopon.cec.api.actions.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ApiReplyCode;
import cn.mopon.cec.api.ticket.TicketFacade;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.enums.VerifySign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.constants.Encoding;
import coo.base.util.CryptoUtils;
import coo.base.util.StringUtils;

/**
 * 对外接口请求拦截器。
 */
public class ApiQueryInterceptor extends HandlerInterceptorAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ObjectMapper mapper = new ObjectMapper();
	@Resource
	private TicketFacade ticketFacade;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String channelCode = request.getParameter("channelCode");
		Channel channel = ticketFacade.queryChannel(channelCode);
		if (channel.getSettings().getVerifySign() == VerifySign.VERIFY) {
			checkSign(request, channel);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		ModelMap modelMap = modelAndView.getModelMap();
		ApiReply reply = getReply(modelMap);
		logReply(request, reply);
		modelMap.clear();
		modelMap.addAttribute(reply);
	}

	/**
	 * 获取响应对象。
	 * 
	 * @param modelMap
	 *            ModelMap
	 * @return 返回响应对象。
	 */
	private ApiReply getReply(ModelMap modelMap) {
		for (Object model : modelMap.values()) {
			if (model instanceof ApiReply) {
				return (ApiReply) model;
			}
		}
		return null;
	}

	/**
	 * 记录接口调用日志。
	 * 
	 * @param request
	 *            HTTP请求
	 * @param reply
	 *            响应对象
	 */
	protected void logReply(HttpServletRequest request, ApiReply reply) {
		String channelCode = request.getParameter("channelCode");
		Channel channel = ticketFacade.queryChannel(channelCode);
		String params;
		try {
			params = mapper.writeValueAsString(request.getParameterMap());
		} catch (JsonProcessingException e) {
			params = e.getMessage();
		}
		String msg = String.format("[%s][请求地址：%s][调用成功]%n[请求参数：%s]%n[响应消息：%s]",
				channelCode, request.getRequestURI(), params,
				getReplyLogString(channel, reply));
		log.info(msg);
	}

	/**
	 * 获取响应消息的日志记录内容。根据渠道设置中的日志长度进行截取。
	 * 
	 * @param channel
	 *            渠道
	 * @param reply
	 *            响应消息
	 * @return 返回响应消息的日志记录内容。
	 */
	private String getReplyLogString(Channel channel, ApiReply reply) {
		try {
			String replyString = mapper.writeValueAsString(reply);
			Integer logLength = channel.getSettings().getLogLength();
			if (logLength > 0 && logLength < replyString.length()) {
				return replyString.substring(0, logLength) + "...省略";
			}
			return replyString;
		} catch (JsonProcessingException e) {
			return e.getMessage();
		}
	}

	/**
	 * 验证签名。
	 * 
	 * @param request
	 *            请求对象
	 * @param channel
	 *            渠道
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码时抛出异常
	 */
	private void checkSign(HttpServletRequest request, Channel channel)
			throws UnsupportedEncodingException {
		String origSign = request.getParameter("sign");
		String paramSignStr = genParamSignStr(request);
		String secKey = channel.getSecKey();
		String targetSign = CryptoUtils.md5(secKey + paramSignStr + secKey);
		if (!origSign.equals(targetSign)) {
			ApiException.thrown(ApiReplyCode.SIGN_VERIFY_FAILED);
		}
	}

	/**
	 * 生成请求参数拼接的待签名字符串。
	 * 
	 * @param request
	 *            请求对象
	 * @return 返回请求参数拼接的待签名字符串。
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码时抛出异常
	 */
	@SuppressWarnings("unchecked")
	private String genParamSignStr(HttpServletRequest request)
			throws UnsupportedEncodingException {
		Map<String, String> paramsMap = new TreeMap<>();
		Enumeration<String> paramNamesIterator = request.getParameterNames();
		while (paramNamesIterator.hasMoreElements()) {
			String paramName = paramNamesIterator.nextElement();
			paramsMap.put(paramName, request.getParameter(paramName));
		}
		paramsMap.remove("sign");
		List<String> paramPairs = new ArrayList<String>();
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			paramPairs.add(entry.getKey() + "=" + entry.getValue());
		}
		String paramSignStr = StringUtils.join(paramPairs, "&");
		return URLEncoder.encode(paramSignStr, Encoding.UTF_8);
	}
}