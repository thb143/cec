package cn.mopon.cec.core.task;

import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.mopon.cec.core.service.StatService;
import coo.base.util.DateUtils;
import coo.core.util.SpringUtils;

/**
 * 日结维护统计任务。
 */
public class DailyStatRegenTask implements Callable<String> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private Date statDate;

	/**
	 * 构造方法。
	 * 
	 * @param statDate
	 *            统计日期
	 */
	public DailyStatRegenTask(Long statDate) {
		this.statDate = new Date(statDate);
	}

	@Override
	public String call() throws Exception {
		try {
			StatService statService = SpringUtils.getBean("statService");
			statService.syncTicketOrderDetail(statDate);
			return DateUtils.format(statDate);
		} catch (Exception e) {
			log.error("日结维护统计[" + statDate + "}]时发生异常。", e);
			return null;
		}
	}
}
