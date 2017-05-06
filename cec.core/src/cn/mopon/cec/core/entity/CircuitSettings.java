package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import cn.mopon.cec.core.assist.enums.SettleRuleType;
import cn.mopon.cec.core.model.PriceRuleModel;
import coo.core.model.UuidEntity;
import coo.core.security.annotations.LogField;

/**
 * 院线设置。
 */
@Entity
@Table(name = "CEC_CircuitSettings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CircuitSettings extends UuidEntity {
	/** 结算规则类型 */
	@Type(type = "IEnumList")
	@LogField(text = "结算规则类型")
	private List<SettleRuleType> settleRuleTypes = Arrays.asList(SettleRuleType
			.values());
	/** 影片最低价 */
	@Type(type = "Json")
	@LogField(text = "影片最低价")
	private PriceRuleModel defaultMinPrice;
	/** 锁定座位时间（分钟） */
	@LogField(text = "锁定座位时间")
	private Integer lockSeatTime = 15;
	/** 日结时间 */
	@LogField(text = "日结时间")
	private Integer dayStatTime = 6;
	/** 通知用户 */
	@OneToMany(mappedBy = "circuitSettings", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<NoticeUser> noticeUsers = new ArrayList<>();
	/** 排期预警用户 */
	@Transient
	private List<User> showWarnUsers = new ArrayList<>();
	/** 价格预警用户 */
	@Transient
	private List<User> priceWarnUsers = new ArrayList<>();
	/** 订单预警用户 */
	@Transient
	private List<User> orderWarnUsers = new ArrayList<>();
	/** 影片预警用户 */
	@Transient
	private List<User> filmWarnUsers = new ArrayList<>();
	/** 统计通知预警用户 */
	@Transient
	private List<User> statNoticeUsers = new ArrayList<>();

	public List<SettleRuleType> getSettleRuleTypes() {
		return settleRuleTypes;
	}

	public void setSettleRuleTypes(List<SettleRuleType> settleRuleTypes) {
		this.settleRuleTypes = settleRuleTypes;
	}

	public PriceRuleModel getDefaultMinPrice() {
		return defaultMinPrice;
	}

	public void setDefaultMinPrice(PriceRuleModel defaultMinPrice) {
		this.defaultMinPrice = defaultMinPrice;
	}

	public Integer getLockSeatTime() {
		return lockSeatTime;
	}

	public void setLockSeatTime(Integer lockSeatTime) {
		this.lockSeatTime = lockSeatTime;
	}

	public Integer getDayStatTime() {
		return dayStatTime;
	}

	public void setDayStatTime(Integer dayStatTime) {
		this.dayStatTime = dayStatTime;
	}

	public List<NoticeUser> getNoticeUsers() {
		return noticeUsers;
	}

	public void setNoticeUsers(List<NoticeUser> noticeUsers) {
		this.noticeUsers = noticeUsers;
	}

	public List<User> getShowWarnUsers() {
		return showWarnUsers;
	}

	public void setShowWarnUsers(List<User> showWarnUsers) {
		this.showWarnUsers = showWarnUsers;
	}

	public List<User> getPriceWarnUsers() {
		return priceWarnUsers;
	}

	public void setPriceWarnUsers(List<User> priceWarnUsers) {
		this.priceWarnUsers = priceWarnUsers;
	}

	public List<User> getOrderWarnUsers() {
		return orderWarnUsers;
	}

	public void setOrderWarnUsers(List<User> orderWarnUsers) {
		this.orderWarnUsers = orderWarnUsers;
	}

	public List<User> getFilmWarnUsers() {
		return filmWarnUsers;
	}

	public void setFilmWarnUsers(List<User> filmWarnUsers) {
		this.filmWarnUsers = filmWarnUsers;
	}

	public List<User> getStatNoticeUsers() {
		return statNoticeUsers;
	}

	public void setStatNoticeUsers(List<User> statNoticeUsers) {
		this.statNoticeUsers = statNoticeUsers;
	}
}