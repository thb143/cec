package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;

import coo.core.security.entity.UserSettingsEntity;

/**
 * 用户设置。
 */
@Entity
@Table(name = "CEC_UserSettings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserSettings extends UserSettingsEntity<Actor> {
	/** 邮箱 */
	@Field(analyze = Analyze.NO)
	private String email;
	/** 手机 */
	@Field(analyze = Analyze.NO)
	private String phone;
	/** 是否接收邮件 */
	private Boolean receiveEmail = true;
	/** 是否接收短信 */
	private Boolean receiveSms = false;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getReceiveEmail() {
		return receiveEmail;
	}

	public void setReceiveEmail(Boolean receiveEmail) {
		this.receiveEmail = receiveEmail;
	}

	public Boolean getReceiveSms() {
		return receiveSms;
	}

	public void setReceiveSms(Boolean receiveSms) {
		this.receiveSms = receiveSms;
	}
}
