package cn.mopon.cec.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.mopon.cec.core.entity.ChannelPolicy;
import cn.mopon.cec.core.entity.CinemaPolicy;
import cn.mopon.cec.core.entity.SpecialPolicy;
import cn.mopon.cec.core.enums.StatDim;
import coo.base.util.DateUtils;
import coo.base.util.NumberUtils;
import coo.core.util.IEnumUtils;

/**
 * 首页模型。
 */
public class HomeModel {
	/**
	 * 业务概览。
	 */
	public class BusinessOverviewModel {
		/** 影院总数 */
		private Integer cinemaCount = 0;
		/** 渠道总数 */
		private Integer channelCount = 0;
		/** 订单总数 */
		private Integer orderCount = 0;
		/** 出票总数 */
		private Integer ticketCount = 0;
		/** 订单总金额 */
		private Double orderAmount = 0D;
		/** 今日订单数 */
		private Integer todayOrderCount = 0;
		/** 今日出票数 */
		private Integer todayTicketCount = 0;
		/** 今日订单金额 */
		private Double todayOrderAmount = 0D;
		/** 最高订单日 */
		private Date topOrderDate = new Date();
		/** 最高订单数 */
		private Integer topOrderCount = 0;

		public Integer getCinemaCount() {
			return cinemaCount;
		}

		public void setCinemaCount(Integer cinemaCount) {
			this.cinemaCount = cinemaCount;
		}

		public Integer getChannelCount() {
			return channelCount;
		}

		public void setChannelCount(Integer channelCount) {
			this.channelCount = channelCount;
		}

		public Integer getOrderCount() {
			return orderCount;
		}

		public void setOrderCount(Integer orderCount) {
			this.orderCount = orderCount;
		}

		public Integer getTicketCount() {
			return ticketCount;
		}

		public void setTicketCount(Integer ticketCount) {
			this.ticketCount = ticketCount;
		}

		public Double getOrderAmount() {
			return orderAmount;
		}

		public void setOrderAmount(Double orderAmount) {
			this.orderAmount = orderAmount;
		}

		public Integer getTodayOrderCount() {
			return todayOrderCount;
		}

		public void setTodayOrderCount(Integer todayOrderCount) {
			this.todayOrderCount = todayOrderCount;
		}

		public Integer getTodayTicketCount() {
			return todayTicketCount;
		}

		public void setTodayTicketCount(Integer todayTicketCount) {
			this.todayTicketCount = todayTicketCount;
		}

		public Double getTodayOrderAmount() {
			return todayOrderAmount;
		}

		public void setTodayOrderAmount(Double todayOrderAmount) {
			this.todayOrderAmount = todayOrderAmount;
		}

		public Date getTopOrderDate() {
			return topOrderDate;
		}

		public void setTopOrderDate(Date topOrderDate) {
			this.topOrderDate = topOrderDate;
		}

		public Integer getTopOrderCount() {
			return topOrderCount;
		}

		public void setTopOrderCount(Integer topOrderCount) {
			this.topOrderCount = topOrderCount;
		}
	}

	/**
	 * 待办事项。
	 */
	public class TodoListModel {
		/** 待审核影院策略数 */
		private Integer auditCinemaPolicyCount = 0;
		/** 待审批影院策略数 */
		private Integer approveCinemaPolicyCount = 0;
		/** 待审核渠道策略数 */
		private Integer auditChannelPolicyCount = 0;
		/** 待审批渠道策略数 */
		private Integer approveChannelPolicyCount = 0;
		/** 待审核特价策略数 */
		private Integer auditSpecialPolicyCount = 0;
		/** 待审批特价策略数 */
		private Integer approveSpecialPolicyCount = 0;
		/** 即将到期的影院策略列表 */
		private List<CinemaPolicy> willExpireCinemaPolicys = new ArrayList<>();
		/** 即将到期的渠道策略列表 */
		private List<ChannelPolicy> willExpireChannelPolicys = new ArrayList<>();
		/** 即将到期的特价策略列表 */
		private List<SpecialPolicy> willExpireSpecialPolicys = new ArrayList<>();

		public Integer getAuditCinemaPolicyCount() {
			return auditCinemaPolicyCount;
		}

