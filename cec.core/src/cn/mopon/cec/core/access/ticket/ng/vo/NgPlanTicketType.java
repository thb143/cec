package cn.mopon.cec.core.access.ticket.ng.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 选座票计划排期类型
 */
public class NgPlanTicketType {
	/** 选座票计划代码 */
	private String planCode;
	/** 票价类型 */
	@XStreamAlias("ticketTypes")
	private List<NgTicketType> ticketTypes;

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public List<NgTicketType> getTicketTypes() {
		return ticketTypes;
	}

	public void setTicketTypes(List<NgTicketType> ticketTypes) {
		this.ticketTypes = ticketTypes;
	}
}
