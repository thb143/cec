package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import cn.mopon.cec.core.enums.PrintMode;
import cn.mopon.cec.core.enums.ShowType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import coo.base.model.Params;
import coo.base.util.StringUtils;

/**
 * 选座票接入设置。
 */
@Entity
@Table(name = "CEC_TicketSettings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TicketSettings {
	/** ID */
	@Id
	private String id;
	/** 关联影院 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Cinema cinema;
	/** 关联选座票接入类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accessTypeId")
	private TicketAccessType accessType;
	/** 放映类型 */
	@Type(type = "IEnumList")
	private List<ShowType> showTypes = new ArrayList<ShowType>();
	/** 取票方式 */
	@Type(type = "IEnum")
	private PrintMode printMode = PrintMode.PRINT;
	/** 凭证编码长度 */
	private Integer voucherCodeLength = 6;
	/** 同步排期天数 */
	private Integer syncShowDays = 3;
	/** 保留排期天数 */
	private Integer keepShowDays = 7;
	/** 接口日志长度 */
	private Integer logLength = 1000;
	/** 接入地址 */
	private String url;
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
	/** 同步排期间隔时间 */
	private Integer syncShowInterval = 30;
	/** 最后同步排期时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastSyncShowTime = new Date();
	/** 参数配置 */
	@Type(type = "Params")
	private Params params;

	/**
	 * 判断是否需要同步排期。
	 * 
	 * @return 返回是否需要同步排期。
	 */
	public Boolean isNeedSyncShows() {
		return lastSyncShowTime != null
				&& new DateTime(lastSyncShowTime).plusMinutes(syncShowInterval)
						.isBeforeNow();
	}

	/**
	 * 获取放映类型文本。
	 * 
	 * @return 返回放映类型文本。
	 */
	@JsonIgnore
	public String getShowTypesText() {
		List<String> showTypeNames = new ArrayList<String>();
		for (ShowType showType : showTypes) {
			showTypeNames.add(showType.getText());
		}
		return StringUtils.join(showTypeNames, ",");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public TicketAccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(TicketAccessType accessType) {
		this.accessType = accessType;
	}

	public List<ShowType> getShowTypes() {
		return showTypes;
	}

	public void setShowTypes(List<ShowType> showTypes) {
		this.showTypes = showTypes;
	}

	public PrintMode getPrintMode() {
		return printMode;
	}

	public void setPrintMode(PrintMode printMode) {
		this.printMode = printMode;
	}

	public Integer getVoucherCodeLength() {
		return voucherCodeLength;
	}

	public void setVoucherCodeLength(Integer voucherCodeLength) {
		this.voucherCodeLength = voucherCodeLength;
	}

	public Integer getSyncShowDays() {
		return syncShowDays;
	}

	public void setSyncShowDays(Integer syncShowDays) {
		this.syncShowDays = syncShowDays;
	}

	public Integer getKeepShowDays() {
		return keepShowDays;
	}

	public void setKeepShowDays(Integer keepShowDays) {
		this.keepShowDays = keepShowDays;
	}

	public Integer getLogLength() {
		return logLength;
	}

	public void setLogLength(Integer logLength) {
		this.logLength = logLength;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public Integer getSyncShowInterval() {
		return syncShowInterval;
	}

	public void setSyncShowInterval(Integer syncShowInterval) {
		this.syncShowInterval = syncShowInterval;
	}

	public Date getLastSyncShowTime() {
		return lastSyncShowTime;
	}

	public void setLastSyncShowTime(Date lastSyncShowTime) {
		this.lastSyncShowTime = lastSyncShowTime;
	}
}
