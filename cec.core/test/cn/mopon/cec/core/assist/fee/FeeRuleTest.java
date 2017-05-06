package cn.mopon.cec.core.assist.fee;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coo.core.jackson.GenericObjectMapper;

public class FeeRuleTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {
		FixedFeeRule fixedFeeRule = new FixedFeeRule();

		FeeTestBean bean = new FeeTestBean();
		bean.setFeeRule(fixedFeeRule);

		GenericObjectMapper mapper = new GenericObjectMapper();
		String json = mapper.writeValueAsString(bean);
		log.debug(json);
		bean = mapper.readValue(json, FeeTestBean.class);
		json = mapper.writeValueAsString(bean);
		log.debug(json);
	}
}
