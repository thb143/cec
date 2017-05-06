package cn.mopon.cec.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Indexed;

import cn.mopon.cec.core.enums.TicketApiMethod;
import cn.mopon.cec.core.enums.VerifySign;
import coo.base.constants.Chars;
import coo.base.util.StringUtils;
import coo.core.security.annotations.LogField;

/**
 * 渠道设置。
 */
@Entity
@Table(name = "CEC_ChannelSettings")
@Indexed(index = "ChannelSettings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChannelSettings {
	/** ID */
	@Id
	private String id;
	/** 关联渠道 */
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Channel channel;
	/** 接口日志长度 */
	@LogField(text = "日志长度")
	private Integer logLength = 1000;
	/** 开放的票务接口 */
	@Type(type = "IEnumList")
	@LogField(text = "开放的票务接口 ")
	private List<TicketApiMethod> ticketApiMethods = new ArrayList<>();
	/** 停售时间 */
	@LogField(text = "提前停售时间")
	private Integer stopSellTime = 30;
	/** 停退时间 */
	@LogField(text = "提前停退时间")
	private Integer stopRevokeTime = 60;
	/** 接口签名设置 */
	@Type(type = "IEnum")
	@LogField(text = "接口签名设置")
	private VerifySign verifySign = VerifySign.VERIFY;

	/**
	 * 获取可访问接口名称。
	 * 
	 * @return 返回可访问接口名称，以";"分隔。
	 */
	public String getTicketApiMethodsTexts() {
		List<String> methods = new ArrayList<>();
		for (TicketApiMethod apiMethod : ticketApiMethods) {
			methods.add(apiMethod.toString());
		}
		return StringUtils.join(methods, Chars.SEMICOLON);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Integer getLogLength() {
		return logLength;
	}

	public void setLogLength(Integer logLength) {
		this.logLength = logLength;
	}

	public List<TicketApiMethod> getTicketApiMethods() {
		return ticketApiMethods;
	}

	public void setTicketApiMethods(List<TicketApiMethod> ticketApiMethods) {
		this.ticketApiMethods = ticketApiMethods;
	}

	public Integer getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(Integer stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public Integer getStopRevokeTime() {
		return stopRevokeTime;
	}

	public void setStopRevokeTime(Integer stopRevokeTime) {
		this.stopRevokeTime = stopRevokeTime;
	}

	public VerifySign getVerifySign() {
		return verifySign;
	}

	public void setVerifySign(VerifySign verifySign) {
		this.verifySign = verifySign;
	}
}
