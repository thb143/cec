package cn.mopon.cec.core.access.ticket.std;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import coo.base.util.CryptoUtils;

/**
 * 密钥对提供类，用于签名和验证签名。
 */
public class KeyPairProvider {
	private static KeyPair keyPair;

	static {
		keyPair = CryptoUtils.genKeyPair();
	}

	public static PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}

	public static PublicKey getPublicKey() {
		return keyPair.getPublic();
	}
}
