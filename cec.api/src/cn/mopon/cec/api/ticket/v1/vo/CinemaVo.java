package cn.mopon.cec.api.ticket.v1.vo;

import cn.mopon.cec.core.entity.Cinema;
import cn.mopon.cec.core.enums.PrintMode;
import cn.mopon.cec.core.enums.Provider;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import coo.core.xstream.IEnumConverter;

/**
 * 影院。
 */
@XStreamAlias("cinema")
public class CinemaVo {
	/** 编码 */
	private String code = "";
	/** 名称 */
	private String name = "";
	/** 影厅数量 */
	private Integer hallCount = 0;
	/** 省份 */
	private String province = "";
	/** 省份编码 */
	private String provinceCode = "";
	/** 城市 */
	private String city = "";
	/** 城市编码 */
	private String cityCode = "";
	/** 辖区 */
	private String county = "";
	/** 辖区编码 */
	private String countyCode = "";
	/** 地址 */
	private String address = "";
	/** LOGO */
	private String logo = "";
	/** 网址 */
	private String url = "";
	/** 客服电话 */
	private String tel = "";
	/** 终端位置说明 */
	private String devicePos = "";
	/** 终端位置图片 */
	private String deviceImg = "";
	/** 综合评分 */
	private Double grade = 0D;
	/** 简介 */
	private String intro = "";
	/** 公交路线 */
	private String busLine = "";
	/** 经度 */
	private String longitude = "";
	/** 纬度 */
	private String latitude = "";
	/** 特色 */
	private String feature = "";
	/** 接入商 */
	@XStreamConverter(value = IEnumConverter.class, types = Provider.class)
	private Provider provider;
	/** 取票方式 */
	@XStreamConverter(value = IEnumConverter.class, types = PrintMode.class)
	private PrintMode printMode;

	/**
	 * 构造方法。
	 * 
	 * @param cinema
	 *            影院
	 * @param path
	 *            服务器图片路径
	 */
	public CinemaVo(Cinema cinema, String path) {
		path = path.endsWith("/") ? path : path + "/";
		code = cinema.getCode();
		name = cinema.getName();
		hallCount = cinema.getHallCount();
		address = cinema.getAddress();
		url = cinema.getUrl();
		tel = cinema.getTel();
		devicePos = cinema.getDevicePos();
		grade = cinema.getGrade();
		intro = cinema.getIntro();
		busLine = cinema.getBusLine();
		longitude = cinema.getLongitude();
		latitude = cinema.getLatitude();
		feature = cinema.getFeature();
		if (cinema.getDeviceImg() != null) {
			deviceImg = path + cinema.getDeviceImg().getPath();
		}
		if (cinema.getLogo() != null) {
			logo = path + cinema.getLogo().getPath();
		}
		province = cinema.getCounty().getCity().getProvince().getName();
		provinceCode = cinema.getCounty().getCity().getProvince().getCode();
		city = cinema.getCounty().getCity().getName();
		cityCode = cinema.getCounty().getCity().getCode();
		county = cinema.getCounty().getName();
		countyCode = cinema.getCounty().getCode();
		provider = cinema.getProvider();
		printMode = cinema.getTicketSettings().getPrintMode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHallCount() {
		return hallCount;
	}

	public void setHallCount(Integer hallCount) {
		this.hallCount = hallCount;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDevicePos() {
		return devicePos;
	}

	public void setDevicePos(String devicePos) {
		this.devicePos = devicePos;
	}

	public String getDeviceImg() {
		return deviceImg;
	}

	public void setDeviceImg(String deviceImg) {
		this.deviceImg = deviceImg;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String region) {
		this.county = region;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String regionCode) {
		this.countyCode = regionCode;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getBusLine() {
		return busLine;
	}

	public void setBusLine(String busLine) {
		this.busLine = busLine;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public PrintMode getPrintMode() {
		return printMode;
	}

	public void setPrintMode(PrintMode printMode) {
		this.printMode = printMode;
	}
}