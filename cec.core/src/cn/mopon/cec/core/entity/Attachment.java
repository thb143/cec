package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import coo.core.model.UuidEntity;

/**
 * 附件表。
 */
@Entity
@Table(name = "CEC_Attachment")
public class Attachment extends UuidEntity {
	/** 名称 */
	private String name;
	/** 路径 */
	private String path;
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
