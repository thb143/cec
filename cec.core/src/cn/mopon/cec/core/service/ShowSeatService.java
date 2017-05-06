package cn.mopon.cec.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.mopon.cec.core.access.ticket.TicketAccessService;
import cn.mopon.cec.core.access.ticket.TicketAccessServiceFactory;
import cn.mopon.cec.core.assist.constants.RedisKey;
import cn.mopon.cec.core.entity.ChannelShow;
import cn.mopon.cec.core.entity.Seat;
import cn.mopon.cec.core.entity.TicketOrder;
import cn.mopon.cec.core.entity.TicketOrderItem;
import cn.mopon.cec.core.enums.SellStatus;
import cn.mopon.cec.core.model.ShowSeat;
import coo.base.constants.Chars;
import coo.base.exception.BusinessException;
import coo.base.util.CollectionUtils;
import coo.core.util.IEnumUtils;

/**
 * 排期座位管理组件。
 */
@Service
public class ShowSeatService {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> showSeatsCache;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Long> updateTimeCache;
	/** 缓存有效时间 */
	@Value("${showSeatMap.cached.second:300}")
	private Integer cachedSecond = 300;

	/**
	 * 初始化排期座位列表。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回排期座位列表。
	 */
	private Map<String, String> initShowSeats(ChannelShow channelShow) {
		setUpdateTime(channelShow);
		Map<String, String> showSeatStatusMap = getExternalShowSeatMap(
				channelShow, SellStatus.DISABLED);

		cacheShowSeats(channelShow, showSeatStatusMap);
		return showSeatStatusMap;
	}

	/**
	 * 获取指定状态的排期座位列表。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param status
	 *            排期座位状态
	 * @return 返回指定状态的排期座位列表。
	 */
	@Transactional(readOnly = true)
	public List<ShowSeat> getShowSeats(ChannelShow channelShow,
			SellStatus... status) {
		Map<String, String> seatStatues = showSeatsCache
				.entries(getKey(channelShow));
		Boolean cacheExpired = (System.currentTimeMillis() - getUpdateTime(channelShow)) / 1000 > cachedSecond;

		if (cacheExpired) {
			seatStatues = initShowSeats(channelShow);
		}

		List<ShowSeat> showSeats = getShowSeats(channelShow, seatStatues);

		if (CollectionUtils.isNotEmpty(status)) {
			Iterator<ShowSeat> showSeatIterator = showSeats.iterator();
			while (showSeatIterator.hasNext()) {
				if (!CollectionUtils.contains(status, showSeatIterator.next()
						.getStatus())) {
					showSeatIterator.remove();
				}
			}
		}
		return showSeats;
	}

	/**
	 * 获取排期座位。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param seatStatues
	 *            座位状态
	 * @return 排期座位列表。
	 */
	private List<ShowSeat> getShowSeats(ChannelShow channelShow,
			Map<String, String> seatStatues) {
		Map<String, ShowSeat> showSeatMap = channelShow.getHall()
				.getShowSeatMap();
		for (Map.Entry<String, ShowSeat> entry : showSeatMap.entrySet()) {
			String status = seatStatues.get(entry.getKey());
			ShowSeat showSeat = entry.getValue();
			if (null != status) {
				showSeat.setStatus(IEnumUtils.getIEnumByValue(SellStatus.class,
						status));
			} else {
				showSeat.setStatus(SellStatus.ENABLED);
			}
		}
		return new ArrayList<>(showSeatMap.values());
	}

	/**
	 * 获取指定座位编码的排期座位列表。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param seatCodes
	 *            座位编码，多个座位编码之间用","分割
	 * @return 返回指定座位编码的排期座位列表。
	 */
	public List<ShowSeat> getShowSeats(ChannelShow channelShow, String seatCodes) {
		List<ShowSeat> showSeats = new ArrayList<>();
		for (String seatCode : seatCodes.split(Chars.COMMA)) {
			ShowSeat showSeat = getCachedShowSeat(channelShow, seatCode);
			if (showSeat == null) {
				throw new BusinessException("未找到编码为[" + seatCode + "]的座位。");
			}
			// 该方法目前只被TicketFacade调用，如果有其它方法调用，需要考虑这个逻辑是否适应其它方法调用。
			if (showSeat.getStatus() == SellStatus.DISABLED) {
				throw new BusinessException("编码为[" + seatCode + "]的座位已锁。");
			}
			showSeats.add(showSeat);
		}
		return showSeats;
	}

