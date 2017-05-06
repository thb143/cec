package cn.mopon.cec.api.actions.api.ticket;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.mopon.cec.api.ApiException;
import cn.mopon.cec.api.ApiReplyCode;
import cn.mopon.cec.api.ticket.TicketFacade;
import cn.mopon.cec.api.ticket.TicketReplyCode;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Cinema;
import coo.base.util.StringUtils;

/**
 * 拦截器。
 */
public class TicketApiInterceptor extends HandlerInterceptorAdapter {
	@Resource
	private TicketFacade ticketFacade;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 检查渠道是否具有访问接口的权限。
		String channelCode = request.getParameter("channelCode");
		Channel channel = ticketFacade.queryChannel(channelCode);
		checkTicketApiMethodAuth((HandlerMethod) handler, channel);
		// 如果请求参数中包含影院编码，检查渠道是否开放该影院。
		String cinemaCode = request.getParameter("cinemaCode");
		if (StringUtils.isNotBlank(cinemaCode)) {
			Cinema cinema = ticketFacade.queryCinema(cinemaCode);
			if (!channel.getOpenedCinemas().contains(cinema)) {
				ApiException.thrown(TicketReplyCode.CINEMA_NOT_OPENED);
			}
		}
		return true;
	}

	/**
	 * 检查渠道是否具有访问接口的权限。
	 * 
	 * @param method
	 *            接口方法
	 * @param channel
	 *            渠道
	 */
	private void checkTicketApiMethodAuth(HandlerMethod method, Channel channel) {
		TikcetApiMethodAuth auth = method
				.getMethodAnnotation(TikcetApiMethodAuth.class);
		if (!channel.getSettings().getTicketApiMethods().contains(auth.value())) {
			ApiException.thrown(ApiReplyCode.ACCESS_DENIDE);
		}
	}
}