package cn.mopon.cec.core.access.film.csp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.mopon.cec.core.entity.Film;

import com.fasterxml.jackson.databind.ObjectMapper;

import coo.base.exception.BusinessException;
import coo.base.util.CryptoUtils;
import coo.core.jackson.GenericObjectMapper;

/**
 * CSP影片接入实现。
 */
@Service
public class CSPFilmService {
	/** URL */
	@Value("${csp.url:http://183.57.42.24:9998/api/v1/queryFilms.json}")
	private String url;
	/** 账号归属 */
	@Value("${csp.account:cec}")
	private String account;
	/** 客户端密码 */
	@Value("${csp.password:cec.csp}")
	private String key;
	ObjectMapper mapper = new GenericObjectMapper();

	/**
	 * 获取影片信息。
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return 返回符合规则的影片列表。
	 */
	public List<Film> getFilms(Date start, Date end) {
		FilmQuery filmQuery = new FilmQuery(account, CryptoUtils.md5(key),
				start, end);
		try {
			FilmReply reply = getResp(filmQuery);
			return reply.getFilmList();
		} catch (IOException e) {
			throw new BusinessException("调用CSP影片接口失败。", e);
		}
	}

	/**
	 * 获取响应报文体。
	 * 
	 * @param query
	 *            参数
	 * @return 返回reply 。
	 * @throws IOException
	 *             异常
	 */
	private FilmReply getResp(FilmQuery query) throws IOException {
		String rep = execute(query);
		FilmReply resp = mapper.readValue(rep, FilmReply.class);
		if (!"001".equals(resp.getCode())) {
			throw new BusinessException("调用csp接口返回失败，" + resp.getMsg());
		}
		return resp;
	}

	/**
	 * 执行http请求。
	 * 
	 * @param query
	 *            参数
	 * @return json串
	 * @throws IOException
	 *             异常
	 */
	private String execute(CspQuery query) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("zip", "false"));
		nvps.add(new BasicNameValuePair("duid", "duid"));
		nvps.addAll(query.getNvps());
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = httpClient.execute(httpPost);
		String req;
		if (200 == response.getStatusLine().getStatusCode()) {
			req = EntityUtils.toString(response.getEntity());
		} else {
			throw new BusinessException("访问csp接口失败，返回状态码:"
					+ response.getStatusLine().getStatusCode());
		}
		return req;
	}
}