package cn.mopon.cec.core.assist.settle;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coo.core.jackson.GenericObjectMapper;

public class SettleRuleTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {
		RoundSettleRule roundSettleRule = new RoundSettleRule();
		RoundSettleRuleItem roundSettleRuleItem = new RoundSettleRuleItem();
		roundSettleRuleItem.setStartAmount(20D);
		roundSettleRuleItem.setEndAmount(30D);
		roundSettleRule.getItems().add(roundSettleRuleItem);
		roundSettleRule.getItems().add(roundSettleRuleItem);
		roundSettleRule.getItems().add(roundSettleRuleItem);

		SettleTestBean bean = new SettleTestBean();
		bean.setSettleRule(roundSettleRule);

		GenericObjectMapper mapper = new GenericObjectMapper();
		String json = mapper.writeValueAsString(bean);
		log.debug(json);
		bean = mapper.readValue(json, SettleTestBean.class);
		json = mapper.writeValueAsString(bean);
		log.debug(json);
	}
}
