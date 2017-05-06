package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import cn.mopon.cec.core.assist.district.County;
import cn.mopon.cec.core.enums.EnabledStatus;
import cn.mopon.cec.core.enums.HallStatus;
import cn.mopon.cec.core.enums.Provider;
import cn.mopon.cec.core.enums.ShowType;
import cn.mopon.cec.core.enums.SnackStatus;
import cn.mopon.cec.core.enums.ValidStatus;
import coo.core.hibernate.search.IEnumTextBridge;
import coo.core.hibernate.search.IEnumValueBridge;
import coo.core.security.annotations.LogBean;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 影院。
 */
@Entity
@Table(name = "CEC_Cinema")
@Indexed(index = "Cinema")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cinema extends ResourceEntity<User> implements Comparable<Cinema> {
	/** 编码 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "影院编码")
	private String code;
	/** 名称 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "影院名称")
	private String name;
	/** 接入商 */
	@Type(type = "IEnum")
	@LogField(text = "接入商")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumTextBridge.class))
	private Provider provider;
	/** 影厅数量 */
	private Integer hallCount = 0;
	/** 所在辖区 */
	@Type(type = "District")
	@IndexedEmbedded
	private County county;
	/** 地址 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "地址")
	private String address;
	/** LOGO */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logo")
	private Attachment logo;
	/** 网址 */
	@LogField(text = "网址")
	private String url;
	/** 客服电话 */
	@LogField(text = "客服电话")
	private String tel;
	/** 终端位置说明 */
	@LogField(text = "终端位置说明")
	private String devicePos;
	/** 终端位置图片 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceImg")
	private Attachment deviceImg;
	/** 综合评分 */
	private Double grade = 8.0D;
	/** 简介 */
	private String intro;
	/** 公交路线 */
	private String busLine;
	/** 经度 */
	private String longitude;
	/** 纬度 */
	private String latitude;
	/** 特色 */
	private String feature;
	/** 排序 */
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IntegerBridge.class))
	private Integer ordinal = 0;
	/** 状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private EnabledStatus status = EnabledStatus.ENABLED;
	/** 可售状态 */
	@Field(analyze = Analyze.NO)
	private Boolean salable = true;
	/** 选座票设置状态 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "选座票设置状态")
	private Boolean ticketSetted = false;
	/** 会员卡设置状态 */
	@Field(analyze = Analyze.NO)
	@LogField(text = "会员卡设置状态")
	private Boolean memberSetted = false;
	/** 选座票接入设置 */
	@OneToOne(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@LogBean({ @LogField(text = "放映类型", property = "showTypes"),
			@LogField(text = "选座票接入类型", property = "accessType.name"),
			@LogField(text = "同步排期天数", property = "syncShowDays"),
			@LogField(text = "保留排期天数", property = "keepShowDays"),
			@LogField(text = "生成凭证长度", property = "voucherCodeLength"),
			@LogField(text = "选座票接入日志长度", property = "logLength"),
			@LogField(text = "同步排期间隔", property = "syncShowInterval"),
			@LogField(text = "选座票接入地址", property = "url"),
			@LogField(text = "选座票接入用户名", property = "username"),
			@LogField(text = "选座票接入密码", property = "password") })
	private TicketSettings ticketSettings;
	/** 会员卡接入设置 */
	@OneToOne(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@LogBean({ @LogField(text = "会员接入类型", property = "accessType.name"),
			@LogField(text = "会员接入日志长度", property = "logLength"),
			@LogField(text = "会员接入地址", property = "url"),
			@LogField(text = "会员接入用户名", property = "username"),
			@LogField(text = "会员接入密码", property = "password") })
	private MemberSettings memberSettings;
	/** 关联影厅 */
	@OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Hall> halls = new ArrayList<>();
	/** 关联影院结算策略 */
	@OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<CinemaPolicy> policys = new ArrayList<>();
	/** 关联卖品列表 */
	@OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Snack> snacks = new ArrayList<>();

	/**
	 * 获取选座设置中的扩展参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return 返回选座设置中的扩展参数值。
	 */
	public String getTicketSettingsParams(String paramName) {
		if (getTicketSettings() != null) {
			return getTicketSettings().getParams().get(paramName);
		}
		return null;
	}

	/**
	 * 获取会员设置中的扩展参数。
	 * 
	 * @param paramName
	 *            参数名
	 * @return 返回选座设置中的扩展参数值。
	 */
	public String getMemberSettingsParams(String paramName) {
		if (getMemberSettings() != null && getTicketSettings() != null) {
			return getTicketSettings().getParams().get(paramName);
		}
		return null;
	}

	/**
	 * 获取匹配排期的影院结算规则。
	 * 
	 * @param show
	 *            排期
	 * @return 返回匹配排期的影院结算规则。
	 */
	public CinemaRule getMatchedRule(Show show) {
		if (show.isValid()) {
			for (CinemaPolicy policy : getPolicys()) {
				CinemaRule rule = policy.getMatchedRule(show);
				if (rule != null) {
					return rule;
				}
			}
		}
		return null;
	}

	/**
	 * 生成新的策略排序号。
	 * 
	 * @return 返回新的策略排序号。
	 */
	public Integer genNewPolicyOrdinal() {
		if (!getPolicys().isEmpty()) {
			return getPolicys().get(0).getOrdinal() + 1;
		} else {
			return 1;
		}
	}

	/**
	 * 获取指定编码的影厅。
	 * 
	 * @param hallCode
	 *            影厅编码
	 * @return 返回指定的影厅。
	 */
	public Hall getHall(String hallCode) {
		for (Hall hall : getHalls()) {
			if (hall.getCode().equals(hallCode)) {
				return hall;
			}
		}
		return null;
	}

	/**
	 * 获取已启用的影厅列表。
	 * 
	 * @return 返回已启用的影厅。
	 */
	public List<Hall> getEnableHalls() {
		List<Hall> halls = new ArrayList<>();
		for (Hall hall : getHalls()) {
			if (hall.getStatus() == HallStatus.ENABLED) {
				halls.add(hall);
			}
		}
		return halls;
	}

	/**
	 * 获取已上架的卖品列表。
	 * 
	 * @return 返回已上架的卖品。
	 */
	public List<Snack> getOnSnacks() {
		List<Snack> snacks = new ArrayList<>();
		for (Snack snack : getSnacks()) {
			if (snack.getStatus() == SnackStatus.ON) {
				snacks.add(snack);
			}
		}
		return snacks;
	}

	/**
	 * 获取有效的影厅列表。
	 * 
	 * @return 返回有效的影厅列表。
	 */
	public List<Hall> getValidHalls() {
		List<Hall> halls = new ArrayList<Hall>();
		for (Hall hall : getHalls()) {
			if (hall.getStatus() != HallStatus.DELETE) {
				halls.add(hall);
			}
		}
		return halls;
	}

	/**
	 * 获取非失效的策略列表。
	 * 
	 * @return 返回非失效的策略列表。
	 */
	public List<CinemaPolicy> getValidPolicys() {
		List<CinemaPolicy> cinemaPolicies = new ArrayList<>();
		for (CinemaPolicy policy : getPolicys()) {
			if (policy.getValid() != ValidStatus.INVALID) {
				cinemaPolicies.add(policy);
			}
		}
		return cinemaPolicies;
	}

	/**
	 * 判断是否提供指定放映类型的选座票。
	 * 
	 * @param showType
	 *            放映类型
	 * @return 如果提供指定放映类型的选座票返回true，否则返回false。
	 */
	public Boolean provideShowType(ShowType showType) {
		return ticketSetted
				&& getTicketSettings().getShowTypes().contains(showType);
	}

	@Override
	public int compareTo(Cinema o) {
		return this.getCounty().getCity().getCode()
				.compareTo(o.getCounty().getCity().getCode());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Integer getHallCount() {
		return hallCount;
	}

	public void setHallCount(Integer hallCount) {
		this.hallCount = hallCount;
	}

	public County getCounty() {
		return county;
	}

	public void setCounty(County county) {
		this.county = county;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Attachment getLogo() {
		return logo;
	}

	public void setLogo(Attachment logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDevicePos() {
		return devicePos;
	}

	public void setDevicePos(String devicePos) {
		this.devicePos = devicePos;
	}

	public Attachment getDeviceImg() {
		return deviceImg;
	}

	public void setDeviceImg(Attachment deviceImg) {
		this.deviceImg = deviceImg;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getBusLine() {
		return busLine;
	}

	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public EnabledStatus getStatus() {
		return status;
	}

	public void setStatus(EnabledStatus status) {
		this.status = status;
	}

	public Boolean getSalable() {
		return salable;
	}

	public void setSalable(Boolean salable) {
		this.salable = salable;
	}

	public Boolean getTicketSetted() {
		return ticketSetted;
	}

	public void setTicketSetted(Boolean ticketSetted) {
		this.ticketSetted = ticketSetted;
	}

	public Boolean getMemberSetted() {
		return memberSetted;
	}

	public void setMemberSetted(Boolean memberSetted) {
		this.memberSetted = memberSetted;
	}

	public TicketSettings getTicketSettings() {
		return ticketSettings;
	}

	public void setTicketSettings(TicketSettings ticketSettings) {
		this.ticketSettings = ticketSettings;
	}

	public MemberSettings getMemberSettings() {
		return memberSettings;
	}

	public void setMemberSettings(MemberSettings memberSettings) {
		this.memberSettings = memberSettings;
	}

	/**
	 * 获取影厅列表。
	 * 
	 * @return 返回排序后的影厅列表。
	 */
	public List<Hall> getHalls() {
		Collections.sort(halls);
		return halls;
	}

	public void setHalls(List<Hall> halls) {
		this.halls = halls;
	}

	/**
	 * 获取策略列表。
	 * 
	 * @return 返回排序后的策略列表。
	 */
	public List<CinemaPolicy> getPolicys() {
		Collections.sort(policys);
		return policys;
	}

	public void setPolicys(List<CinemaPolicy> policys) {
		this.policys = policys;
	}

	public List<Snack> getSnacks() {
		return snacks;
	}

	public void setSnacks(List<Snack> snacks) {
		this.snacks = snacks;
	}
}
