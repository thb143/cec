package cn.mopon.cec.core.access.ticket.dx;

import java.util.ArrayList;
import java.util.List;

import cn.mopon.cec.core.access.ticket.dx.vo.DXShow;
import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.entity.Show;
import cn.mopon.cec.core.enums.ShowType;

import com.thoughtworks.xstream.XStream;

import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.DateUtils;
import coo.core.xstream.DateConverter;
import coo.core.xstream.GenericXStream;
import coo.core.xstream.IEnumConverter;

/**
 * 排期响应对象。
 */
public class SessionReply extends DxReply {
	private static XStream xstream;
	private List<DXShow> shows = new ArrayList<>();

	static {
		xstream = new GenericXStream();
		xstream.alias("root", DxServiceReply.class);
		xstream.alias("data", SessionReply.class);
		xstream.alias("data", DxReply.class, SessionReply.class);

		xstream.alias("show", DXShow.class);
		xstream.registerLocalConverter(DXShow.class, "showType",
				new IEnumConverter(ShowType.class));
		xstream.registerLocalConverter(DXShow.class, "showTime",
				new DateConverter(DateUtils.SECOND));
		xstream.registerConverter(new DateConverter(DateUtils.SECOND));
	}

	@Override
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * 获取排期。
	 * 
	 * @param cinema
	 *            影院
	 * @return 返回对应的排期。
	 */
	public List<Show> getShows(Cinema cinema) {
		List<Show> planShows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shows)) {
			Show planShow = null;
			for (DXShow show : shows) {
				planShow = new Show();
				BeanUtils.copyFields(show, planShow, "startTime,endTime");
				planShow.setCinema(cinema);
				planShow.getHall().setCinema(cinema);
				planShow.setDuration(show.getDuration());
				planShows.add(planShow);
			}
		}
		return planShows;
	}

	public void setShows(List<DXShow> shows) {
		this.shows = shows;
	}

}
