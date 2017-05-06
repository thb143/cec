package cn.mopon.cec.core.access.sms;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsServiceTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private SmsService mmsService = new SmsService();;

	@Before
	public void init() {
		mmsService.setUrl("http://192.168.9.132:8080/mts/services/MTS?wsdl");
		mmsService.setSecKey("34qw71f7-a8c9-11e1-8933-");
		mmsService.setClientCode("01");
	}

	@Test
	public void testSMS() throws Exception {
		mmsService.sendSMS("18665984723", "尊敬的客户");
		log.debug("调用短信接口成功。");
	}

	// @Test
	public void testMMS() {
		String imgFile = "e:\\img\\test.jpg";
		String title = "测试彩信";
		String mobile = "18665984723";
		String content = "尊敬的用户，彩信测试。";
		mmsService.sendMMS(mobile, content, title, imgFile);
		log.debug("调用彩信接口成功。");
	}
}
