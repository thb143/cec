package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.assist.period.DayPeriodRule;
import cn.mopon.cec.core.assist.period.PeriodRule;
import cn.mopon.cec.core.assist.settle.FixedSettleRule;
import cn.mopon.cec.core.assist.settle.SettleRule;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.SubmitType;
import cn.mopon.cec.core.enums.ValidStatus;
import cn.mopon.cec.core.model.CinemaModel;
import cn.mopon.cec.core.model.HallModel;
import coo.base.util.BeanUtils;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.security.entity.ResourceEntity;

/**
 * 特殊定价规则。
 */
@Entity
@Table(name = "CEC_SpecialRule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecialRule extends ResourceEntity<User> implements
		Comparable<SpecialRule> {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policyId")
	private SpecialPolicy policy;
	/** 名称 */
	private String name;
	/** 放映类型 */
	@Type(type = "IEnum")
	private ShowType showType;
	/** 关联影厅 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_SpecialRule_Hall", joinColumns = @JoinColumn(name = "ruleId"), inverseJoinColumns = @JoinColumn(name = "hallId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Hall> halls = new ArrayList<>();
	/** 关联影片 */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CEC_SpecialRule_Film", joinColumns = @JoinColumn(name = "ruleId"), inverseJoinColumns = @JoinColumn(name = "filmId"))
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Film> films = new ArrayList<>();
	/** 时段规则 */
	@Type(type = "Json")
	private PeriodRule periodRule = new DayPeriodRule();
	/** 结算规则 */
	@Type(type = "Json")
	private SettleRule settleRule = new FixedSettleRule();
	/** 上报类型 */
	@Type(type = "IEnum")
	private SubmitType submitType;
	/** 加减值 */
	private Double amount = 0D;
	/** 生效状态 */
	@Type(type = "IEnum")
	private ValidStatus valid = ValidStatus.UNVALID;
	/** 启用状态 */
	@Type(type = "IEnum")
	private EnabledStatus enabled = EnabledStatus.DISABLED;
	/** 绑定规则 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boundRuleId")
	private SpecialRule boundRule;
	/** 排序 */
	private Integer ordinal = 0;
	/** 关联渠道列表 */
	@OneToMany(mappedBy = "rule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<SpecialChannel> channels = new ArrayList<SpecialChannel>();

	/**
	 * 获取摘要信息。
	 * 
	 * @return 返回摘要信息。
	 */
	public String getSummary() {
		StringBuilder builder = new StringBuilder();
		builder.append(getShowType().getText());
		builder.append(" ");
		builder.append(getSettleRule().toString());
		builder.append("\n");
		builder.append(getCinemasNameText());
		if (!getFilms().isEmpty()) {
			builder.append("\n");
			builder.append(getFilmsNameText());
		}
		builder.append("\n");
		builder.append(getPeriodRule().toString());
		return builder.toString();
	}

	/**
	 * 判断规则是否匹配排期。
	 * 
	 * @param show
	 *            排期
	 * @return 如果匹配排期返回true，否则返回false。
	 */
	public Boolean matchShow(Show show) {
		Boolean matched = valid == ValidStatus.VALID
				&& enabled == EnabledStatus.ENABLED
				&& getShowType() == show.getShowType()
				&& getHalls().contains(show.getHall())
				&& getPeriodRule().contains(show.getShowTime());
		// 如果以上条件匹配并且设置了影片，则判断是否命中影片。
		if (matched && CollectionUtils.isNotEmpty(getFilms())) {
			matched = getFilms().contains(show.getFilm());
		}
		return matched;
	}

	/**
	 * 判断是否包含指定的特殊定价渠道。
	 * 
	 * @param channel
	 *            渠道
	 * @return 返回是否包含指定的特殊定价渠道。
	 */
	public Boolean containsChannel(Channel channel) {
		for (SpecialChannel specialChannel : getChannels()) {
			if (specialChannel.getValid() != ValidStatus.INVALID
					&& specialChannel.getChannel().equals(channel)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取已生效的渠道列表。
	 * 
	 * @return 返回已生效的渠道列表。
	 */
	public List<SpecialChannel> getValidChannels() {
		List<SpecialChannel> channels = new ArrayList<SpecialChannel>();
		for (SpecialChannel channel : getChannels()) {
			if (channel.isValid()) {
				channels.add(channel);
			}
		}
		return channels;
	}

	/**
	 * 获取渠道列表。（不包含原本）
	 * 
	 * @return 返回渠道列表。
	 */
	public List<SpecialChannel> getChannelsExcludeOrig() {
		List<SpecialChannel> channels = new ArrayList<SpecialChannel>();
		for (SpecialChannel channel : getChannels()) {
			if (!channel.isOrig()) {
				channels.add(channel);
			}
		}
		return channels;
	}

	/**
	 * 获取渠道列表。（不包含副本）
	 * 
	 * @return 返回渠道列表。
	 */
	public List<SpecialChannel> getChannelsExcludeCopy() {
		List<SpecialChannel> channels = new ArrayList<SpecialChannel>();
		for (SpecialChannel channel : isBounded() ? boundRule.getChannels()
				: getChannels()) {
			if (!channel.isCopy() && channel.getValid() != ValidStatus.INVALID) {
				channels.add(channel);
			}
		}
		return channels;
	}

	/**
	 * 复制规则。
	 * 
	 * @param origRule
	 *            原规则
	 * @param toPolicy
	 *            复制到目标策略
	 * @return 返回复制的规则。
	 */
	public static SpecialRule copy(SpecialRule origRule, SpecialPolicy toPolicy) {
		SpecialRule rule = new SpecialRule();
		rule.setName(origRule.getName());
		BeanUtils.copyFields(origRule, rule, "boundRule,status");
		rule.setId(null);
		rule.setPeriodRule(origRule.getPeriodRule());
		rule.setSettleRule(origRule.getSettleRule());
		rule.setShowType(origRule.getShowType());
		rule.setAmount(origRule.getAmount());
		rule.setValid(ValidStatus.UNVALID);
		rule.setEnabled(EnabledStatus.DISABLED);
		rule.autoFillIn();
		rule.setPolicy(toPolicy);
		toPolicy.getRules().add(rule);
		// 如果是复制到同一个策略，则设置序号为新的序号。
		if (origRule.getPolicy().equals(toPolicy)) {
			rule.setOrdinal(toPolicy.genNewRuleOrdinal());
			rule.setName(origRule.getName() + "-副本");
		}
		for (SpecialChannel origChannel : origRule.getChannels()) {
			// 复制已生效未绑定、未生效的渠道。
			if ((origChannel.getValid() == ValidStatus.VALID && !origChannel
					.isBounded())
					|| origChannel.getValid() == ValidStatus.UNVALID) {
				SpecialChannel channel = SpecialChannel.copy(origChannel, rule);
				rule.getChannels().add(channel);
			}
		}
		return rule;
	}

	/**
	 * 获取关联影片文本。
	 * 
	 * @return 返回关联影片文本。
	 */
	public String getFilmsNameText() {
		List<String> filmsNameText = new ArrayList<String>();
		for (Film film : getFilms()) {
			filmsNameText.add(film.getName());
		}
		return StringUtils.join(filmsNameText, " | ");
	}

	/**
	 * 获取影院、影厅信息。
	 * 
	 * @return 返回影院、影厅信息。
	 */
	public String getCinemasNameText() {
		List<String> summarys = new ArrayList<>();
		for (CinemaModel model : getCinemaList()) {
			summarys.add(model.getSummary());
		}
		return StringUtils.join(summarys, "\n");
	}

	/**
	 * 获取关联影院集合。
	 * 
	 * @return 返回关联影院集合。
	 */
	public List<CinemaModel> getCinemaList() {
		List<CinemaModel> cinemaModels = new ArrayList<CinemaModel>();
		Map<Cinema, List<Hall>> map = new HashMap<Cinema, List<Hall>>();
		List<Hall> halls = null;
		for (Hall hall : getHalls()) {
			if (!map.containsKey(hall.getCinema())) {
				halls = new ArrayList<Hall>();
				halls.add(hall);
				map.put(hall.getCinema(), halls);
			} else {
				map.get(hall.getCinema()).add(hall);
			}
		}
		Iterator<Entry<Cinema, List<Hall>>> iter = map.entrySet().iterator();
		Entry<Cinema, List<Hall>> entry = null;
		while (iter.hasNext()) {
			entry = iter.next();
			CinemaModel cinemaModel = new CinemaModel();
			cinemaModel.setName(entry.getKey().getName());
			cinemaModel.setHalls(entry.getValue());
			cinemaModels.add(cinemaModel);
		}
		return cinemaModels;
	}

	/**
	 * 获取非失效的渠道。
	 * 
	 * @return 返回非失效的渠道列表。
	 */
	public List<SpecialChannel> getChannelsExcludeInvalid() {
		List<SpecialChannel> specialChannels = new ArrayList<>();
		// 返回已生效未绑定且为绑定的渠道。
		for (SpecialChannel channel : getChannels()) {
			if ((channel.getValid() == ValidStatus.VALID && !channel
					.isBounded()) || channel.getValid() == ValidStatus.UNVALID) {
				specialChannels.add(channel);
			}
		}
		return specialChannels;
	}

	/**
	 * 获取影厅模型。
	 * 
	 * @return 返回影厅模型。
	 */
	public List<HallModel> getHallModels() {
		Map<Cinema, List<Hall>> map = new HashMap<Cinema, List<Hall>>();
		List<Hall> halls = null;
		for (Hall hall : getHalls()) {
			if (!map.containsKey(hall.getCinema())) {
				halls = new ArrayList<Hall>();
				halls.add(hall);
				map.put(hall.getCinema(), halls);
			} else {
				map.get(hall.getCinema()).add(hall);
			}
		}
		List<HallModel> hallModelList = new ArrayList<HallModel>();
		for (Entry<Cinema, List<Hall>> entry : map.entrySet()) {
			HallModel hallModel = new HallModel();
			hallModel.setCinemaName(entry.getKey().getName());
			StringBuilder hallNames = new StringBuilder();
			StringBuilder hallIds = new StringBuilder();
			for (Hall hall : entry.getValue()) {
				hallNames.append(hall.getFullName()).append(",");
				hallIds.append(hall.getId()).append(",");
			}
			hallModel.setHallId(hallIds.toString());
			hallModel.setHallName(hallNames.toString());
			hallModelList.add(hallModel);
		}
		return hallModelList;
	}

	/**
	 * 判断是否已被绑定（在生效状态下被修改生成了副本）。
	 * 
	 * @return 如果被绑定返回true，否则返回false。
	 */
	public Boolean isBounded() {
		return boundRule != null;
	}

	/**
	 * 判断是否原本。
	 * 
	 * @return 如果是原本返回true，否则返回false。
	 */
	public Boolean isOrig() {
		return boundRule != null && valid == ValidStatus.VALID;
	}

	/**
	 * 判断是否可以编辑。
	 * 
	 * @return 如果可以编辑，返回true，否则返回false。
	 */
	public boolean isEdit() {
		// 判断是否包含未生效的渠道。
		Boolean containsUnvalidChannel = false;
		for (SpecialChannel channel : getChannels()) {
			if (channel.getValid() == ValidStatus.UNVALID) {
				containsUnvalidChannel = true;
				break;
			}
		}
		// 如果是未生效或者已生效不包含未生效渠道的规则允许编辑。
		return valid == ValidStatus.UNVALID
				|| (valid == ValidStatus.VALID && !containsUnvalidChannel);
	}

	/**
	 * 判断是否副本。
	 * 
	 * @return 如果是副本返回true，否则返回false。
	 */
	public Boolean isCopy() {
		return boundRule != null && valid == ValidStatus.UNVALID;
	}

	/**
	 * 判断是否允许新增渠道。
	 * 
	 * @return 返回是否允许新增渠道。
	 */
	public Boolean isAddChannel() {
		List<String> policyChannelIds = new ArrayList<>();
		for (Channel channel : policy.getChannels()) {
			policyChannelIds.add(channel.getId());
		}
		List<String> channelIds = new ArrayList<>();
		for (SpecialChannel channel : getChannelsExcludeInvalid()) {
			channelIds.add(channel.getChannel().getId());
		}
		return !channelIds.equals(policyChannelIds);
	}

	@Override
	public int compareTo(SpecialRule other) {
		int order = (showType.getValue() + periodRule.getType().getValue())
				.compareTo(other.getShowType().getValue()
						+ other.getPeriodRule().getType().getValue());
		// 同种放映类型、同种时段，按创建时间再次排序。
		if (order == 0) {
			return other.getCreateDate().compareTo(getCreateDate());
		}
		return order;
	}

	public SpecialPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(SpecialPolicy policy) {
		this.policy = policy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShowType getShowType() {
		return showType;
	}

	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	public List<Hall> getHalls() {
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	public List<Film> getFilms() {
		return films;
	}

	public void setFilms(List<Film> films) {
		this.films = films;
	}

	public PeriodRule getPeriodRule() {
		return periodRule;
	}

	public void setPeriodRule(PeriodRule periodRule) {
		this.periodRule = periodRule;
	}

	public SettleRule getSettleRule() {
		return settleRule;
	}

	public void setSettleRule(SettleRule settleRule) {
		this.settleRule = settleRule;
	}

	public SubmitType getSubmitType() {
		return submitType;
	}

	public void setSubmitType(SubmitType submitType) {
		this.submitType = submitType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ValidStatus getValid() {
		return valid;
	}

	public void setValid(ValidStatus valid) {
		this.valid = valid;
	}

	public EnabledStatus getEnabled() {
		return enabled;
	}

	public void setEnabled(EnabledStatus enabled) {
		this.enabled = enabled;
	}

	public SpecialRule getBoundRule() {
		return boundRule;
	}

	public void setBoundRule(SpecialRule boundRule) {
		this.boundRule = boundRule;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * 获取渠道。
	 * 
	 * @return 返回排序后的渠道列表。
	 */
	public List<SpecialChannel> getChannels() {
		Collections.sort(channels);
		return channels;
	}

	public void setChannels(List<SpecialChannel> channels) {
		this.channels = channels;
	}
}
