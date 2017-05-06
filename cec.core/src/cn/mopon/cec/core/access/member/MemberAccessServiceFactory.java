package cn.mopon.cec.core.access.member;

import cn.mopon.cec.core.access.member.dx.DXMemberService;
import cn.mopon.cec.core.access.member.hfh.HFHMemberService;
import cn.mopon.cec.core.access.member.mtx.MTXMemberService;
import cn.mopon.cec.core.access.member.ng.NgMemberService;
import cn.mopon.cec.core.entity.MemberSettings;
import cn.mopon.cec.core.enums.AccessModel;
import coo.base.exception.UncheckedException;
import coo.base.util.BeanUtils;

/**
 * 会员卡接口工厂。
 */
public class MemberAccessServiceFactory {

	/**
	 * 获取会员卡接口对象。
	 * 
	 * @param settings
	 *            会员卡设置
	 * @return 返回会员卡设置对应的会员卡接口对象。
	 */
	public static MemberAccessService getMemberService(MemberSettings settings) {
		MemberSettings cinemaMemberSettings = new MemberSettings();
		BeanUtils.copyFields(settings, cinemaMemberSettings);
		if (settings.getAccessType().getModel() == AccessModel.CENTER) {
			cinemaMemberSettings.setUrl(settings.getAccessType().getUrl());
			cinemaMemberSettings.setUsername(settings.getAccessType()
					.getUsername());
			cinemaMemberSettings.setPassword(settings.getAccessType()
					.getPassword());
		}
		switch (cinemaMemberSettings.getAccessType().getAdapter()) {
		case MTX:
			return new MTXMemberService(cinemaMemberSettings);
		case DX:
			return new DXMemberService(cinemaMemberSettings);
		case HFH:
			return new HFHMemberService(cinemaMemberSettings);
		case NG:
			return new NgMemberService(cinemaMemberSettings);
		default:
			throw new UncheckedException("不支持的接入类型["
					+ cinemaMemberSettings.getAccessType().getAdapter()
							.getText() + "]。");
		}
	}
}