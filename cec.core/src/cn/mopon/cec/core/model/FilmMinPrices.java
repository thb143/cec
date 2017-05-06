package cn.mopon.cec.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 影片最低价模型。
 */
@SuppressWarnings("serial")
public class FilmMinPrices implements Serializable {
	private List<FilmMinPriceItem> items = new ArrayList<>();

	public List<FilmMinPriceItem> getItems() {
		return items;
	}

	public void setItems(List<FilmMinPriceItem> items) {
		this.items = items;
	}

}