		public void setAuditCinemaPolicyCount(Integer auditCinemaPolicyCount) {
			this.auditCinemaPolicyCount = auditCinemaPolicyCount;
		}

		public Integer getApproveCinemaPolicyCount() {
			return approveCinemaPolicyCount;
		}

		public void setApproveCinemaPolicyCount(Integer approveCinemaPolicyCount) {
			this.approveCinemaPolicyCount = approveCinemaPolicyCount;
		}

		public Integer getAuditChannelPolicyCount() {
			return auditChannelPolicyCount;
		}

		public void setAuditChannelPolicyCount(Integer auditChannelPolicyCount) {
			this.auditChannelPolicyCount = auditChannelPolicyCount;
		}

		public Integer getApproveChannelPolicyCount() {
			return approveChannelPolicyCount;
		}

		public void setApproveChannelPolicyCount(
				Integer approveChannelPolicyCount) {
			this.approveChannelPolicyCount = approveChannelPolicyCount;
		}

		public Integer getAuditSpecialPolicyCount() {
			return auditSpecialPolicyCount;
		}

		public void setAuditSpecialPolicyCount(Integer auditSpecialPolicyCount) {
			this.auditSpecialPolicyCount = auditSpecialPolicyCount;
		}

		public Integer getApproveSpecialPolicyCount() {
			return approveSpecialPolicyCount;
		}

		public void setApproveSpecialPolicyCount(
				Integer approveSpecialPolicyCount) {
			this.approveSpecialPolicyCount = approveSpecialPolicyCount;
		}

		public List<CinemaPolicy> getWillExpireCinemaPolicys() {
			return willExpireCinemaPolicys;
		}

		public void setWillExpireCinemaPolicys(
				List<CinemaPolicy> willExpireCinemaPolicys) {
			this.willExpireCinemaPolicys = willExpireCinemaPolicys;
		}

		public List<ChannelPolicy> getWillExpireChannelPolicys() {
			return willExpireChannelPolicys;
		}

		public void setWillExpireChannelPolicys(
				List<ChannelPolicy> willExpireChannelPolicys) {
			this.willExpireChannelPolicys = willExpireChannelPolicys;
		}

		public List<SpecialPolicy> getWillExpireSpecialPolicys() {
			return willExpireSpecialPolicys;
		}

		public void setWillExpireSpecialPolicys(
				List<SpecialPolicy> willExpireSpecialPolicys) {
			this.willExpireSpecialPolicys = willExpireSpecialPolicys;
		}
	}

	/**
	 * 今日排行。
	 */
	public class TodayRanksModel {
		/** 影院排行 */
		private List<RankModel> cinemaRank = new ArrayList<>();
		/** 渠道排行 */
		private List<RankModel> channelRank = new ArrayList<>();
		/** 影片排行 */
		private List<RankModel> filmRank = new ArrayList<>();
		/** 特价排行 */
		private List<RankModel> specialPolicyRank = new ArrayList<>();

		/**
		 * 添加排行数据。
		 * 
		 * @param key
		 *            缓存field
		 * @param value
		 *            缓存值
		 */
		public void addRank(String key, Number value) {
			String[] keys = key.split("_");
			RankModel rank;
			switch (keys[0]) {
			case "CIN":
				rank = getRank(keys[1], cinemaRank);
				add(rank, keys, value);
				break;
			case "CHA":
				rank = getRank(keys[1], channelRank);
				add(rank, keys, value);
				break;
			case "FIL":
				rank = getRank(keys[1], filmRank);
				add(rank, keys, value);
				break;
			case "SPO":
				rank = getRank(keys[1], specialPolicyRank);
				add(rank, keys, value);
				break;
			default:
				break;
			}
		}

