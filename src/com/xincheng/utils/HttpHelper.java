package com.xincheng.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xincheng.httpProxy.SecureTrustManager;

public class HttpHelper {

	private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

	public static String httpRequest(String requestUrl, String requestMethod, String params) {
		return httpRequest(requestUrl, requestMethod, params, null, null);
	}

	public static String httpRequest(String requestUrl, String requestMethod, String params, Map<String, String> requestProperties) {
		return httpRequest(requestUrl, requestMethod, params, requestProperties, null);
	}

	/**
	 * @param requestUrl
	 * @param requestMethod
	 * @param params
	 * @return
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String params, Map<String, String> requestProperties, Proxy proxy) {
		String responseText = null;
		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpUrlConn = null;

		try {

			URL url = new URL(requestUrl);
			if (proxy != null) {
				httpUrlConn = (HttpURLConnection) url.openConnection(proxy);
				if (httpUrlConn instanceof HttpsURLConnection) {
					HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection(proxy);
					httpsCon.setHostnameVerifier(getHostnameVerifier());
					httpsCon.setSSLSocketFactory(getSslContext().getSocketFactory());
					httpUrlConn = httpsCon;
				}
			} else {
				httpUrlConn = (HttpURLConnection) url.openConnection();
			}

			if (requestProperties != null && requestProperties.size() > 0) {
				for (String key : requestProperties.keySet()) {
					if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(requestProperties.get(key))) {
						httpUrlConn.setRequestProperty(key, requestProperties.get(key));
					}
				}
			}

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setDefaultUseCaches(false);
			httpUrlConn.setConnectTimeout(20000);
			httpUrlConn.setReadTimeout(30000);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			// 当有数据需要提交时

			if (null != params) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(params.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			responseText = buffer.toString();
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.", ce);
			logger.error(requestUrl);
		} catch (SocketException se) {
			logger.error("SocketException ", se);
			logger.error(requestUrl);
		} catch (Exception e) {
			logger.error("https request error:{}", e);
			logger.error(requestUrl);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(bufferedReader);
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}
		
		return responseText;
	}

	private static HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				return true;
			}
		};
	}

	public static SSLContext getSslContext() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new SecureTrustManager() }, new SecureRandom());
		} catch (NoSuchAlgorithmException ex) {
		} catch (KeyManagementException ex) {
		}
		return sslContext;
	}
}
