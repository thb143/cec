package cn.mopon.cec.site.actions.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.service.TicketVoucherService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;

/**
 * 选座票凭证管理。
 */
@Controller
@RequestMapping("/order")
public class TicketVoucherAction {
	@Resource
	private TicketVoucherService ticketVoucherService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 重置凭证。
	 * 
	 * @param voucherId
	 *            选座票凭证ID
	 * @return 返回提示信息。
	 */
	@Auth("ORDER_MANAGE")
	@RequestMapping("ticketVoucher-reset")
	public ModelAndView reset(String voucherId) {
		ticketVoucherService.resetTicketVoucher(ticketVoucherService
				.getTicketVoucher(voucherId));
		return DialogResultUtils.reload(messageSource
				.get("ticketVoucher.reset.success"));
	}
}