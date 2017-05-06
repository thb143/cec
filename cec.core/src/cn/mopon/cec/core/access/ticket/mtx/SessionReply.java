package cn.mopon.cec.core.access.ticket.mtx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Interval;

import cn.mopon.cec.core.access.ticket.mtx.vo.MTXShow;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 查询电影院放映计划信息响应对象。
 */
@XStreamAlias("GetCinemaPlanResult")
public class SessionReply extends MtxReply {
	private static XStream xstream;
	@XStreamAlias("shows")
	private List<MTXShow> shows = new ArrayList<MTXShow>();

	static {
		xstream = new GenericXStream();
		xstream.alias("GetCinemaPlanResult", SessionReply.class);
		xstream.alias("GetCinemaPlanResult", MtxReply.class, MtxReply.class);

		xstream.alias("Show", MTXShow.class);
		xstream.registerLocalConverter(MTXShow.class, "showType",
				new IEnumConverter(ShowType.class));
		xstream.registerConverter(new DateConverter("yyyy-MM-ddHH:mm:ss"));
		xstream.registerLocalConverter(MTXShow.class, "showTime",
				new DateConverter("yyyy-MM-ddHH:mm"));

	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 根据时间段获取排期。
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return 返回时间段内的排期列表。
	 */
	public List<Show> getShows(Date start, Date end) {
		List<Show> planShows = new ArrayList<Show>();
		if (CollectionUtils.isNotEmpty(shows)) {
			Interval interval = DateUtils.getInterval(start,
					DateUtils.getNextDay(end));
			for (MTXShow show : this.shows) {
				if (interval.contains(show.getShowTime().getTime())
						&& "1".equals(show.getStatus())) {
					Show planShow = new Show();
					BeanUtils.copyFields(show, planShow,
							"startTime,endTime,status");
					planShow.setDuration(show.getDuration());
					planShows.add(planShow);
				}
			}
		}
		return planShows;
	}

	public List<MTXShow> getShows() {
		return shows;
	}

	public void setShows(List<MTXShow> shows) {
		this.shows = shows;
	}

}
