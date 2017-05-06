package cn.mopon.cec.core.access.member.ng;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.mopon.cec.core.access.member.vo.MemberCard;
import coo.base.exception.BusinessException;
import coo.base.util.CryptoUtils;
import coo.base.util.StringUtils;

/**
 * NG会员请求对象基类。
 */
public abstract class NgQuery {
	/** 访问路径 */
	private String action;
	/** 参数列表 */
	protected List<NameValuePair> params = new ArrayList<NameValuePair>();

	/**
	 * 构造方法。
	 * 
	 * @param memberCard
	 *            会员卡
	 * @param secKey
	 *            渠道密码
	 */
	public NgQuery(MemberCard memberCard, String secKey) {
		if (StringUtils.isNotEmpty(memberCard.getChipCode())) {
			params.add(new BasicNameValuePair("chipCode", getSecKey(
					memberCard.getChipCode(), secKey)));
		}
		if (StringUtils.isNotEmpty(memberCard.getCardCode())) {
			params.add(new BasicNameValuePair("cardCode", memberCard
					.getCardCode()));
		}
		if (StringUtils.isNotEmpty(memberCard.getPassword())) {
			params.add(new BasicNameValuePair("password", getSecKey(
					memberCard.getPassword(), secKey)));
		}
	}

	/**
	 * AES加密。
	 * 
	 * @param content
	 *            待加密内容
	 * @param channelKey
	 *            渠道密码
	 * @return 返回加密后的结果。
	 */
	private String getSecKey(String content, String channelKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(CryptoUtils.md5(channelKey).toUpperCase()
					.getBytes("UTF-8"));
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
			return parseByte2HexStr(encrypted);
		} catch (Exception e) {
			throw new BusinessException("AES加密错误。");
		}
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	private String parseByte2HexStr(byte[] buf) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}
}
