package cn.mopon.cec.core.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.service.ChannelShowService;
import coo.core.util.SpringUtils;

/**
 * 生成渠道排期任务。
 */
public class ChannelShowGenTask implements Callable<Integer> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String showId;

	/**
	 * 构造方法。
	 * 
	 * @param showId
	 *            影院排期ID
	 */
	public ChannelShowGenTask(String showId) {
		this.showId = showId;
	}

	@Override
	public Integer call() throws Exception {
		try {
			ChannelShowService channelShowService = SpringUtils
					.getBean("channelShowService");
			return channelShowService.genChannelShows(showId);
		} catch (Exception e) {
			log.error("生成影院排期[" + showId + "]对应的渠道排期时发生异常。", e);
			return 0;
		}
	}
}