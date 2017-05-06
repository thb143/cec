package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.entity.Channel;
import cn.mopon.cec.core.entity.ChannelSettings;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShelveStatus;
import coo.base.model.Page;
import coo.base.util.BeanUtils;
import coo.base.util.CryptoUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.message.MessageSource;
import coo.core.model.SearchModel;
import coo.core.security.annotations.AutoFillIn;
import coo.core.security.annotations.DetailLog;
import coo.core.security.annotations.DetailLog.LogType;
import coo.core.security.annotations.SimpleLog;

/**
 * 渠道管理。
 */
@Service
public class ChannelService {
	@Resource
	private Dao<Channel> channelDao;
	@Resource
	private Dao<ChannelSettings> channelSettingsDao;
	@Resource
	private ShowService showService;
	@Resource
	private ChannelShowService channelShowService;
	@Resource
	private MessageSource messageSource;

	/**
	 * 分页搜索渠道。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的渠道分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<Channel> searchChannel(SearchModel searchModel) {
		FullTextCriteria criteria = channelDao.createFullTextCriteria();
		criteria.addSortAsc("code", SortField.Type.STRING);
		criteria.setKeyword(searchModel.getKeyword());
		return channelDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 获取所有渠道列表。
	 * 
	 * @return 返回所有渠道列表。
	 */
	@Transactional(readOnly = true)
	public List<Channel> getAllChannels() {
		return channelDao.searchAll("code", true, SortField.Type.STRING);
	}

	/**
	 * 获取渠道。
	 * 
	 * @param channelId
	 *            渠道ID
	 * @return 返回渠道。
	 */
	@Transactional(readOnly = true)
	public Channel getChannel(String channelId) {
		return channelDao.get(channelId);
	}

	/**
	 * 新增渠道。
	 * 
	 * @param channel
	 *            渠道
	 */
	@Transactional
	@AutoFillIn
	@SimpleLog(code = "channel.add.log", vars = { "channel.name" })
	public void createChannel(Channel channel) {
		if (!channelDao.isUnique(channel, "code")) {
			messageSource.thrown("channel.add.exist");
		}
		if (!channelDao.isUnique(channel, "name")) {
			messageSource.thrown("channel.add.name.exist");
		}
		String src = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPXRSTUVWXYZ";
		channel.setSecKey(CryptoUtils.genRandomCode(src, 16));
		channelDao.save(channel);

		ChannelSettings settings = new ChannelSettings();
		settings.setId(channel.getId());
		settings.setChannel(channel);
		channelSettingsDao.save(settings);
	}

	/**
	 * 修改渠道。
	 * 
	 * @param channel
	 *            渠道
	 */
	@Transactional
	@AutoFillIn
	@DetailLog(target = "channel", code = "channel.edit.log", vars = { "channel.name" }, type = LogType.ALL)
	public void updateChannel(Channel channel) {
		if (!channelDao.isUnique(channel, "code")) {
			messageSource.thrown("channel.edit.exist");
		}
		if (!channelDao.isUnique(channel, "name")) {
			messageSource.thrown("channel.edit.name.exist");
		}
		Channel origChannel = getChannel(channel.getId());
		BeanUtils.copyFields(channel, origChannel, "salable");
	}

	/**
	 * 开放渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回影响到的影院排期列表。
	 */
	@Transactional
	@SimpleLog(code = "channel.open.log", vars = { "channel.name" })
	public List<Show> enableChannel(Channel channel) {
		List<Show> shows = new ArrayList<>();
		if (!channel.getOpened()) {
			channel.setOpened(true);
			shows = showService.getMatchedShows(channel);
		}
		return shows;
	}

	/**
	 * 关闭渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回作废渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "channel.close.log", vars = { "channel.name" })
	public Integer disableChannel(Channel channel) {
		Integer count = 0;
		if (channel.getOpened()) {
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(channel);
			for (ChannelShow channelShow : channelShows) {
				channelShow.setStatus(ShelveStatus.INVALID);
				count++;
			}
			channel.setOpened(false);
		}
		return count;
	}

	/**
	 * 开放销售渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回上架渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "channel.open.salable.log", vars = { "channel.name" })
	public Integer openChannelSalable(Channel channel) {
		Integer count = 0;
		if (!channel.getSalable()) {
			channel.setSalable(true);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(channel);
			for (ChannelShow channelShow : channelShows) {
				if (channelShow.isOnable()) {
					channelShow.setStatus(ShelveStatus.ON);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 关闭销售渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回下架渠道排期数。
	 */
	@Transactional
	@SimpleLog(code = "channel.close.salable.log", vars = { "channel.name" })
	public Integer closeChannelSalable(Channel channel) {
		Integer count = 0;
		if (channel.getSalable()) {
			channel.setSalable(false);
			List<ChannelShow> channelShows = channelShowService
					.getMatchedChannelShows(channel);
			for (ChannelShow channelShow : channelShows) {
				if (channelShow.isOffable()) {
					channelShow.setStatus(ShelveStatus.OFF);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 获取开放的渠道列表。
	 * 
	 * @param cinemaIds
	 *            影院ID
	 * @return 开放的渠道列表。
	 */
	@Transactional(readOnly = true)
	public List<Channel> getChannelsByCinema(String cinemaIds) {
		FullTextCriteria criteria = channelDao.createFullTextCriteria();
		criteria.addFilterField("opened", true);
		criteria.addSortAsc("code", SortField.Type.STRING);
		List<Channel> channels = channelDao.searchBy(criteria);
		Iterator<Channel> iterator = channels.iterator();
		while (iterator.hasNext()) {
			Channel channel = iterator.next();
			if (!valid(channel, cinemaIds)) {
				iterator.remove();
			}
		}
		return channels;
	}

	/**
	 * 渠道是否有效。
	 * 
	 * @param channel
	 *            渠道
	 * @param cinemaIds
	 *            影院ID
	 * @return 返回true,有效；false，无效。
	 */
	private boolean valid(Channel channel, String cinemaIds) {
		if (StringUtils.isNotEmpty(cinemaIds)) {
			int count = 0;
			for (Cinema cinema : channel.getOpenedCinemas()) {
				for (String cinemaId : cinemaIds.split(",")) {
					if (cinema.getId().equals(cinemaId)) {
						count++;
						break;
					}
				}
			}
			if (count == cinemaIds.split(",").length) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取开放的渠道列表。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 开放的渠道列表。
	 */
	@Transactional(readOnly = true)
	public List<Channel> searchOpenedChannels(SearchModel searchModel) {
		FullTextCriteria criteria = channelDao.createFullTextCriteria();
		criteria.clearSearchFields();
		criteria.addSearchField("name");
		criteria.addFilterField("opened", true);
		criteria.addSortAsc("code", SortField.Type.STRING);
		criteria.setKeyword(searchModel.getKeyword());
		return channelDao.searchBy(criteria);
	}
}