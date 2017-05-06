package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import coo.core.hibernate.search.DateBridge;
import coo.core.model.UuidEntity;

/**
 * 权益卡用户。
 */
@Entity
@Table(name = "CEC_BenefitCardUser")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "BenefitCardUser")
public class BenefitCardUser extends UuidEntity {
	/** 关联渠道 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channelId")
	@IndexedEmbedded(includePaths = { "id" })
	private Channel channel;
	/** 手机号码 */
	@Field(analyze = Analyze.NO)
	private String mobile;
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = DateBridge.class))
	private Date createDate;
	/** 权益卡列表 */
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Cache(region = "Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@ContainedIn
	private List<BenefitCard> cards = new ArrayList<BenefitCard>();

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<BenefitCard> getCards() {
		return cards;
	}

	public void setCards(List<BenefitCard> cards) {
		this.cards = cards;
	}
}
