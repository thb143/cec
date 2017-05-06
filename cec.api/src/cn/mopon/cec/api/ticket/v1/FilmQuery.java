package cn.mopon.cec.api.ticket.v1;

import org.hibernate.validator.constraints.NotBlank;

import cn.mopon.cec.api.ApiQuery;

/**
 * 查询影片请求对象。
 */
public class FilmQuery extends ApiQuery {
	/** 影片编码 */
	@NotBlank(message = "影片编码不能为空。")
	private String filmCode;

	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}
}