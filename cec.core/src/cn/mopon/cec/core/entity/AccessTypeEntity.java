package cn.mopon.cec.core.entity;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import cn.mopon.cec.core.enums.AccessModel;
import cn.mopon.cec.core.enums.Provider;
import coo.base.model.Params;
import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 接入类型基类。
 */
@MappedSuperclass
public abstract class AccessTypeEntity extends ResourceEntity<User> {
	@LogField(text = "接入类型名称")
	private String name;
	/** 接入商 */
	@Type(type = "IEnum")
	@LogField(text = "接入商")
	private Provider provider;
	/** 接入模式 */
	@Type(type = "IEnum")
	@LogField(text = "接入模式")
	private AccessModel model;
	/** 接入地址 */
	@LogField(text = "接入地址")
	private String url;
	/** 用户名 */
	@LogField(text = "用户名 ")
	private String username;
	/** 密码 */
	@LogField(text = "密码")
	private String password;
	/** 响应超时 */
	@LogField(text = "响应超时")
	private Integer socketTimeout = 10;
	/** 请求超时 */
	@LogField(text = "请求超时")
	private Integer connectTimeout = 5;
	/** 参数配置 */
	@LogField(text = "参数配置")
	@Type(type = "Params")
	private Params params;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider providerType) {
		this.provider = providerType;
	}

	public AccessModel getModel() {
		return model;
	}

	public void setModel(AccessModel model) {
		this.model = model;
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

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

}