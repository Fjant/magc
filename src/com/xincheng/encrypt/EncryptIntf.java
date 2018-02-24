package com.xincheng.encrypt;


/**
 * 加密解密接口 
 * 
 * @author sam_codd
 * @version 1.0
 */
public interface EncryptIntf {
	
	/**
	 * 将传进来的明文以AES算法进行加密
	 * 
	 * @param text
	 *            String
	 * @return String
	 */
	public String enCodeAES(String text) throws Exception;
	
	/**
	 * 将加密文本以AES算法进行解密；
	 * 
	 * @param encryptText
	 *            String
	 * @return String
	 */
	public String deCodeAES(String encryptText) throws Exception;

    /**
     *  将明文文本加密
     * @param text String
     * @return String
     */
    String encrypt(String text) throws Exception;

    /**
     * 将密文解密成明文；
     * @param encryptText String
     * @return String
     */
    String decrypt(String encryptText) throws Exception;
    
    /**
     * 使用MD5算法进行数据加密
     * @param text
     * @return
     * @throws Exception
     */
    public String encryptMD5(String text) throws Exception;
}
