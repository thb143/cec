package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import coo.base.model.Params;

/**
 * 会员卡接入设置。
 */
@Entity
@Table(name = "CEC_MemberSettings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberSettings {
	/** ID */
	@Id
	private String id;
	/** 关联影院 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Cinema cinema;
	/** 关联会员接入类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accessTypeId")
	private MemberAccessType accessType;
	/** 接口日志长度 */
	private Integer logLength = 1000;
	/** 接入地址 */
	private String url;
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
	/** 参数配置 */
	@Type(type = "Params")
	private Params params;

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

	public MemberAccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(MemberAccessType accessType) {
		this.accessType = accessType;
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
}