package cec.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import coo.base.constants.Encoding;
import coo.base.util.CryptoUtils;
import coo.base.util.StringUtils;

public class GenSignTest {
	public static void main(String[] args) {
		String param = "channelCode=0028";
		String key = "a85bT9PfWoFowAXX";

		Map<String, String> paramsMap = new TreeMap<>();
		String[] pars = param.split("&");
		for (int i = 0; i < pars.length; i++) {
			paramsMap.put(pars[i].split("=")[0], pars[i].split("=")[1]);
		}
		List<String> paramPairs = new ArrayList<String>();
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			paramPairs.add(entry.getKey() + "=" + entry.getValue());
		}
		String paramSignStr = StringUtils.join(paramPairs, "&");

		try {
			System.out.println(URLEncoder.encode(paramSignStr, Encoding.UTF_8));
			String sign = CryptoUtils.md5(key
					+ URLEncoder.encode(paramSignStr, Encoding.UTF_8) + key);
			System.out.println("sign:" + sign);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}