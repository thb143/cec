package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.entity.SpecialChannel;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.entity.SpecialRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShelveStatus;
import cn.mopon.cec.core.enums.SpecialPolicyStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.SpecialChannelCopyModel;
import cn.mopon.cec.core.model.SpecialChannelListModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.message.MessageSource;
import coo.core.security.annotations.SimpleLog;

/**
 * 特殊定价渠道管理。
 */
@Service
public class SpecialChannelService {
	@Resource
	private Dao<SpecialChannel> specialChannelDao;
	@Resource
	private SpecialPolicyService specialPolicyService;
	@Resource
	private SpecialRuleService specialRuleService;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private SecurityService securityService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 获取指定ID的特殊定价渠道。
	 * 
	 * @param channelId
	 *            特殊定价渠道ID
	 * @return 返回指定ID的特殊定价渠道。
	 */
	@Transactional(readOnly = true)
	public SpecialChannel getSpecialChannel(String channelId) {
		return specialChannelDao.get(channelId);
	}

	/**
	 * 开放特殊定价渠道。
	 * 
	 * @param channel
	 *            特殊定价渠道
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "specialChannel.open.log", vars = {
			"channel.rule.policy.name", "channel.rule.name",
			"channel.channel.name" })
	public List<Show> openSpecialChannel(SpecialChannel channel) {
		// 如果特殊定价渠道未生效，不允许开放。
		checkValidStatus(channel, "specialChannel.open.invalid");
		List<Show> shows = new ArrayList<>();
		if (channel.getEnabled() == EnabledStatus.DISABLED) {
			// 开放特殊定价渠道时将特殊定价渠道影响到的排期重新生成渠道排期。
			channel.setEnabled(EnabledStatus.ENABLED);
			shows = showService.getMatchedShows(channel.getRule());
		}
		return shows;
	}

	/**
	 * 关闭特殊定价渠道。
	 * 
	 * @param channel
	 *            特殊定价渠道
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "specialChannel.close.log", vars = {
			"channel.rule.policy.name", "channel.rule.name",
			"channel.channel.name" })
	public List<Show> closeSpecialChannel(SpecialChannel channel) {
		// 如果特殊定价渠道未生效，不允许关闭。
		checkValidStatus(channel, "specialChannel.close.invalid");
		List<Show> shows = new ArrayList<>();
		if (channel.getEnabled() == EnabledStatus.ENABLED) {
			// 在设置特殊定价渠道关闭前先获取规则影响到的影院排期
			shows = showService.getMatchedShows(channel.getRule());
			// 关闭特殊定价渠道时将特殊定价渠道影响到的排期全部置为失效。
			channel.setEnabled(EnabledStatus.DISABLED);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(channel);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
			}
		}
		return shows;
	}

	/**
	 * 删除特殊定价渠道。
	 * 
	 * @param channel
	 *            特殊定价渠道
	 */
	@Transactional
	@SimpleLog(code = "specialChannel.delete.log", vars = {
			"channel.rule.policy.name", "channel.rule.name",
			"channel.channel.name" })
	public void deleteSpecialChannel(SpecialChannel channel) {
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(channel.getRule()
				.getPolicy(), "specialChannel.delete.approve");
		// 如果渠道"已生效",不允许删除。
		if (channel.getValid() == ValidStatus.VALID) {
			messageSource.thrown("specialChannel.delete.valid");
		}
		// 如果特殊定价渠道是副本则解除原本的关联。
		if (channel.isCopy()) {
			SpecialChannel boundCinemaRule = channel.getBoundChannel();
			boundCinemaRule.setBoundChannel(null);
			channel.getRule().getChannels().remove(channel);
			specialChannelDao.remove(channel);
		} else {
			// 如果规则“未生效”，即没有产品关联，直接删除。
			channel.getRule().getChannels().remove(channel);
			specialChannelDao.remove(channel);
		}
	}

	/**
	 * 更新特殊定价渠道。
	 * 
	 * @param channel
	 *            特殊定价渠道
	 */
	@Transactional
	public void updateSpecialChannel(SpecialChannel channel) {
		SpecialChannel origChannel = specialChannelDao.get(channel.getId());
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		SpecialPolicy policy = origChannel.getRule().getPolicy();
		specialPolicyService.checkSpecialPolicyApprove(policy,
				"specialChannel.eidt.approve");
		// 如果渠道结算方式未被修改，不做处理。
		if (channel.getSettleRule().toString()
				.equals(origChannel.getSettleRule().toString())
				&& channel.getConnectFee() == origChannel.getConnectFee()) {
			return;
		}
		// 更新特殊定价渠道
		update(channel, origChannel);
		policy.setStatus(SpecialPolicyStatus.SUBMIT);
	}

