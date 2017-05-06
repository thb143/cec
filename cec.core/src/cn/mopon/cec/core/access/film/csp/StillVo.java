package cn.mopon.cec.core.access.film.csp;

/**
 * 剧照信息。
 */
public class StillVo {
	/** 标题 */
	private String name;
	/** 图片路径 */
	private String imageUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
