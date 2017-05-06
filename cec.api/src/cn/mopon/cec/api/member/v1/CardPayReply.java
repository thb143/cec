package cn.mopon.cec.api.member.v1;

import cn.mopon.cec.api.ApiReply;
import cn.mopon.cec.core.enums.PrintMode;
import cn.mopon.cec.core.enums.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 会员卡支付响应对象。
 */
@JsonInclude(Include.NON_NULL)
public class CardPayReply extends ApiReply {
	/** 取票方式 */
	@XStreamConverter(value = IEnumConverter.class, types = PrintMode.class)
	private PrintMode printMode;
	/** 接入商 */
	@XStreamConverter(value = IEnumConverter.class, types = Provider.class)
	private Provider provider;
	/** 凭证编码 */
	private String voucherCode;
	/** 取票号 */
	private String printCode;
	/** 取票验证码 */
	private String verifyCode;

	/**
	 * 构造方法。
	 * 
	 */
	public CardPayReply() {
		super();
	}

	public PrintMode getPrintMode() {
		return printMode;
	}

	public void setPrintMode(PrintMode printMode) {
		this.printMode = printMode;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

}
