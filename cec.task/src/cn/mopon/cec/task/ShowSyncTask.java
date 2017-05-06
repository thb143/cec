package cn.mopon.cec.task;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.service.ShowService;
import coo.core.util.SpringUtils;

/**
 * 排期同步任务。
 */
public class ShowSyncTask implements Callable<Void> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String cinemaId;

	/**
	 * 构造方法。
	 * 
	 * @param cinemaId
	 *            影院ID
	 */
	public ShowSyncTask(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	@Override
	public Void call() throws Exception {
		try {
			ShowService showService = SpringUtils.getBean("showService");
			showService.syncShows(cinemaId);
		} catch (Exception e) {
			log.error("定时同步排期时发生异常。", e);
		}
		return null;
	}
}
