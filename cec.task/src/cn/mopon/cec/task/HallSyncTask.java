package cn.mopon.cec.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.service.HallService;
import coo.core.util.SpringUtils;

/**
 * 影厅同步任务。
 */
public class HallSyncTask implements Callable<Void> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String cinemaId;

	/**
	 * 构造方法。
	 * 
	 * @param cinemaId
	 *            影院ID
	 */
	public HallSyncTask(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	@Override
	public Void call() throws Exception {
		try {
			HallService hallService = SpringUtils.getBean("hallService");
			hallService.syncHalls(cinemaId);
		} catch (Exception e) {
			log.error("定时同步影厅和座位时发生异常。", e);
		}
		return null;
	}
}