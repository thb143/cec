package cn.mopon.cec.core.access.ticket;

import cn.mopon.cec.core.access.ticket.dx.DxTicketService;
import cn.mopon.cec.core.access.ticket.hfh.HfhTicketService;
import cn.mopon.cec.core.access.ticket.mtx.MtxTicketService;
import cn.mopon.cec.core.access.ticket.ng.NgTicketService;
import cn.mopon.cec.core.access.ticket.ngc.NgcTicketService;
import cn.mopon.cec.core.access.ticket.std.StdTicketService;
import cn.mopon.cec.core.access.ticket.virtual.VirtualTicketService;
import cn.mopon.cec.core.access.ticket.yxt.YxtTicketService;
import cn.mopon.cec.core.entity.TicketSettings;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.exception.UncheckedException;
import coo.base.util.BeanUtils;

/**
 * 网络售票接口工厂。
 */
public class TicketAccessServiceFactory {
	/**
	 * 获取网络售票接口对象。
	 * 
	 * @param origSettings
	 *            影院选座票设置
	 * @return 返回选座票设置对应的网络售票接口对象。
	 */
	public static TicketAccessService getTicketService(
			TicketSettings origSettings) {
		TicketSettings settings = genSettings(origSettings);
		switch (settings.getAccessType().getAdapter()) {
		case STD:
			return new StdTicketService(settings);
		case HFH:
			return new HfhTicketService(settings);
		case MTX:
			return new MtxTicketService(settings);
		case DX:
			return new DxTicketService(settings);
		case TEST:
			return new VirtualTicketService(settings);
		case NGC:
			return new NgcTicketService(settings);
		case HLN:
			return new NgTicketService(settings);
		case DD:
			return new YxtTicketService(settings);
		case ZL:
			return new YxtTicketService(settings);
		case JY:
			return new YxtTicketService(settings);
		case WY:
			return new YxtTicketService(settings);
		default:
			throw new UncheckedException("不支持的接入类型["
					+ settings.getAccessType().getAdapter().getText() + "]。");
		}
	}

	/**
	 * 生成选座票设置。
	 * 
	 * @param origSettings
	 *            原选座票设置
	 * @return 返回生成的选座票设置。
	 */
	private static TicketSettings genSettings(TicketSettings origSettings) {
		TicketSettings settings = new TicketSettings();
		BeanUtils.copyFields(origSettings, settings);
		if (origSettings.getAccessType().getModel() == AccessModel.CENTER) {
			settings.setUrl(origSettings.getAccessType().getUrl());
			settings.setUsername(origSettings.getAccessType().getUsername());
			settings.setPassword(origSettings.getAccessType().getPassword());
		}
		return settings;
	}
}