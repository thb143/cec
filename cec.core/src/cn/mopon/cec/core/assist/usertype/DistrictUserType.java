package cn.mopon.cec.core.assist.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import cn.mopon.cec.core.assist.district.District;
import cn.mopon.cec.core.assist.district.DistrictHelper;
import coo.core.hibernate.usertype.AbstractUserType;

/**
 * 行政区划自定义类型。
 */
public class DistrictUserType extends AbstractUserType {
	private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner) {
		try {
			String value = getValue(rs, names[0], session);
			if (value != null) {
				return DistrictHelper.getDistrict(value);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new HibernateException("转换行政区划类型时发生异常。", e);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) {
		try {
			if (value != null) {
				setValue(st, ((District) value).getCode(), index, session);
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
		return District.class;
	}
}