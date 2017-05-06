package cn.mopon.cec.core.assist.district;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import coo.core.jackson.GenericObjectMapper;

public class DistrictHelperTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new GenericObjectMapper();
		log.debug(mapper.writeValueAsString(DistrictHelper.getProvinces()));
		log.debug(DistrictHelper.getDistrict("621000").getName());
		log.debug(DistrictHelper.getDistrict("620923").getName());

		List<String> countyCodes = new ArrayList<String>();
		countyCodes.add("654321");
		countyCodes.add("654003");
		countyCodes.add("654322");
		countyCodes.add("653201");
		countyCodes.add("653001");
		countyCodes.add("652824");
		countyCodes.add("652827");
		countyCodes.add("652901");
		countyCodes.add("640202");
		countyCodes.add("632623");
		countyCodes.add("511123");
		countyCodes.add("511129");
		countyCodes.add("510504");
		countyCodes.add("451423");
		countyCodes.add("450321");
		countyCodes.add("441901");
		countyCodes.add("441622");
		countyCodes.add("140929");
		countyCodes.add("130530");
		countyCodes.add("130526");
		countyCodes.add("654321");

		log.debug((mapper.writeValueAsString(DistrictHelper
				.getProvinces(countyCodes))));
	}
}