package cn.mopon.cec.site.actions.operate;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.MemberAccessType;
import cn.mopon.cec.core.entity.TicketAccessType;
import cn.mopon.cec.core.service.MemberAccessTypeService;
import cn.mopon.cec.core.service.TicketAccessTypeService;
import coo.core.message.MessageSource;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 接入类型管理。
 */
@Controller
@RequestMapping("/operate")
@Auth("CIRCUITSETTINGS_MANAGE")
public class AccessTypeAction {
	@Resource
	private TicketAccessTypeService ticketAccessTypeService;
	@Resource
	private MemberAccessTypeService memberAccessTypeService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 查看接入类型列表。
	 * 
	 * @param selectedId
	 *            选择的接入类型
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("accessType-list")
	public void list(String selectedId, Model model) {
		model.addAttribute("selectedId", selectedId);
		model.addAttribute("ticketAccessTypes",
				ticketAccessTypeService.getTicketAccessTypeList(null));
		model.addAttribute("memberAccessTypes",
				memberAccessTypeService.getMemberAccessTypeList(null));
	}

	/**
	 * 弹出新增选座票接入类型页面。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("ticketAccessType-add")
	public void addTicketAccessType(Model model) {
		model.addAttribute("ticketAccessType", new TicketAccessType());
	}

	/**
	 * 保存选座票接入类型。
	 * 
	 * @param ticketAccessType
	 *            选座票接入类型
	 * @return 返回提示信息。
	 */
	@RequestMapping("ticketAccessType-save")
	public ModelAndView saveTicketAccessType(TicketAccessType ticketAccessType) {
		ticketAccessTypeService.createTicketAccessType(ticketAccessType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("ticketAccessType.add.success"),
				"/operate/accessType-list?selectedId="
						+ ticketAccessType.getId());
	}

	/**
	 * 编辑选座票接入类型。
	 * 
	 * @param model
	 *            数据模型
	 * @param id
	 *            选座票接入类型ID
	 */
	@RequestMapping("ticketAccessType-edit")
	public void editTicketAccessType(Model model, String id) {
		model.addAttribute("ticketAccessType",
				ticketAccessTypeService.getTicketAccessType(id));
	}

	/**
	 * 修改选座票接入类型。
	 * 
	 * @param ticketAccessType
	 *            选座票接入类型
	 * @return 返回提示信息。
	 */
	@RequestMapping("ticketAccessType-update")
	public ModelAndView updateTicketAccessType(TicketAccessType ticketAccessType) {
		ticketAccessTypeService.updateTicketAccessType(ticketAccessType);
		return NavTabResultUtils.forward(
				messageSource.get("ticketAccessType.edit.success"),
				"/operate/accessType-list?selectedId="
						+ ticketAccessType.getId());
	}

	/**
	 * 弹出新增会员接入类型页面。
	 * 
	 * @param model
	 *            数据模型
	 */
	@RequestMapping("memberAccessType-add")
	public void addMemberAccessType(Model model) {
		model.addAttribute("memberAccessType", new MemberAccessType());
	}

	/**
	 * 保存会员接入类型。
	 * 
	 * @param memberAccessType
	 *            选座票接入类型
	 * @return 返回提示信息。
	 */
	@RequestMapping("memberAccessType-save")
	public ModelAndView saveMemberAccessType(MemberAccessType memberAccessType) {
		memberAccessTypeService.createMemberAccessType(memberAccessType);
		return DialogResultUtils.closeAndForwardNavTab(
				messageSource.get("memberAccessType.add.success"),
				"/operate/accessType-list?selectedId="
						+ memberAccessType.getId());
	}

	/**
	 * 编辑会员接入类型。
	 * 
	 * @param model
	 *            数据模型
	 * @param id
	 *            会员接入类型ID
	 */
	@RequestMapping("memberAccessType-edit")
	public void editMemberAccessType(Model model, String id) {
		model.addAttribute("memberAccessType",
				memberAccessTypeService.getMemberAccessType(id));
	}

	/**
	 * 编辑会员接入类型。
	 * 
	 * @param memberAccessType
	 *            会员接入类型
	 * @return 返回提示信息。
	 */
	@RequestMapping("memberAccessType-update")
	public ModelAndView updateMemberAccessType(MemberAccessType memberAccessType) {
		memberAccessTypeService.updateMemberAccessType(memberAccessType);
		return NavTabResultUtils.forward(
				messageSource.get("memberAccessType.edit.success"),
				"/operate/accessType-list?selectedId="
						+ memberAccessType.getId());
	}
}
