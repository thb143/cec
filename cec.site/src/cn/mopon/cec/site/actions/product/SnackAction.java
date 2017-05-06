package cn.mopon.cec.site.actions.product;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.entity.SnackType;
import cn.mopon.cec.core.enums.SnackStatus;
import cn.mopon.cec.core.model.SnackModel;
import cn.mopon.cec.core.service.ChannelService;
import cn.mopon.cec.core.service.CinemaService;
import cn.mopon.cec.core.service.SnackService;
import cn.mopon.cec.core.service.SnackTypeService;
import coo.base.util.CollectionUtils;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.Auth;
import coo.mvc.util.DialogResultUtils;
import coo.mvc.util.NavTabResultUtils;

/**
 * 卖品管理。
 */
@Controller
@RequestMapping("/product")
public class SnackAction {
	@Resource
	private SnackService snackService;
	@Resource
	private SnackTypeService snackTypeService;
	@Resource
	private CinemaService cinemaService;
	@Resource
	private ChannelService channelService;
	@Resource
	private MessageSource messageSource;
	@Value(value = "${content.server.url}")
	private String url;

	/**
	 * 查看卖品管理主页面。
	 * 
	 * @param model
	 *            数据模型
	 * @param searchModel
	 *            搜索条件
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("snack-main")
	public void main(Model model, SearchModel searchModel) {
		model.addAttribute("searchModel", searchModel);
		model.addAttribute("cinemaPage",
				cinemaService.searchCinema(searchModel));
	}

	/**
	 * 查看卖品列表。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinema
	 *            影院
	 */
	@Auth("PRODUCT_VIEW")
	@RequestMapping("snack-list")
	public void list(Model model, Cinema cinema) {
		url = url.endsWith("/") ? url : url + "/";
		List<Snack> snacks = snackService.getSnacks(cinema);
		List<SnackType> snackTypes = snackTypeService
				.searchSnackTypes(new SearchModel());
		model.addAttribute("snackTypes", filterSnackTypes(snackTypes, snacks));
		model.addAttribute("imagePath", url);
		model.addAttribute("cinema", cinema);
		model.addAttribute("snacks", getSnackModels(snacks, cinema));
	}

	/**
	 * 构建卖器模型列表。
	 * 
	 * @param snacks
	 *            卖品列表
	 * @param cinema
	 *            影院
	 * @return 返回卖器模型列表。
	 */
	private List<SnackModel> getSnackModels(List<Snack> snacks, Cinema cinema) {
		List<SnackModel> snackModels = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(snacks)) {
			List<Channel> channels = channelService.getAllChannels();
			List<String> channelIds = new ArrayList<>();
			for (Channel channel : channels) {
				if (channel.getOpenedCinemas().contains(cinema)) {
					channelIds.add(channel.getId());
				}
			}
			for (Snack snack : snacks) {
				SnackModel snackModel = new SnackModel(snack);
				snackModel.setIsAddChannel(isAddChannel(snack, channelIds));
				snackModels.add(snackModel);
			}
		}
		return snackModels;
	}

	/**
	 * 判断是否允许新增渠道。
	 * 
	 * @param snack
	 *            卖品
	 * @param channelIds
	 *            渠道ID列表
	 * @return 返回是否允许新增渠道。
	 */
	public Boolean isAddChannel(Snack snack, List<String> channelIds) {
		List<String> snackChannelIds = new ArrayList<>();
		for (SnackChannel channel : snack.getSnackChannels()) {
			snackChannelIds.add(channel.getChannel().getId());
		}
		return snackChannelIds.equals(channelIds) ? false : !snackChannelIds
				.containsAll(channelIds);
	}

	/**
	 * 新增卖品。
	 * 
	 * @param model
	 *            数据模型
	 * @param cinema
	 *            影院
	 * @param snackType
	 *            卖品类型
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snack-add")
	public void add(Model model, Cinema cinema, SearchModel searchModel) {
		List<SnackType> snackTypes = snackTypeService
				.searchSnackTypes(searchModel);
		List<Snack> snacks = snackService.getSnacks(cinema);
		model.addAttribute("cinema", cinema);
		model.addAttribute("snackTypes", filterSnackTypes(snackTypes, snacks));
	}

	/**
	 * 过滤影院已选的卖品类型。
	 * 
	 * @param snackTypes
	 *            卖品类型列表
	 * @param snacks
	 *            影院已有的卖品列表
	 * @return 返回过滤后的卖品类型列表。
	 */
	private List<SnackType> filterSnackTypes(List<SnackType> snackTypes,
			List<Snack> snacks) {
		if (CollectionUtils.isNotEmpty(snacks)) {
			List<SnackType> resultList = new ArrayList<>();
			resultList.addAll(snackTypes);
			for (SnackType type : snackTypes) {
				for (Snack snack : snacks) {
					if (snack.getType().equals(type)) {
						resultList.remove(type);
						break;
					}
				}
			}
			return resultList;
		} else {
			return snackTypes;
		}
	}

	/**
	 * 保存卖品。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @param snackTypeIds
	 *            选择的卖品类型ID数组
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snack-save")
	public ModelAndView save(String cinemaId, String[] snackTypeIds) {
		snackService.createSnack(cinemaId, snackTypeIds);
		return DialogResultUtils.closeAndReloadDiv(
				messageSource.get("snack.add.success"), "snackListBox");
	}

	/**
	 * 编辑卖品。
	 * 
	 * @param model
	 *            数据模型
	 * @param snack
	 *            卖品
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snack-edit")
	public void edit(Model model, Snack snack) {
		model.addAttribute(snackService.getSnack(snack.getId()));
	}

	/**
	 * 更新卖品。
	 * 
	 * @param snack
	 *            卖品
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snack-update")
	public ModelAndView update(Snack snack) {
		String message = messageSource.get("snack.edit.success");
		if (snack.getStatus() == SnackStatus.UNVALID) {
			message = messageSource.get("snack.set.success");
		}
		snackService.updateSnack(snack);
		return DialogResultUtils.closeAndReloadDiv(message, "snackListBox");
	}

	/**
	 * 删除卖品。
	 * 
	 * @param snack
	 *            卖品
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_MANAGE")
	@RequestMapping("snack-delete")
	public ModelAndView delete(Snack snack) {
		snackService.deleteSnack(snack);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("snack.delete.success"), "snackListBox");
	}

	/**
	 * 上架卖品。
	 * 
	 * @param snack
	 *            卖品
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("snack-enable")
	public ModelAndView enable(Snack snack) {
		snackService.enableSnack(snack);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("snack.enable.success"), "snackListBox");
	}

	/**
	 * 下架卖品。
	 * 
	 * @param snack
	 *            卖品
	 * @return 返回提示信息。
	 */
	@Auth("PRODUCT_SWITCH")
	@RequestMapping("snack-disable")
	public ModelAndView disable(Snack snack) {
		snackService.disableSnack(snack);
		return NavTabResultUtils.reloadDiv(
				messageSource.get("snack.disable.success"), "snackListBox");
	}
}