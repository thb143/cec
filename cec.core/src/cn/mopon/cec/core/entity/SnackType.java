package cn.mopon.cec.core.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import coo.core.security.annotations.LogField;
import coo.core.security.entity.ResourceEntity;

/**
 * 卖品类型。
 */
@Entity
@Table(name = "CEC_SnackType")
@Indexed(index = "SnackType")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnackType extends ResourceEntity<User> {
	/** 关联类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId")
	private SnackGroup group;
	/** 名称 */
	@LogField(text = "卖品类型名称")
	@Field(analyze = Analyze.NO)
	private String name;
	/** 图片 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image")
	private Attachment image;
	/** 备注 */
	@LogField(text = "备注")
	@Field(analyze = Analyze.NO)
	private String remark;

	public SnackGroup getGroup() {
		return group;
	}

	public void setGroup(SnackGroup group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Attachment getImage() {
		return image;
	}

	public void setImage(Attachment image) {
		this.image = image;
	}
}