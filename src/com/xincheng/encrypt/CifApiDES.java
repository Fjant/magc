package com.xincheng.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

/**
 * 
 * DES加解密,支持与delphi交互(字符串编码需统一为UTF-8)
 * 
 *
 * 
 * @author wym
 */

public class CifApiDES {
	private static Logger logger = Logger.getLogger(CifApiDES.class);
	/**
	 * 
	 * 密钥
	 */
	public static String KEY = "";

	private final static String DES = "DES";

	/**
	 * 
	 * 加密
	 */

	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

		// DES算法要求有一个可信任的随机数源

		SecureRandom sr = new SecureRandom();

		// 从原始密匙数据创建DESKeySpec对象

		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密匙工厂，然后用它把DESKeySpec转换成

		// 一个SecretKey对象

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作

		Cipher cipher = Cipher.getInstance(DES);

		// 用密匙初始化Cipher对象

		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		// 现在，获取数据并加密

		// 正式执行加密操作

		return cipher.doFinal(src);

	}

	/**
	 * 
	 * 解密
	 */

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {

		// DES算法要求有一个可信任的随机数源

		SecureRandom sr = new SecureRandom();

		// 从原始密匙数据创建一个DESKeySpec对象

		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成

		// 一个SecretKey对象

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作

		Cipher cipher = Cipher.getInstance(DES);

		// 用密匙初始化Cipher对象

		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		// 现在，获取数据并解密

		// 正式执行解密操作

		return cipher.doFinal(src);

	}

	/**
	 * 
	 * 加密
	 */

	public final static String getEncString(String src, String password) {

		try {

			KEY = password;
			return byte2hex(encrypt(src.getBytes(), KEY.getBytes()));

		} catch (Exception e) {

			logger.error(e.getMessage(), e);

		}

		return null;

	}

	/**
	 * 
	 * 解密
	 */

	public final static String decrypt(String src) {

		try {

			return new String(decrypt(hex2byte(src.getBytes()), KEY.getBytes()));

		} catch (Exception e) {

			logger.error(e.getMessage(), e);

		}

		return null;

	}

	public static String byte2hex(byte[] b) {

		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase();

	}

	public static byte[] hex2byte(byte[] b) {

		if ((b.length % 2) != 0)

			throw new IllegalArgumentException("长度不是偶数");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {

			String item = new String(b, n, 2);

			b2[n / 2] = (byte) Integer.parseInt(item, 16);

		}

		return b2;

	}

	public static void main(String[] args) {

		try {

			String src = "03|2013-07-31|11:30:30";

			String crypto = CifApiDES.getEncString(src, "12345678");

			logger.debug("密文[" + src + "]:" + crypto);

			logger.debug("解密后:" + CifApiDES.decrypt(crypto));

		} catch (Exception e) {

			logger.error(e.getMessage(), e);

		}

	}

}
