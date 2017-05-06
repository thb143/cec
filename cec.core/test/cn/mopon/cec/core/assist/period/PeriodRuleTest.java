package cn.mopon.cec.core.assist.period;

import org.joda.time.LocalTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coo.core.jackson.GenericObjectMapper;

public class PeriodRuleTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {
		DayPeriodRule dayPeriodRule = new DayPeriodRule();
		DayPeriodRuleItem dayPeriodRuleItem = new DayPeriodRuleItem();
		dayPeriodRuleItem.setStartTime(LocalTime.parse("09:00"));
		dayPeriodRuleItem.setEndTime(LocalTime.parse("11:00"));
		dayPeriodRule.getItems().add(dayPeriodRuleItem);
		dayPeriodRule.getItems().add(dayPeriodRuleItem);
		dayPeriodRule.getItems().add(dayPeriodRuleItem);

		PeriodTestBean bean = new PeriodTestBean();
		bean.setPeriodRule(dayPeriodRule);

		GenericObjectMapper mapper = new GenericObjectMapper();
		String json = mapper.writeValueAsString(bean);
		log.debug(json);
		bean = mapper.readValue(json, PeriodTestBean.class);
		json = mapper.writeValueAsString(bean);
		log.debug(json);
	}
}
