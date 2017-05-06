package cn.mopon.cec.api.actions.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ApiReplyCode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.util.StringUtils;

/**
 * API Action基类。
 */
public abstract class ApiAction {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected ObjectMapper mapper = new ObjectMapper();

	/**
	 * 异常拦截处理。
	 * 
	 * @param request
	 *            HTTP请求
	 * @param ex
	 *            异常
	 * @return 返回异常视图。
	 */
	@ExceptionHandler(value = { ApiException.class, BindException.class,
			Exception.class })
	protected ApiReply checkedException(HttpServletRequest request, Exception ex) {
		ApiReply reply = new ApiReply(ApiReplyCode.FAILED);
		if (ex instanceof ApiException) {
			reply = new ApiReply(((ApiException) ex).getReplyCode());
		}
		if (ex instanceof BindException) {
			List<String> errorMsgs = new ArrayList<>();
			List<ObjectError> errors = ((BindException) ex).getBindingResult()
					.getAllErrors();
			for (ObjectError error : errors) {
				errorMsgs.add(error.getDefaultMessage());
			}
			reply = new ApiReply(ApiReplyCode.PARAMS_VALIDATE_FAILED);
			reply.setMsg(StringUtils.join(errorMsgs, "|"));
		}
		logError(request, reply, ex);
		return reply;
	}

	/**
	 * 记录接口异常日志。
	 * 
	 * @param request
	 *            HTTP请求
	 * @param reply
	 *            响应对象
	 * @param ex
	 *            异常
	 */
	private void logError(HttpServletRequest request, ApiReply reply,
			Exception ex) {
		String channelCode = request.getParameter("channelCode");
		String params;
		String replyMsg;
		try {
			params = mapper.writeValueAsString(request.getParameterMap());
			replyMsg = mapper.writeValueAsString(reply);
		} catch (JsonProcessingException e) {
			params = e.getMessage();
			replyMsg = e.getMessage();
		}
		String msg = String.format("[%s][请求地址：%s][调用失败]%n[请求参数：%s]%n[响应消息：%s]",
				channelCode, request.getRequestURI(), params, replyMsg);
		if (ex instanceof ApiException) {
			log.error(msg);
		} else {
			log.error(msg, ex);
		}
	}
}