package cn.mopon.cec.core.assist.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import cn.mopon.cec.core.assist.district.District;
import cn.mopon.cec.core.assist.district.DistrictHelper;
import coo.base.constants.Chars;
import coo.base.util.CollectionUtils;
import coo.base.util.StringUtils;
import coo.core.hibernate.usertype.AbstractUserType;

/**
 * 行政区划列表自定义类型。
 */
public class DistrictListUserType extends AbstractUserType {
	private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner) {
		try {
			String value = getValue(rs, names[0], session);
			if (value != null) {
				List<District> districts = new ArrayList<District>();
				for (String districtValue : value.toString().split(Chars.COMMA)) {
					districts.add(DistrictHelper.getDistrict(districtValue));
				}
				return districts;
			} else {
				return new ArrayList<District>();
			}
		} catch (Exception e) {
			throw new HibernateException("转换行政区划类型时发生异常。", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) {
		try {
			List<District> values = (ArrayList<District>) value;
			if (CollectionUtils.isNotEmpty(values)) {
				List<String> districtValues = new ArrayList<String>();
				for (District district : values) {
					districtValues.add(district.getCode());
				}
				setValue(st, StringUtils.join(districtValues, Chars.COMMA),
						index, session);
			} else {
				setValue(st, null, index, session);
			}
		} catch (Exception e) {
			throw new HibernateException("转换行政区划类型时发生异常。", e);
		}
	}

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Class<?> returnedClass() {
		return List.class;
	}
}