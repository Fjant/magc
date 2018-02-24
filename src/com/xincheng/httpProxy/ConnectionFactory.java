package com.xincheng.httpProxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.xincheng.utils.StringUtil;
import com.xincheng.wx.Env.WxEnv;

/**
 *
 */
@Service
public class ConnectionFactory implements HttpURLConnectionFactory {
	private Logger logger = Logger.getLogger(ConnectionFactory.class);

	Proxy proxy;

	String proxyHost;

	Integer proxyPort;

	SSLContext sslContext;

	public ConnectionFactory() {
		if (WxEnv.propertyUtil.get("SSLCONTEXT_IS_Proxy").trim().equals("1")) {
			this.proxyHost = WxEnv.propertyUtil.get("httpProxHost");
			this.proxyPort = Integer.parseInt(WxEnv.propertyUtil.get("httpProxPort"));
		}
	}

	public ConnectionFactory(String proxyHost, Integer proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	private void initializeProxy() {
		if (proxyHost != null && proxyPort != null) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		}
	}

	@Override
	public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
		initializeProxy();
		HttpURLConnection con = (HttpURLConnection) (proxy == null ? url.openConnection() : url.openConnection(proxy));
		if (con instanceof HttpsURLConnection) {
			HttpsURLConnection httpsCon = (HttpsURLConnection) (proxy == null ? url.openConnection() : url.openConnection(proxy));
			httpsCon.setHostnameVerifier(getHostnameVerifier());
			httpsCon.setSSLSocketFactory(getSslContext().getSocketFactory());
			return httpsCon;
		} else {
			return con;
		}

	}

	public SSLContext getSslContext() {
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new SecureTrustManager() }, new SecureRandom());
		} catch (NoSuchAlgorithmException ex) {
			logger.error(ex);
		} catch (KeyManagementException ex) {
			logger.error(ex);
		}
		return sslContext;
	}

	private HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				return true;
			}
		};
	}

	public String get(String uri, Map<String, String> requestProperties, boolean useProxy) {
		ByteArrayOutputStream out = null;
		URL url = null;
		HttpURLConnection urlConnection = null;

		try {
			
			url = new URL(uri);
			if (useProxy) {
				urlConnection = getHttpURLConnection(url);
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}

			if (requestProperties != null && requestProperties.size() > 0) {
				for (String key : requestProperties.keySet()) {
					if (!StringUtils.isEmpty(key)) {
						urlConnection.setRequestProperty(key, requestProperties.get(key));
					}
				}
			}

			urlConnection.setReadTimeout(60 * 1000);
			urlConnection.connect();
			InputStream in = urlConnection.getInputStream();
			out = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int count = -1;
			while (in != null && (count = in.read(data)) != -1) {
				out.write(data, 0, count);
			}
			String result = out.toString("UTF-8");
			result = StringUtil.removeInvalidUnicodeChar(result);
			return result;
		} catch (Exception e) {
			logger.error(e);
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public String get(String uri) {
		return get(uri, null, true);
	}
}