package cn.mopon.cec.core.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.BnLog;
import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.Snack;
import cn.mopon.cec.core.entity.SnackChannel;
import cn.mopon.cec.core.enums.OpenStatus;
import cn.mopon.cec.core.model.SnackChannelCopyModel;
import cn.mopon.cec.core.model.SnackChannelListModel;
import coo.base.util.BeanUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 卖品渠道管理。
 */
@Service
public class SnackChannelService {
	@Resource
	private SnackService snackService;
	@Resource
	private ChannelService channelService;
	@Resource
	private Dao<SnackChannel> snackChannelDao;
	@Resource
	private SecurityService securityService;
	@Resource
	private MessageSource messageSource;
	@Resource
	private BnLogger bnLogger;

	/**
	 * 获取指定ID的卖品渠道。
	 * 
	 * @param channelId
	 *            卖品渠道ID
	 * @return 返回指定ID的卖品渠道。
	 */
	@Transactional(readOnly = true)
	public SnackChannel getSnackChannel(String channelId) {
		return snackChannelDao.get(channelId);
	}

	/**
	 * 获取符合条件的卖品渠道。
	 * 
	 * @param channelCode
	 *            渠道编码
	 * @param snackCode
	 *            卖品编码
	 * @return 返回符合条件的卖品渠道。
	 */
	@Transactional(readOnly = true)
	public SnackChannel searchSnackChannel(String channelCode, String snackCode) {
		FullTextCriteria criteria = snackChannelDao.createFullTextCriteria();
		criteria.addFilterField("status", OpenStatus.OPENED.getValue());
		criteria.addFilterField("channel.code", channelCode);
		criteria.addFilterField("snack.code", snackCode);
		return snackChannelDao.searchUnique(criteria);
	}

	/**
	 * 开放卖品渠道。
	 * 
	 * @param channel
	 *            卖品渠道
	 */
	@Transactional
	@SimpleLog(code = "snackChannel.open.log", vars = { "channel.snack.code",
			"channel.snack.type.name", "channel.channel.name" })
	public void openSnackChannel(SnackChannel channel) {
		channel.setStatus(OpenStatus.OPENED);
	}

	/**
	 * 关闭卖品渠道。
	 * 
	 * @param channel
	 *            卖品渠道
	 */
	@Transactional
	@SimpleLog(code = "snackChannel.close.log", vars = { "channel.snack.code",
			"channel.snack.type.name", "channel.channel.name" })
	public void closeSnackChannel(SnackChannel channel) {
		channel.setStatus(OpenStatus.CLOSED);
	}

	/**
	 * 更新卖品渠道。
	 * 
	 * @param snackChannel
	 *            卖品渠道
	 */
	@Transactional
	@DetailLog(target = "snackChannel", code = "snackChannel.edit.log", vars = {
			"snackChannel.snack.code", "snackChannel.snack.type.name",
			"snackChannel.channel.name" }, type = LogType.ALL)
	public void updateSnackChannel(SnackChannel snackChannel) {
		SnackChannel origSnackChannel = snackChannelDao.get(snackChannel
				.getId());
		BeanUtils.copyFields(snackChannel, origSnackChannel, "status");
	}

	/**
	 * 获取卖品渠道列表模型。
	 * 
	 * @param snackId
	 *            卖品ID
	 * @return 返回卖品渠道列表模型。
	 */
	public SnackChannelListModel getSnackChannelListModel(String snackId) {
		Snack snack = snackService.getSnack(snackId);
		SnackChannelListModel model = new SnackChannelListModel();
		model.setSnack(snack);
		model.addSnackChannels(snack.getSnackChannels());
		List<Channel> channels = channelService.getAllChannels();
		Iterator<Channel> iter = channels.iterator();
		while (iter.hasNext()) {
			Channel channel = iter.next();
			if (!channel.getOpenedCinemas().contains(snack.getCinema())) {
				iter.remove();
			}
		}
		model.addChannels(channels);
		model.getChannels().clear();
		return model;
	}

	/**
	 * 保存卖品渠道。
	 * 
	 * @param model
	 *            卖品渠道列表模型
	 */
	@Transactional
	public void saveSnackChannels(SnackChannelListModel model) {
		Snack snack = snackService.getSnack(model.getSnack().getId());
		// 获取未设置过的渠道列表。
		for (Channel channel : model.getChannels()) {
			// 不允许新增重复的渠道。
			if (snack.containsChannel(channel)) {
				messageSource.thrown("snackChannel.add.repeat");
			}
			SnackChannel snackChannel = new SnackChannel();
			snackChannel.setChannel(channel);
			snackChannel.setConnectFee(model.getOrigChannel().getConnectFee());
			snackChannel.setSnack(snack);
			snackChannel.autoFillIn();
			snackChannelDao.save(snackChannel);
			snack.getSnackChannels().add(snackChannel);

			saveLog(snack, channel);
		}
	}

	/**
	 * 复制卖品渠道。
	 * 
	 * @param model
	 *            卖品渠道复制模型
	 */
	@Transactional
	public void copySnackChannel(SnackChannelCopyModel model) {
		SnackChannel origSnackChannel = snackChannelDao.get(model
				.getOrigSnackChannelId());
		Snack snack = origSnackChannel.getSnack();
		for (Channel channel : model.getChannels()) {
			if (snack.containsChannel(channel)) {
				messageSource.thrown("snackChannel.add.repeat");
			}
			SnackChannel snackChannel = new SnackChannel();
			BeanUtils.copyFields(origSnackChannel, snackChannel, "id,status");
			snackChannel.setChannel(channel);
			snackChannel.autoFillIn();
			snackChannelDao.save(snackChannel);
			snack.getSnackChannels().add(snackChannel);

			saveLog(snack, channel);
		}
	}

	/**
	 * 保存日志。
	 * 
	 * @param snack
	 *            卖品
	 * @param channel
	 *            渠道
	 */
	private void saveLog(Snack snack, Channel channel) {
		BnLog bnLog = new BnLog();
		bnLog.setMessage(messageSource.get("snackChannel.add.log", snack
				.getCinema().getName(), snack.getType().getName(), channel
				.getName()));
		bnLog.setCreateDate(new Date());
		bnLog.setCreator(securityService.getCurrentUser().getUsername());
		bnLogger.log(bnLog);
	}
}