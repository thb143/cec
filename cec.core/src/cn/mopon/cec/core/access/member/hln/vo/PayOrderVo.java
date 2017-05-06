package cn.mopon.cec.core.access.member.hln.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 订单支付VO。
 */
@XStreamAlias("PayOrder")
public class PayOrderVo {
	private Integer payType;
	private String account;
	private String sign;
	private String password;
	private String operator;
	private String orderNo;
	@XStreamImplicit
	private List<PayOrderItemVo> items = new ArrayList<PayOrderItemVo>();

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<PayOrderItemVo> getItems() {
		return items;
	}

	public void setItems(List<PayOrderItemVo> items) {
		this.items = items;
	}

}
