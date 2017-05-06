package cn.mopon.cec.core.access.film.csp;

/**
 * 预告片信息。
 */
public class TrailerVo {
	/** 标题 */
	private String name;
	/** 图片路径 */
	private String imageUrl;
	/** 视频路径 */
	private String videoUrl;

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

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}
