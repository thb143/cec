package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 卖品分类。
 */
@Entity
@Table(name = "CEC_SnackGroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnackGroup extends ResourceEntity<User> {
	/** 名称 */
	@LogField(text = "卖品分类名称")
	private String name;
	/** 关联卖品类型 */
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy("createDate desc")
	private List<SnackType> snackTypes = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SnackType> getSnackTypes() {
		return snackTypes;
	}

	public void setSnackTypes(List<SnackType> snackTypes) {
		this.snackTypes = snackTypes;
	}
}