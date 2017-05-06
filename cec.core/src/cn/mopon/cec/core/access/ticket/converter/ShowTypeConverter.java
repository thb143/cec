package cn.mopon.cec.core.access.ticket.converter;

import cn.mopon.cec.core.enums.ShowType;
import coo.base.util.StringUtils;

/**
 * 放映类型转换器。
 */
public class ShowTypeConverter {
	/**
	 * 根据影片编码获取放映类型。
	 * 
	 * @param filmCode
	 *            影片编码
	 * @return 返回放映类型。
	 */
	public static ShowType getShowType(String filmCode) {
		if (StringUtils.isNotBlank(filmCode) && filmCode.length() > 4) {
			char showTypeChar = filmCode.charAt(3);
			if (showTypeChar == '1') {
				return ShowType.NORMAL2D;
			}
			if (showTypeChar == '2') {
				return ShowType.NORMAL3D;
			}
			if (showTypeChar == '3') {
				return ShowType.MAX2D;
			}
			if (showTypeChar == '4') {
				return ShowType.MAX3D;
			}
			if (showTypeChar == '6') {
				return ShowType.DMAX;
			}
		}
		return null;
	}
}
