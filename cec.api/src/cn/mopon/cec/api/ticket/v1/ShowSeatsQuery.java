package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;
import cn.mopon.cec.core.enums.SellStatus;

/**
 * 查询场次座位请求对象。
 */
public class ShowSeatsQuery extends ApiQuery {
	/** 渠道场次编码 */
	@NotBlank(message = "渠道场次编码不能为空。")
	private String channelShowCode;
	/** 座位状态 */
	private SellStatus status;

	public String getChannelShowCode() {
		return channelShowCode;
	}

	public void setChannelShowCode(String productCode) {
		this.channelShowCode = productCode;
	}

	public SellStatus getStatus() {
		return status;
	}

	public void setStatus(SellStatus status) {
		this.status = status;
	}
}