		/**
		 * 添加排行统计。
		 * 
		 * @param rank
		 *            排行对象
		 * @param keys
		 *            key 数组
		 * @param value
		 *            值
		 */
		private void add(RankModel rank, String[] keys, Number value) {
			StatDim dim = IEnumUtils.getIEnumByValue(StatDim.class, keys[3]);
			rank.setName(keys[2]);
			if (dim == StatDim.ORDER) {
				rank.setOrderCount(rank.getOrderCount() + value.intValue());
			} else if (dim == StatDim.TICKET) {
				rank.setTicketCount(rank.getTicketCount() + value.intValue());
			} else if (dim == StatDim.CHANNELSUBSIDYORDER) {
				rank.setChannelSubsidyOrderCount(rank
						.getChannelSubsidyOrderCount() + value.intValue());
			} else if (dim == StatDim.CHANNELSUBSIDYTICKET) {
				rank.setChannelSubsidyTicketCount(rank
						.getChannelSubsidyTicketCount() + value.intValue());
			} else if (dim == StatDim.CHANNELSUBSIDYAMOUNT) {
				rank.setChannelSubsidyAmount(rank.getChannelSubsidyAmount()
						+ value.doubleValue());
			} else {
				rank.setOrderAmount(rank.getOrderAmount() + value.doubleValue());
			}
		}

		/**
		 * 获取排行。
		 * 
		 * @param id
		 *            ID
		 * @param ranks
		 *            排行列表
		 * @return 排行
		 */
		private RankModel getRank(String id, List<RankModel> ranks) {
			for (RankModel rank : ranks) {
				if (rank.getId().equals(id)) {
					return rank;
				}
			}
			RankModel rank = new RankModel();
			rank.setId(id);
			ranks.add(rank);
			return rank;
		}

		/**
		 * 获取排序后的影院排行。
		 * 
		 * @return 排序后的影院排行。
		 */
		public List<RankModel> getCinemaRank() {
			Collections.sort(cinemaRank);
			return cinemaRank;
		}

		public void setCinemaRank(List<RankModel> cinemaRank) {
			this.cinemaRank = cinemaRank;
		}

		/**
		 * 获取排序后的渠道排行。
		 * 
		 * @return 排序后的渠道排行。
		 */
		public List<RankModel> getChannelRank() {
			Collections.sort(channelRank);
			return channelRank;
		}

		public void setChannelRank(List<RankModel> channelRank) {
			this.channelRank = channelRank;
		}

		/**
		 * 获取排序后的影片排行。
		 * 
		 * @return 排序后的影片排行。
		 */
		public List<RankModel> getFilmRank() {
			Collections.sort(filmRank);
			return filmRank;
		}

		public void setFilmRank(List<RankModel> filmRank) {
			this.filmRank = filmRank;
		}

		/**
		 * 获取排序后的特价排行。
		 * 
		 * @return 排序后的特价排行。
		 */
		public List<RankModel> getSpecialPolicyRank() {
			Collections.sort(specialPolicyRank);
			return specialPolicyRank;
		}

		public void setSpecialPolicyRank(List<RankModel> specialPolicyRank) {
			this.specialPolicyRank = specialPolicyRank;
		}
	}

	/**
	 * 排行模型。
	 */
	public class RankModel implements Comparable<RankModel> {
		/** 业务ID */
		private String id;
		/** 名称 */
		private String name;
		/** 订单数 */
		private Integer orderCount = 0;
		/** 出票数 */
		private Integer ticketCount = 0;
		/** 订单金额 */
		private Double orderAmount = 0D;
		/** 渠道补贴订单数 */
		private Integer channelSubsidyOrderCount = 0;
		/** 渠道补贴票数 */
		private Integer channelSubsidyTicketCount = 0;
		/** 渠道补贴金额 */
		private Double channelSubsidyAmount = 0D;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getOrderCount() {
			return orderCount;
		}

		public void setOrderCount(Integer orderCount) {
			this.orderCount = orderCount;
		}

		public Integer getTicketCount() {
			return ticketCount;
		}

		public void setTicketCount(Integer ticketCount) {
			this.ticketCount = ticketCount;
		}

		public Double getOrderAmount() {
			return orderAmount;
		}

		public void setOrderAmount(Double orderAmount) {
			this.orderAmount = orderAmount;
		}

		public Integer getChannelSubsidyOrderCount() {
			return channelSubsidyOrderCount;
		}

		public void setChannelSubsidyOrderCount(Integer channelSubsidyOrderCount) {
			this.channelSubsidyOrderCount = channelSubsidyOrderCount;
		}