	/**
	 * 更新特殊定价渠道。
	 * 
	 * @param channel
	 *            新特殊定价渠道
	 * @param origChannel
	 *            原特殊定价渠道
	 */
	private void update(SpecialChannel channel, SpecialChannel origChannel) {
		// 如果编辑的渠道是已生效的，则新增一条绑定关系的渠道。如果编辑的渠道是未生效的，则直接更新该渠道。
		if (origChannel.getValid() == ValidStatus.VALID) {
			// 如果特殊定价渠道已经被编辑过，不允许再次进行编辑。
			if (channel.getBoundChannel() != null) {
				messageSource.thrown("specialChannel.edit.repeat");
			}
			channel.setId(null);
			channel.setRule(origChannel.getRule());
			channel.setChannel(origChannel.getChannel());
			channel.setValid(ValidStatus.UNVALID);
			channel.setBoundChannel(origChannel);
			channel.setRule(origChannel.getRule());
			channel.autoFillIn();
			origChannel.getRule().getChannels().add(channel);
			specialChannelDao.save(channel);
			origChannel.setBoundChannel(channel);
		} else {
			BeanUtils.copyFields(channel, origChannel);
			origChannel.setModifier(securityService.getCurrentUser());
			origChannel.setModifyDate(new Date());
		}
	}

	/**
	 * 获取特殊定价渠道列表模型。
	 * 
	 * @param ruleId
	 *            规则ID
	 * @return 返回特殊定价渠道列表模型。
	 */
	public SpecialChannelListModel getSpecialChannelListModel(String ruleId) {
		SpecialRule rule = specialRuleService.getSpecialRule(ruleId);
		SpecialChannelListModel model = new SpecialChannelListModel();
		model.setSpecialRule(rule);
		model.addSpecialChannels(rule.getChannelsExcludeInvalid());
		List<Channel> channels = rule.getPolicy().getChannels();
		model.addChannels(channels);
		model.getChannels().clear();
		return model;
	}

	/**
	 * 保存特殊定价渠道。
	 * 
	 * @param model
	 *            特殊定价渠道列表模型
	 */
	@Transactional
	public void saveSpecialChannels(SpecialChannelListModel model) {
		SpecialRule rule = specialRuleService.getSpecialRule(model
				.getSpecialRule().getId());
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(rule.getPolicy(),
				"specialChannel.eidt.approve");
		// 获取未设置过的渠道列表。
		for (Channel channel : model.getChannels()) {
			// 不允许新增重复的渠道。
			if (rule.containsChannel(channel)) {
				messageSource.thrown("specialChannel.add.repeat");
			}
			SpecialChannel specialChannel = new SpecialChannel();
			specialChannel.setChannel(channel);
			specialChannel.setSettleRule(model.getSettleRule());
			specialChannel
					.setConnectFee(model.getOrigChannel().getConnectFee());
			specialChannel.setRule(rule);
			rule.getChannels().add(specialChannel);
			specialChannel.autoFillIn();
			specialChannelDao.save(specialChannel);
			// 将策略修改为“待提交”状态。
			rule.getPolicy().setStatus(SpecialPolicyStatus.SUBMIT);
		}
	}

	/**
	 * 复制特殊定价渠道。
	 * 
	 * @param model
	 *            特殊定价渠道复制模型
	 */
	@Transactional
	public void copySpecialChannel(SpecialChannelCopyModel model) {
		SpecialChannel origSpecialChannel = specialChannelDao.get(model
				.getOrigSpecialChannelId());
		SpecialRule rule = origSpecialChannel.getRule();
		// 如果策略处于待审核、待审批状态，不允许进行该操作。
		specialPolicyService.checkSpecialPolicyApprove(rule.getPolicy(),
				"specialChannel.copy.approve");
		for (Channel channel : model.getChannels()) {
			if (rule.containsChannel(channel)) {
				messageSource.thrown("specialChannel.add.repeat");
			}
			SpecialChannel specialChannel = new SpecialChannel();
			BeanUtils.copyFields(origSpecialChannel, specialChannel,
					"id,valid,enabled,boundChannel");
			specialChannel.setChannel(channel);
			rule.getChannels().add(specialChannel);
			specialChannel.autoFillIn();
			specialChannelDao.save(specialChannel);
			// 将策略修改为“待提交”状态。
			rule.getPolicy().setStatus(SpecialPolicyStatus.SUBMIT);
		}
	}

	/**
	 * 检测特殊定价渠道是否已生效，如果未生效抛出不允许操作的异常提示信息。
	 * 
	 * @param channel
	 *            特殊定价渠道
	 * @param msgKey
	 *            提示语Key
	 */
	private void checkValidStatus(SpecialChannel channel, String msgKey) {
		if (channel.getValid() == ValidStatus.UNVALID) {
			messageSource.thrown(msgKey);
		}
	}
}