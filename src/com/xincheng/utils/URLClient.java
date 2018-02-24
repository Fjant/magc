package com.xincheng.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.xincheng.wx.Env.WxEnv;

public class URLClient {
	private static final Logger logger = Logger.getLogger(URLClient.class);
	
	/**
	 * 目前仅支持HTTP，请凌其伟增加到HTTPS的支持
	 * @param conUrl 目标地址
	 * @return
	 */
	public static String  httpURLConectionGET(String conUrl) {
        try {
        	if(conUrl.toLowerCase().startsWith("https")){
        		return httpsURLConectionGET(conUrl);
        	}else{
	            URL url = new URL(conUrl);    // 把字符串转换为URL请求地址
	            HttpURLConnection connection =  null;
	            if("1".equals(WxEnv.propertyUtil.get("SSLCONTEXT_IS_Proxy"))){
	            	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(WxEnv.propertyUtil.get("httpProxHost"), Integer.parseInt(WxEnv.propertyUtil.get("httpProxPort"))));
	            	connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接
	            }
	            else{
	            	 connection = (HttpURLConnection) url.openConnection();// 打开连接
	            }
	          
	            connection.connect();// 连接会话
	            // 获取输入流
	            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line;
	            StringBuilder sb = new StringBuilder();
	            while ((line = br.readLine()) != null) {// 循环读取流
	                sb.append(line);
	            }
	            br.close();// 关闭流
	            connection.disconnect();// 断开连接
	            return sb.toString();
        	}
        } catch (Exception e) {
        	logger.error("URLClient Error",e);
        }
        return null;
    }
	
	
	
	private static class TrustAnyTrustManager implements X509TrustManager {  
		  
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
  
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
  
        public X509Certificate[] getAcceptedIssuers() {  
            return new X509Certificate[] {};  
        }  
    }  
  
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {  
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    }  
  
    private static String httpsURLConectionGET(String url) throws Exception {
//    	logger.info("发送https------------");
        try {  
            SSLContext sslContext = null;  
           // System.setProperty("https.protocols", "TLS");  
        	try{
        		sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				logger.info("SSL TYPE IS SunJSSE");
			}catch(Exception e){
				logger.error("SSL TYPE IS IBMJSSE2 ERROR",e);
			}
			if(sslContext==null){
				sslContext = SSLContext.getInstance("TLSv1.1", "IBMJSSE2");
				logger.info("SSL TYPE IS IBMJSSE2");
			}
			sslContext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());  
            URL console = new URL(url);  
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();  
            conn.setSSLSocketFactory(sslContext.getSocketFactory());  
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
            conn.connect();  
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            br.close();// 关闭流
            conn.disconnect();// 断开连接
           return sb.toString();
        } catch (ConnectException e) {
        	logger.error("异常ConnectException------------",e);
  
        } catch (IOException e) {
        	logger.error("异常IOException------------",e);
        }
        return null;
    }  
}


