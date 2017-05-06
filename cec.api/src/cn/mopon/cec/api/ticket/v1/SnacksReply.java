package cn.mopon.cec.api.ticket.v1;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.api.ticket.v1.vo.SnackVo;
import cn.mopon.cec.core.entity.BenefitCardType;
import cn.mopon.cec.core.entity.BenefitCardTypeSnackRule;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ValidStatus;

/**
 * 查询卖品响应对象。
 */
public class SnacksReply extends ApiReply {
	/** 卖品列表 */
	private List<SnackVo> snacks = new ArrayList<SnackVo>();

	/**
	 * 构造方法。
	 * 
	 * @param snackChannels
	 *            卖品渠道列表
	 * @param snackRule
	 *            权益卡类卖品规则
	 * @param url
	 *            图片路径
	 */
	public SnacksReply(List<SnackChannel> snackChannels,
			List<BenefitCardType> types, String url) {
		for (SnackChannel snackChannel : snackChannels) {
			List<BenefitCardTypeSnackRule> snackRules = new ArrayList<>();
			for (BenefitCardType type : types) {
				BenefitCardTypeSnackRule snackRule = type
						.getMatchedSnackRule(snackChannel.getSnack());
				if (snackRule != null
						&& snackRule.getValid() == ValidStatus.VALID
						&& snackRule.getEnabled() == EnabledStatus.ENABLED) {
					snackRules.add(snackRule);
				}
			}
			this.snacks.add(new SnackVo(snackChannel, snackRules, url));
		}
	}

	public List<SnackVo> getSnacks() {
		return snacks;
	}

	public void setSnacks(List<SnackVo> snacks) {
		this.snacks = snacks;
	}
}