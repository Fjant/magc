package com.xincheng.encrypt;

import java.io.Serializable;
import java.util.Random;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.businessmatrix.util.Encrypt;

/**
 * PBEWithMD5AndDES算法加密解密
 * 
 * @author sam_codd
 * @version
 */
public class EncryptImpl implements EncryptIntf, Serializable {
	public EncryptImpl() {
	}

	public EncryptImpl(String password) {
		this.password = password;
	}

	private final static EncryptIntf encrypt = new EncryptImpl();

	private String password = "e_f_codd";

	private final String encoding = "GBK";

	/**
	 * 16进制字符数组
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	/**
	 * AES密匙
	 */
	private final static byte[] keyByte = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66 }; // 16字节的密钥,可以改变

	/**
	 * 一位Byte到16进制字符串的转换
	 * 
	 * @param b
	 *            byte
	 * @return String
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * Byte数组到16进制字符串的转换
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 16进制字符串到Byte转换
	 * 
	 * @param b
	 *            String
	 * @return byte
	 */
	private static byte HexStringTobyte(String b) {
		int By = 0;
		String b1 = b.substring(0, 1);
		int b11 = -1;
		String b2 = b.substring(1);
		int b12 = -1;
		for (int i = 0; i < 16; i++) {
			if (b1.equals(hexDigits[i])) {
				b11 = i;
			}
		}
		for (int i = 0; i < 16; i++) {
			if (b2.equals(hexDigits[i])) {
				b12 = i;
			}
		}
		By = b11 * 16 + b12;
		if (By > 256) {
			By = By - 256;
		}
		return (byte) By;
	}

	/**
	 * 16进制字符串到Byte数组的转换
	 * 
	 * @param b
	 *            String
	 * @return byte[]
	 */
	private static byte[] HexStringTobyteArray(String b) {
		byte[] r = new byte[b.length() / 2];
		for (int i = 0; i < b.length() / 2; i++) {
			r[i] = HexStringTobyte(b.substring(i * 2, i * 2 + 2));
		}
		return r;
	}

	public static EncryptIntf getInstance() {
		return encrypt;
	}

	/**
	 * 将加密文本进行解密；
	 * 
	 * @param encryptText
	 *            String
	 * @return String
	 */
	public String deCodeAES(String encryptText) throws Exception {
		// 通过SecretKeySpec形成一个key
		SecretKey key = new SecretKeySpec(keyByte, "AES");
		// 获得一个私鈅加密类Cipher，ECB是加密方式，PKCS5Padding是填充方法
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// 使用私鈅解密
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] NewCipherText = HexStringTobyteArray(encryptText);
		byte[] newString = cipher.doFinal(NewCipherText);
		return new String(newString, encoding);

	}

	/**
	 * 将传进来的明文以AES算法进行加密
	 * 
	 * @param text
	 *            String
	 * @return String
	 */
	public String enCodeAES(String text) throws Exception {
		byte[] OriByte = text.getBytes(encoding);
		// 通过SecretKeySpec形成一个key
		SecretKey key = new SecretKeySpec(keyByte, "AES");
		// 获得一个私鈅加密类Cipher，ECB是加密方式，PKCS5Padding是填充方法
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// 使用私鈅加密
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] OriCipherText = cipher.doFinal(OriByte);
		String b = byteArrayToHexString(OriCipherText);
		return b; // 密码，转换成16进制
	}

	/**
	 * 将加密文本进行解密；
	 * 
	 * @param encryptText
	 *            String
	 * @return String
	 */
	public String decrypt(String encryptText) throws Exception {
		if (encryptText == null || encryptText.length() == 0) {
			return "";
		}
		PBEKeySpec pbks = new PBEKeySpec((password).toCharArray());

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey k = skf.generateSecret(pbks);

		StringTokenizer st = new StringTokenizer(hex2string(encryptText), " ");
		int num = 0;
		byte[] salt = new byte[8];
		while (st.hasMoreTokens() && (num < 8)) {
			salt[num] = (byte) (Integer.parseInt(st.nextToken()));
			num++;
		}

		int count = 0;
		byte[] cbtemp = new byte[2000];
		while (st.hasMoreTokens()) {
			cbtemp[count] = (byte) (Integer.parseInt(st.nextToken()));
			count++;
		}
		byte[] cb = new byte[count];
		for (int i = 0; i < cb.length; i++) {
			cb[i] = cbtemp[i];
		}
		Cipher cp = Cipher.getInstance("PBEWithMD5AndDES");
		PBEParameterSpec ps = new PBEParameterSpec(salt, 1000);
		cp.init(Cipher.DECRYPT_MODE, k, ps);

		byte[] ptext = cp.doFinal(cb);

		return new String(ptext);

	}

	/**
	 * 将传进来的明文以PBEWithMD5AndDES算法进行加密
	 * 
	 * @param text
	 *            String
	 * @return String
	 */
	public String encrypt(String text) throws Exception {
		if (text == null || text.length() == 0) {
			return "";
		}

		PBEKeySpec pbks = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey k = skf.generateSecret(pbks);
		byte[] salt = new byte[8];
		Random r = new Random();
		r.nextBytes(salt);
		Cipher cp = Cipher.getInstance("PBEWithMD5AndDES");
		PBEParameterSpec ps = new PBEParameterSpec(salt, 1000);
		cp.init(Cipher.ENCRYPT_MODE, k, ps);
		byte[] ptext = text.getBytes(encoding);
		byte[] ctext = cp.doFinal(ptext);

		String result = "";
		for (int i = 0; i < salt.length; i++) {
			result += salt[i] + " ";
		}

		for (int i = 0; i < ctext.length; i++) {
			result += ctext[i] + " ";
		}
		return string2hex(result);
	}

	/**
	 * 使用MD5算法进行数据加密,由于MD5算法不可逆，所以没有相应的解密算法
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String encryptMD5(String text) throws Exception {
		return Encrypt.getInstance().encrypt(text);
	}

	/**
	 * 校验密码 检查明文和密文是否匹配
	 * 
	 * @param text
	 *            明文
	 * @param encryptText
	 *            密文
	 * @return
	 * @throws Exception
	 */
	public boolean verifyEncrypt(String text, String encryptText) throws Exception {
		String xxx = Encrypt.getInstance().decrypt(encryptText);
		System.out.println("xxx:" + xxx);
		return xxx.equals(text);
	}

	/**
	 * 将16进制编码的字符串转换为带有空格分隔的字符串 比如:F89ADFCA2AE9719817D3575A9540600C ==> -8 -102
	 * -33 -54 42 -23 113 -104 23 -45 87 90 -107 64 96 12
	 * 
	 * @param s
	 * @return
	 */
	private String hex2string(String s) {
		String ret = "";
		for (int i = 0; i < s.length() / 2; i++) {
			ret += String.valueOf(Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16)) + " ";

		}
		if (ret.endsWith(" "))
			return ret.substring(0, ret.length() - 1);
		return ret;
	}

	/**
	 * 将加密的带有空格分隔的字符转换为16进制编码的字符串. 比如:-8 -102 -33 -54 42 -23 113 -104 23 -45 87
	 * 90 -107 64 96 12 ==> F89ADFCA2AE9719817D3575A9540600C
	 * 
	 * @param str
	 * @return
	 */
	private String string2hex(String str) {
		String[] split = str.split(" ");
		byte[] b = new byte[split.length];
		for (int i = 0; i < split.length; i++) {
			b[i] = Byte.parseByte(split[i]);
		}

		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;

		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) {
		try {
			EncryptImpl en = (EncryptImpl) EncryptImpl.getInstance();
			// 要加密的用户工号
			String userid = "1";
			// 用户工号加密后的值
			String e_userid = en.encrypt(userid);
			System.out.println("encrypt-->" + e_userid);
			System.out.println("enCodeAES-->" + en.enCodeAES(userid));

			// String xxx =
			// "-128 -70 115 94 -92 -35 -120 -13 121 52 -86 -110 -76 -81 7 84 ";//123456

			String xxx = "100 32 3 -123 19 -121 -111 -46 40 -2 -19 -94 -92 -33 -67 -5 -117 -95 -43 123 96 99 69 75 ";// winmail99

			en.verifyEncrypt(xxx, "11");
			// 对加密的用户工号进行解密
			EncryptImpl den = (EncryptImpl) EncryptImpl.getInstance();
			System.out.println(den.decrypt(e_userid).equals(userid));
			System.out.println(den.deCodeAES(en.enCodeAES(userid)));
			System.out.println(Encrypt.getInstance().decrypt(xxx));
			System.out.println(den.encryptMD5("000000"));
			System.out.println(den.encryptMD5("000000"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
