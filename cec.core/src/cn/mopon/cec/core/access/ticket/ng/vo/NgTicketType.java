package cn.mopon.cec.core.access.ticket.ng.vo;

/**
 * 
 * 票类型vo
 * 
 */
public class NgTicketType {
	/** 票价类型编码 */

	private String priceTypeCode;
	/** 票价类型名称 */

	private String priceName;
	/** 票价 */

	private String price;

	public String getPriceTypeCode() {
		return priceTypeCode;
	}

	public void setPriceTypeCode(String priceTypeCode) {
		this.priceTypeCode = priceTypeCode;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