	/**
	 * 更新缓存的排期座位列表。
	 * 
	 * @param order
	 *            选座票订单
	 * @param status
	 *            更新状态
	 */
	@Transactional(readOnly = true)
	public void updateShowSeats(TicketOrder order, SellStatus status) {
		for (TicketOrderItem orderItem : order.getOrderItems()) {
			String seatCode = orderItem.getSeatCode();
			showSeatsCache.put(
					getKey(order.getCinema().getCode(), order.getShowCode()),
					seatCode, status.getValue());
		}
	}

	/**
	 * 缓存排期座位图。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param showSeatMap
	 *            排期座位图
	 */
	private void cacheShowSeats(ChannelShow channelShow,
			Map<String, String> showSeatMap) {
		long expireTime = (channelShow.getExpireTime().getTime() - System
				.currentTimeMillis()) / 1000;
		String showSeatKey = getKey(channelShow);
		redisTemplate.delete(showSeatKey);
		if (!showSeatMap.isEmpty() && expireTime > 0) {
			showSeatsCache.putAll(showSeatKey, showSeatMap);
			redisTemplate.expire(showSeatKey, expireTime, TimeUnit.SECONDS);
		} 
	}

	/**
	 * 从缓存中获取指定的排期座位。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param seatCode
	 *            座位编号
	 * @return 返回指定的排期座位。
	 */
	private ShowSeat getCachedShowSeat(ChannelShow channelShow, String seatCode) {
		String status = showSeatsCache.get(getKey(channelShow), seatCode);
		Seat seat = channelShow.getHall().getSeat(seatCode);
		if (seat == null) {
			return null;
		}
		ShowSeat showSeat = ShowSeat.createBySeat(seat);
		if (status != null) {
			showSeat.setStatus(IEnumUtils.getIEnumByValue(SellStatus.class,
					status));
		}
		return showSeat;
	}

	/**
	 * 根据排期获取座位图缓存key。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 排期座位的缓存key。
	 */
	private String getKey(ChannelShow channelShow) {
		return getKey(channelShow.getCinema().getCode(),
				channelShow.getShowCode());
	}

	/**
	 * 根据缓存key。
	 * 
	 * @param cinemaCode
	 *            影院编码
	 * @param showCode
	 *            排期编码
	 * @return 返回缓存key。
	 */
	private String getKey(String cinemaCode, String showCode) {
		return RedisKey.SHOW_SEAT_MAP + "_" + cinemaCode + "_" + showCode;
	}

	/**
	 * 设置更新时间缓存。
	 * 
	 * @param channelShow
	 *            渠道排期
	 */
	private void setUpdateTime(ChannelShow channelShow) {
		updateTimeCache.put(RedisKey.SHOW_SEAT_UPDATE_TIME_MAP,
				getKey(channelShow), System.currentTimeMillis());
	}

	/**
	 * 获取更新时间缓存。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @return 返回更新时间缓存。
	 */
	private Long getUpdateTime(ChannelShow channelShow) {
		Long updateTime = updateTimeCache.get(
				RedisKey.SHOW_SEAT_UPDATE_TIME_MAP, getKey(channelShow));
		if (updateTime == null) {
			updateTime = 0L;
		}
		return updateTime;
	}

	/**
	 * 获取外部排期座位图。
	 * 
	 * @param channelShow
	 *            渠道排期
	 * @param status
	 *            状态
	 * @return 返回外部排期座位列表。
	 */
	private Map<String, String> getExternalShowSeatMap(ChannelShow channelShow,
			SellStatus... status) {
		TicketAccessService ticketAccessService = TicketAccessServiceFactory
				.getTicketService(channelShow.getCinema().getTicketSettings());
		List<ShowSeat> externalShowSeats = ticketAccessService.getShowSeats(
				channelShow, status);
		return getShowSeatStatusMap(externalShowSeats);
	}

	/**
	 * 获取排期座位MAP。
	 * 
	 * @param showSeats
	 *            排期座位列表
	 * @return 返回排期座位MAP。
	 */
	private Map<String, String> getShowSeatStatusMap(List<ShowSeat> showSeats) {
		Map<String, String> showSeatMap = new HashMap<>();
		for (ShowSeat showSeat : showSeats) {
			showSeatMap
					.put(showSeat.getCode(), showSeat.getStatus().getValue());
		}
		return showSeatMap;
	}
}