		public Integer getChannelSubsidyTicketCount() {
			return channelSubsidyTicketCount;
		}

		public void setChannelSubsidyTicketCount(
				Integer channelSubsidyTicketCount) {
			this.channelSubsidyTicketCount = channelSubsidyTicketCount;
		}

		public Double getChannelSubsidyAmount() {
			return channelSubsidyAmount;
		}

		public void setChannelSubsidyAmount(Double channelSubsidyAmount) {
			this.channelSubsidyAmount = channelSubsidyAmount;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public int compareTo(RankModel o) {
			return o.getOrderAmount().compareTo(getOrderAmount());
		}
	}

	/**
	 * 销售趋势列表模型。
	 */
	public class SalesChartListModel {
		private List<SalesChartModel> salesCharts = new ArrayList<>();

		/**
		 * 添加排行数据。
		 * 
		 * @param key
		 *            缓存field
		 * @param value
		 *            缓存值
		 */
		public void addStat(String key, Number value) {
			String[] keys = key.split("_");
			SalesChartModel model = getModel(keys[0]);
			StatDim dim = IEnumUtils.getIEnumByValue(StatDim.class, keys[1]);
			if (dim == StatDim.ORDER) {
				model.setOrderCount(model.getOrderCount() + value.intValue());
			} else if (dim == StatDim.TICKET) {
				model.setTicketCount(model.getTicketCount() + value.intValue());
			} else {
				model.setOrderAmount(model.getOrderAmount()
						+ value.doubleValue());
			}
		}

		/**
		 * 获取日期模型。
		 * 
		 * @param date
		 *            日期
		 * @return 模型
		 */
		private SalesChartModel getModel(String date) {
			for (SalesChartModel model : salesCharts) {
				if (date.equals(model.getDate())) {
					return model;
				}
			}
			SalesChartModel model = new SalesChartModel();
			model.setDate(date);
			salesCharts.add(model);
			return model;
		}

		/**
		 * 获取日销售数据列表。
		 * 
		 * @return 日销售数据列表。
		 */
		public List<SalesChartModel> getSalesCharts() {
			Collections.sort(salesCharts);
			if (salesCharts.size() > 30) {
				return salesCharts.subList(salesCharts.size() - 30,
						salesCharts.size());
			}
			return salesCharts;
		}

		/**
		 * 获取日期数组。
		 * 
		 * @return 日期数组。
		 */
		public Object[] getDays() {
			List<SalesChartModel> models = getSalesCharts();
			String[] days = new String[models.size()];
			for (int i = 0; i < models.size(); i++) {
				days[i] = DateUtils.format(
						DateUtils.parse(models.get(i).getDate()), "M-d");
			}
			return days;
		}

		/**
		 * 获取订单金额数组。
		 * 
		 * @return 订单金额数组。
		 */
		public Object[] getAmounts() {
			List<SalesChartModel> models = getSalesCharts();
			Double[] amounts = new Double[models.size()];
			for (int i = 0; i < models.size(); i++) {
				amounts[i] = NumberUtils.halfUp(models.get(i).getOrderAmount());
			}
			return amounts;
		}

		public void setSalesCharts(List<SalesChartModel> salesCharts) {
			this.salesCharts = salesCharts;
		}
	}

	/**
	 * 销售趋势模型。
	 */
	public class SalesChartModel implements Comparable<SalesChartModel> {
		/** 日期 */
		private String date;
		/** 订单数 */
		private Integer orderCount = 0;
		/** 出票数 */
		private Integer ticketCount = 0;
		/** 订单金额 */
		private Double orderAmount = 0D;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public Integer getOrderCount() {
			return orderCount;
		}

		public void setOrderCount(Integer orderCount) {
			this.orderCount = orderCount;
		}

		public Integer getTicketCount() {
			return ticketCount;
		}

		public void setTicketCount(Integer ticketCount) {
			this.ticketCount = ticketCount;
		}

		public Double getOrderAmount() {
			return orderAmount;
		}

		public void setOrderAmount(Double orderAmount) {
			this.orderAmount = orderAmount;
		}

		@Override
		public int compareTo(SalesChartModel o) {
			return getDate().compareTo(o.getDate());
		}
	}
}
