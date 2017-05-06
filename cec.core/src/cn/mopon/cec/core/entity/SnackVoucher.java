package cn.mopon.cec.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import cn.mopon.cec.core.enums.PrintStatus;
import cn.mopon.cec.core.enums.TicketOrderStatus;
import coo.core.hibernate.search.IEnumValueBridge;

/**
 * 卖品凭证。
 */
@Entity
@Table(name = "CEC_SnackVoucher")
@Indexed(index = "SnackVoucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnackVoucher {
	/** ID */
	@Id
	private String id;
	/** 关联订单 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@IndexedEmbedded(includePaths = { "code" })
	private SnackOrder snackOrder;
	/** 凭证号 */
	@Field(analyze = Analyze.NO)
	private String code;
	/** 打票状态 */
	@Type(type = "IEnum")
	@Field(analyze = Analyze.NO, bridge = @FieldBridge(impl = IEnumValueBridge.class))
	private PrintStatus status = PrintStatus.NO;
	/** 是否可打印 */
	@Field(analyze = Analyze.NO)
	private Boolean printable = true;
	/** 重打印次数 */
	private Integer reprintCount = 0;
	/** 生成时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date genTime;
	/** 确认打票时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date confirmPrintTime;

	/**
	 * 判断凭证是否可以打票。
	 * 
	 * @return 返回凭证是否可以打票。
	 */
	public Boolean isVerifyable() {
		return getSnackOrder().getStatus() == TicketOrderStatus.SUCCESS
				&& printable;
	}

	/**
	 * 判断凭证是否可以更换。
	 * 
	 * @return 返回凭证是否可以更换。
	 */
	public Boolean isChangeable() {
		return getSnackOrder().getStatus() == TicketOrderStatus.SUCCESS
				&& status == PrintStatus.NO;
	}

	/**
	 * 判断凭证是否可以重置。
	 * 
	 * @return 返回凭证是否可以重置。
	 */
	public Boolean isResetable() {
		return getSnackOrder().getStatus() == TicketOrderStatus.SUCCESS
				&& status == PrintStatus.YES && !printable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SnackOrder getSnackOrder() {
		return snackOrder;
	}

	public void setSnackOrder(SnackOrder snackOrder) {
		this.snackOrder = snackOrder;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PrintStatus getStatus() {
		return status;
	}

	public void setStatus(PrintStatus status) {
		this.status = status;
	}

	public Date getGenTime() {
		return genTime;
	}

	public void setGenTime(Date genTime) {
		this.genTime = genTime;
	}

	public Date getConfirmPrintTime() {
		return confirmPrintTime;
	}

	public void setConfirmPrintTime(Date validateTime) {
		this.confirmPrintTime = validateTime;
	}

	public Boolean getPrintable() {
		return printable;
	}

	public void setPrintable(Boolean printable) {
		this.printable = printable;
	}

	public Integer getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(Integer reprintCount) {
		this.reprintCount = reprintCount;
	}
}