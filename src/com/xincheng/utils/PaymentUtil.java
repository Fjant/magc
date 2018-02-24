package com.xincheng.utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @类名：PaymentCore
 * @作者:wanhonghui
 * @日期：2015年12月16日 下午5:21:12
 * @说明：支付平台接口公用函数类
 * 以下代码只是为了第三方接入测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写
 */
public class PaymentUtil {

	/**
	 * 除去请求中的空值和签名参数
	 * @作者：wanhonghui
	 * @日期：2015年12月16日 下午5:22:35
	 * @param sArray
	 * @return 去掉空值与签名参数后的新签名参数
	 */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("signature")|| key.equalsIgnoreCase("signatureType") || key.equalsIgnoreCase("paymentType") || key.equalsIgnoreCase("paymentTypeExt")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    
    /**
     * 把请求中所有参数排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @作者：wanhonghui
     * @日期：2015年12月16日 下午5:24:28
     * @param params 需要排序并参与字符拼接的请求参数
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key)==null ? "" : params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    
    /**
     * 
     * @作者：wanhonghui
     * @日期：2015年12月16日 下午6:30:52
     * @param sPara 要签名的请求参数
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,String key,String input_charset) {
		sPara = PaymentUtil.paraFilter(sPara);
    	String prestr = PaymentUtil.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign =  sign(prestr, key, input_charset);
        return mysign;
    }
    

	
	/**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text +"&key="+ key;
    	try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(getContentBytes(text, input_charset));  
            byte b[] = md.digest();  
  
            int i;  
  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
            //32位加密  
            return buf.toString();  
            // 16位的加密  
            //return buf.toString().substring(8, 24);  
        } catch (NoSuchAlgorithmException e) {  
            return null;  
        }  
    	
    }
    
  
    
   

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}

